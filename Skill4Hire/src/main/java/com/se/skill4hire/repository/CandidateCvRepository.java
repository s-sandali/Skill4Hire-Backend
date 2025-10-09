package com.se.skill4hire.repository;

import com.se.skill4hire.entity.candidate.CandidateCv;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateCvRepository extends MongoRepository<CandidateCv, String> {
    Optional<CandidateCv> findByCandidateId(String candidateId);
    boolean existsByCandidateId(String candidateId);
    void deleteByCandidateId(String candidateId);
}
