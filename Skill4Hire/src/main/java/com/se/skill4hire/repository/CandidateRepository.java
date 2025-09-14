package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Candidate findByEmail(String email); // simple method
}
