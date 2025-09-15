package com.se.skill4hire.repository;

import com.se.skill4hire.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminProfile, Long> {
    AdminProfile findByEmail(String email);
}