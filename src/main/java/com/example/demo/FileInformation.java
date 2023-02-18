package com.example.demo;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FileInformation {
	
	@Id
	private Long fileId;
	
	private String fileName;
	
	private String fileStatus;
	
	private LocalDateTime createdTimestamp;
	
	private LocalDateTime lastUpdatedTimestamp;

}
