package com.se.skill4hire.repository.auth;

import com.se.skill4hire.entity.auth.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateAuthRepository extends MongoRepository<Candidate, String> {
    Candidate findByEmail(String email);
}
