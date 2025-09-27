package com.se.skill4hire.repository.profile;

import com.se.skill4hire.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    // Find profile by auth user ID
    @Query("SELECT ep FROM EmployeeProfile ep WHERE ep.user.id = :userId")
    Optional<EmployeeProfile> findByUserId(@Param("userId") Long userId);

    // Check if Profile exists for user
    boolean existsByUser_Id(Long userId);
}