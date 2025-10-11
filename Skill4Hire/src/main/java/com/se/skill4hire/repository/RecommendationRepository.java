package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Recommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

    // Find recommendations by employee
    Page<Recommendation> findByEmployeeId(String employeeId, Pageable pageable);

    // Check if recommendation already exists
    Optional<Recommendation> findByEmployeeIdAndCandidateIdAndJobId(String employeeId, String candidateId, String jobId);

    // Find recommendations for a specific job
    Page<Recommendation> findByJobId(String jobId, Pageable pageable);

    // Find recommendations for a specific candidate
    Page<Recommendation> findByCandidateId(String candidateId, Pageable pageable);
}