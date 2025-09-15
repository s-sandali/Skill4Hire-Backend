package com.se.skill4hire.repository;

import com.se.skill4hire.model.CandidatePreferredCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatePreferredCompanyRepository extends JpaRepository<CandidatePreferredCompany, Long> {
    List<CandidatePreferredCompany> findByCandidateId(Long candidateId);
    void deleteByCandidateId(Long candidateId);
    boolean existsByCandidateIdAndCompanyNameIgnoreCase(Long candidateId, String companyName);
}

