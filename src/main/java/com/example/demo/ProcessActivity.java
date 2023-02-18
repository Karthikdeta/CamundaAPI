package com.example.demo;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProcessActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fileId;

    private String processInstanceId;

    private String status;

    private LocalDateTime activityTimeStamp;

    private String recUpdatedUser;

    public static ProcessActivity createProcessActivityInstance(Long fileId, String processInstanceId,
            String status, String updatedBy) {
        ProcessActivity activity = ProcessActivity.builder().fileId(fileId)
                .processInstanceId(processInstanceId).status(status)
                .activityTimeStamp(LocalDateTime.now()).recUpdatedUser(updatedBy).build();
        return activity;
    }

}
