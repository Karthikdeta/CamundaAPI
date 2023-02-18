package com.example.demo;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProcessController {

    private final ProcessActivityRepository processActivityRepository;

    @GetMapping("/getProcessActivityInfo/{fileId}")
    public List<ProcessActivity> getProcessActivityInfo(@PathVariable Long fileId) {
        return processActivityRepository.findByFileIdOrderByIdDesc(fileId);
    }

}
