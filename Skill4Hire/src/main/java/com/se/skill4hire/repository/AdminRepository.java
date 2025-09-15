package com.se.skill4hire.repository;

import com.se.skill4hire.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository  // Optional, but good to have
public interface AdminRepository extends JpaRepository<AdminProfile, Long> {
    Optional<AdminProfile> findByEmail(String email);
}
