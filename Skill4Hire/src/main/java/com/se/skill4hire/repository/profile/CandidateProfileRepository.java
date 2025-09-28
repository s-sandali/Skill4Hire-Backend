package com.se.skill4hire.repository.profile;

import com.se.skill4hire.entity.profile.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {

    // Find profile by auth user ID
    @Query("SELECT cp FROM CandidateProfile cp WHERE cp.user.id = :userId")
    Optional<CandidateProfile> findByUserId(@Param("userId") Long userId);

    // Check if profile exists for user - FIX METHOD NAME
    boolean existsByUser_Id(Long userId);
}