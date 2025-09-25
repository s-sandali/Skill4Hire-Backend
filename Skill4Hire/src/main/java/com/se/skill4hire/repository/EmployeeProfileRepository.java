package com.se.skill4hire.repository;

import com.se.skill4hire.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    boolean existsByEmail(String email);
    Optional<EmployeeProfile> findByEmail(String email);
}
