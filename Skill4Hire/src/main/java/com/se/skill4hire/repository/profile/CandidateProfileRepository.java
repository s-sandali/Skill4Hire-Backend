package com.se.skill4hire.repository.profile;

import com.se.skill4hire.entity.profile.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    Optional<CandidateProfile> findByEmail(String email);
    Optional<CandidateProfile> findByUserId(Long userId);
    boolean existsByEmail(String email);
}