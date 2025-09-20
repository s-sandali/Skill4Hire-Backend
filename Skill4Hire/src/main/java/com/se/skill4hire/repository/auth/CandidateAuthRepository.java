package com.se.skill4hire.repository.auth;

import com.se.skill4hire.entity.auth.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateAuthRepository extends JpaRepository<Candidate, Long> {
    Candidate findByEmail(String email);
}
