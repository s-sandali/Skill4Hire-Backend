package com.se.skill4hire.repository.profile;

import com.se.skill4hire.entity.auth.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends JpaRepository<Company, Long> {
    // Find a company by email if needed
    Company findByEmail(String email);
}
