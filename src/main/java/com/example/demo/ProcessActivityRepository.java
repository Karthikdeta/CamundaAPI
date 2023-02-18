package com.example.demo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessActivityRepository extends JpaRepository<ProcessActivity, Long> {

    ProcessActivity findFirstByfileId(Long fileId);

    List<ProcessActivity> findByFileIdOrderByIdDesc(Long fileId);

}
