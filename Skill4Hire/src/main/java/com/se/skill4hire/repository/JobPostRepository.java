package com.se.skill4hire.repository;

import com.se.skill4hire.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    
    List<JobPost> findByType(String type);
    List<JobPost> findByLocation(String location);
}
