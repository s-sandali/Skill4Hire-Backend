package com.se.skill4hire.repository.profile;

import com.se.skill4hire.entity.EmployeeProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends MongoRepository<EmployeeProfile, String> {
    Optional<EmployeeProfile> findByUserId(String userId);
}