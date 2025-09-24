package com.se.skill4hire.repository.auth;

import com.se.skill4hire.entity.auth.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyAuthRepository extends JpaRepository<Company, Long> {

    // Find company by email
    Optional<Company> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);
}
