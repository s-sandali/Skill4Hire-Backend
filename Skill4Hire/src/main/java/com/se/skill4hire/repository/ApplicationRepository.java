package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByCandidateId(String candidateId);
    List<Application> findByCandidateIdAndStatus(String candidateId, Application.ApplicationStatus status);
    Optional<Application> findByCandidateIdAndJobPostId(String candidateId, String jobPostId);

    // New: company and job-based queries
    List<Application> findByCompanyId(String companyId);
    List<Application> findByCompanyIdAndStatus(String companyId, Application.ApplicationStatus status);
    List<Application> findByJobPostId(String jobPostId);
    List<Application> findByJobPostIdAndStatus(String jobPostId, Application.ApplicationStatus status);
}