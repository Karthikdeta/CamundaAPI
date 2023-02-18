package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Worksheet {

    public static void main(String[] args) {
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/engine-rest/process-definition/key/Process_11hza5v", String.class);
        System.out.println(response.getBody());
        Map<String, Object> parsedMap = new GsonJsonParser().parseMap(response.getBody());
        System.out.println(parsedMap);
        System.out.println(parsedMap.size());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        
        
        int fileId = 101;
        
        JsonObject fileIdObj = new JsonObject();
        fileIdObj.addProperty("value", 101);
        fileIdObj.addProperty("type", "Integer");
        
        JsonObject variablesObj = new JsonObject();
        
        String requestBody = "{\"variables\": {\"fileId\": { \"value\": " + fileId + ", \"type\": \"Integer\" } }, \"businessKey\": " + fileId + " }";
        HttpEntity entity = new HttpEntity(requestBody, headers);     
       // ResponseEntity<String> response1 = restTemplate.postForEntity("http://localhost:8080/engine-rest/process-definition/Process_11hza5v:18:d8149dd4-ae11-11ed-9e95-60a5e28b92c3/start", entity, String.class);        
       // System.out.println(response1);
        ResponseEntity<List> response1 = restTemplate.getForEntity("http://localhost:8080/engine-rest/task?processInstanceId=f0d3e9c5-ae11-11ed-9e95-60a5e28b92c3", List.class);
        System.out.println(((Map<String, String>) response1.getBody().get(0)).get("id"));
        
    }

}
