package com.se.skill4hire.repository;

import com.se.skill4hire.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    // Find employee by email
    Optional<EmployeeProfile> findByEmail(String email);

    // Find all active employees
    List<EmployeeProfile> findByActiveTrue();

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if email exists for another employee (for update operations)
    boolean existsByEmailAndIdNot(String email, Long id);

    // Soft delete by setting active to false
    @Modifying
    @Query("UPDATE EmployeeProfile e SET e.active = false WHERE e.id = :id")
    void deactivateById(@Param("id") Long id);

    // Find employee by ID and active status
    Optional<EmployeeProfile> findByIdAndActiveTrue(Long id);

    // Find employees by name (case-insensitive search)
    List<EmployeeProfile> findByNameContainingIgnoreCase(String name);

    // Find active employees by name
    List<EmployeeProfile> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    // Find employees by position
    List<EmployeeProfile> findByPosition(String position);

    // Find active employees by position
    List<EmployeeProfile> findByPositionAndActiveTrue(String position);

    // Count all active employees
    long countByActiveTrue();

    // Find employees created after a specific date
    List<EmployeeProfile> findByCreatedAtAfter(java.time.LocalDateTime date);

    // Find employees created before a specific date
    List<EmployeeProfile> findByCreatedAtBefore(java.time.LocalDateTime date);
}