package com.se.skill4hire.repository.profile;

import com.se.skill4hire.entity.CompanyProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends MongoRepository<CompanyProfile, String> {
    // No custom methods needed
}
