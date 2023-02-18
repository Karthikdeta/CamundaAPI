package com.example.demo;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {

    private final FileService fileService;
    private final CamundaService camundaService;

    private static final String PROCESS_KEY = "Process_11hza5v";

    @PostMapping("fileValidation")
    public ResponseEntity<FileStatus> fileValidation(@RequestBody FileInformation fileInfo) {
        log.info("Incoming File Validation Request - {}", fileInfo.getFileId());
        return fileService.randomResponse(fileInfo.getFileId(), "File Validation Successful",
                "File Validation Failed");
    }

    @PostMapping("generateReport")
    public ResponseEntity<FileStatus> generateReport(@RequestBody FileInformation fileInfo) {
        log.info("Incoming Generate Report Request");
        return fileService.randomResponse(fileInfo.getFileId(), "Report Generation Successful",
                "Report Generation Failed");
    }

    @GetMapping("getAllFileInformation")
    public List<FileInformation> getAllFileInformation() {
        return this.fileService.getAllFileInformation();
    }

    @GetMapping("getFileInformation/{id}")
    public FileInformation getFileInformationById(@PathVariable Long id) {
        return this.fileService.getFileInformationById(id);
    }

    @PostMapping("fileUpload/{fileName}")
    public FileInformation fileUpload(@PathVariable String fileName) {
        FileInformation fileInfo = fileService.fileUpload(fileName);
        camundaService.startProcessInstance(fileInfo.getFileId(),
                camundaService.getProcessDefinitionId(PROCESS_KEY));
        return fileInfo;
    }

    @PostMapping("completeTask/{fileId}")
    public FileStatus completeUserTask(@PathVariable Long fileId,
            @RequestParam("taskName") String taskName) {
        if (camundaService.completeUserTask(fileId, taskName).equals("Success")) {
            return FileStatus.builder().status("Success").build();
        }
        return FileStatus.builder().status("Failed").isError(true)
                .errorInfo("Unable to complete user task - " + taskName).build();
    }

}
