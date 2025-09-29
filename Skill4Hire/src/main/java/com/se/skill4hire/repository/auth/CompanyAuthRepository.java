package com.se.skill4hire.repository.auth;

import com.se.skill4hire.entity.auth.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyAuthRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
}
