package com.se.skill4hire.repository;

import com.se.skill4hire.entity.EmployeeNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeNotificationRepository extends MongoRepository<EmployeeNotification, String> {
    List<EmployeeNotification> findByEmployeeIdOrderByCreatedAtDesc(String employeeId);
    List<EmployeeNotification> findByEmployeeIdAndReadFalseOrderByCreatedAtDesc(String employeeId);
    long countByEmployeeIdAndReadFalse(String employeeId);

    Page<EmployeeNotification> findByEmployeeId(String employeeId, Pageable pageable);
}