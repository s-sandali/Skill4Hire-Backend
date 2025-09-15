package com.se.skill4hire.repository;


import com.se.skill4hire.model.CandidateCv;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface CandidateCvRepository extends JpaRepository<CandidateCv, Long> {
    Optional<CandidateCv> findByCandidateId(Long candidateId);
    boolean existsByCandidateId(Long candidateId);
    void deleteByCandidateId(Long candidateId);
}