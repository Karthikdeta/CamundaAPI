package com.example.demo;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessActivityRepository processActivityRepository;

    public ProcessActivity updateProcessActivityInfo(Long fileId, String processInstanceId,
            String status, String updatedBy) {
        return processActivityRepository.save(ProcessActivity.createProcessActivityInstance(fileId,
                processInstanceId, status, updatedBy));
    }

    public ProcessActivity updateProcessActivityInfo(Long fileId, String status, String updatedBy) {
        return processActivityRepository.save(
                ProcessActivity.createProcessActivityInstance(fileId, null, status, updatedBy));
    }

}
