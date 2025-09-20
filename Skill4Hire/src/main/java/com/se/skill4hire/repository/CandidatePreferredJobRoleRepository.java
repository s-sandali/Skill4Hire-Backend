package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatePreferredJobRoleRepository extends JpaRepository<Notification.CandidatePreferredJobRole, Long> {
    List<Notification.CandidatePreferredJobRole> findByCandidateId(Long candidateId);
    void deleteByCandidateId(Long candidateId);
    boolean existsByCandidateIdAndJobRoleIgnoreCase(Long candidateId, String jobRole);
}

