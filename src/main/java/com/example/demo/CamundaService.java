package com.example.demo;

import static com.example.demo.Constants.USER_PROCESS;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CamundaService {

    private final ProcessActivityRepository processActivityRepository;
    private final FileService fileService;

    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public String startProcessInstance(Long fileId, String processDefinitionId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"variables\": {\"fileId\": { \"value\": " + fileId
                + ", \"type\": \"Integer\" } }, \"businessKey\": " + fileId + " }";

        HttpEntity entity = new HttpEntity(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8080/engine-rest/process-definition/{processDefinitionId}/start",
                entity, String.class, Map.of("processDefinitionId", processDefinitionId));

        if (response.getStatusCode() == HttpStatus.OK) {
            String processInstanceId = (String) CommonUtils.parseJson(response.getBody()).get("id");
            ProcessActivity activity = ProcessActivity.builder().fileId(fileId)
                    .processInstanceId(processInstanceId).status("Started")
                    .activityTimeStamp(LocalDateTime.now()).recUpdatedUser(USER_PROCESS).build();
            processActivityRepository.save(activity);
            return processInstanceId;
        }
        throw new RuntimeException("Unable to start process instance");
    }

    @SuppressWarnings("unchecked")
    public String getUserTask(String processInstanceId) {
        @SuppressWarnings("rawtypes")
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:8080/engine-rest/task?processInstanceId=" + processInstanceId,
                List.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ((Map<String, String>) response.getBody().get(0)).get("id");
        }
        throw new RuntimeException("Unable to get User Task");
    }

    public String getProcessDefinitionId(String processKey) {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:8080/engine-rest/process-definition/key/" + processKey,
                String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return (String) CommonUtils.parseJson(response.getBody()).get("id");
        }
        throw new RuntimeException("Unable to get process definition id");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public String completeUserTask(Long fileId, String taskName) {
        ProcessActivity activity = processActivityRepository.findFirstByfileId(fileId);
        if (activity != null) {
            String userTaskId = getUserTask(activity.getProcessInstanceId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/engine-rest/task/{userTaskId}/complete", entity,
                    String.class, Map.of("userTaskId", userTaskId));
            if ((response.getStatusCode() == HttpStatus.OK) || (response.getStatusCode() == HttpStatus.NO_CONTENT)) {
                taskName = taskName.equalsIgnoreCase("Complete") ? "Processed" : taskName + " Completed";
                ProcessActivity processActivity = ProcessActivity.builder().fileId(fileId)
                        .processInstanceId(activity.getProcessInstanceId())
                        .status(taskName).activityTimeStamp(LocalDateTime.now())
                        .recUpdatedUser(USER_PROCESS).build();
                processActivityRepository.save(processActivity);
                if(taskName.equalsIgnoreCase("Processed")) {
                    fileService.updateFileStatus(fileId, taskName);
                }
                return "Success";
            }
            throw new RuntimeException("Unable to complete user task");
        }
        return null;
    }

}
