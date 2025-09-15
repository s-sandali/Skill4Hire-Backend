package com.se.skill4hire.repository;

import com.se.skill4hire.model.CandidatePreferredJobRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatePreferredJobRoleRepository extends JpaRepository<CandidatePreferredJobRole, Long> {
    List<CandidatePreferredJobRole> findByCandidateId(Long candidateId);
    void deleteByCandidateId(Long candidateId);
    boolean existsByCandidateIdAndJobRoleIgnoreCase(Long candidateId, String jobRole);
}

