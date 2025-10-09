package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByCandidateId(String candidateId);
    List<Application> findByCandidateIdAndStatus(String candidateId, Application.ApplicationStatus status);
}
