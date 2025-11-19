package com.se.skill4hire.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.skill4hire.entity.CompanyNotification;

@Repository
public interface CompanyNotificationRepository extends MongoRepository<CompanyNotification, String> {
    List<CompanyNotification> findByCompanyIdOrderByCreatedAtDesc(String companyId);
    List<CompanyNotification> findByCompanyIdAndReadFalseOrderByCreatedAtDesc(String companyId);
    long countByCompanyIdAndReadFalse(String companyId);
}
