package com.example.demo;

import static com.example.demo.Constants.USER_SERVICE;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileInformationRepository fileInformationRepository;
    private final ProcessService processService;

    public List<FileInformation> getAllFileInformation() {
        return this.fileInformationRepository.findAll(Sort.by(Order.desc("fileId")));
    }

    public FileInformation getFileInformationById(Long id) {
        return this.fileInformationRepository.findById(id).get();
    }

    public FileInformation fileUpload(String fileName) {
        Long count = this.fileInformationRepository.count();
        LocalDateTime currentDateTime = LocalDateTime.now();
        return this.fileInformationRepository.save(FileInformation.builder().fileName(fileName)
                .fileStatus("File Uploading").fileId(count + 1).createdTimestamp(currentDateTime)
                .lastUpdatedTimestamp(currentDateTime).build());
    }

    public FileInformation updateFileStatus(Long fileId, String status) {
        FileInformation fileEntity = getFileInformationById(fileId);
        fileEntity.setFileStatus(status);
        fileEntity.setLastUpdatedTimestamp(LocalDateTime.now());
        return this.fileInformationRepository.save(fileEntity);
    }

    public ResponseEntity<FileStatus> randomResponse(Long fileId, String successMsg,
            String failureMsg) {
        Random random = new Random();
        int randomNum = random.nextInt(1000);
        if (randomNum % 2 == 0) {
            this.updateFileStatus(fileId, successMsg);
            processService.updateProcessActivityInfo(fileId, successMsg, USER_SERVICE);
            return new ResponseEntity<FileStatus>(FileStatus.builder().status(successMsg).build(),
                    HttpStatus.OK);
        }
        this.updateFileStatus(fileId, failureMsg);
        processService.updateProcessActivityInfo(fileId, failureMsg, USER_SERVICE);
        return new ResponseEntity<FileStatus>(FileStatus.builder().status(failureMsg).isError(true)
                .errorInfo("Technical Failure").build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
