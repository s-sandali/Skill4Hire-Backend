package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CandidatePreferredJobRoleRepository extends MongoRepository<Notification.CandidatePreferredJobRole, String> {
    List<Notification.CandidatePreferredJobRole> findByCandidateId(String candidateId);
    void deleteByCandidateId(String candidateId);
    boolean existsByCandidateIdAndJobRoleIgnoreCase(String candidateId, String jobRole);
}
