package com.se.skill4hire.repository;

import com.se.skill4hire.entity.auth.CandidateCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateCvRepository extends JpaRepository<CandidateCv, Long> {
    Optional<CandidateCv> findByCandidateId(Long candidateId);
    boolean existsByCandidateId(Long candidateId);
    void deleteByCandidateId(Long candidateId);
}
