package com.se.skill4hire.repository.job;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.auth.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {

    // Find jobs by company
    List<JobPost> findByCompany(Company company);

    // Find jobs by company and status
    List<JobPost> findByCompanyAndStatus(Company company, JobPost.JobStatus status);

    // Find jobs by status
    List<JobPost> findByStatus(JobPost.JobStatus status);

    // Find job by ID and company (for security)
    Optional<JobPost> findByIdAndCompany(Long id, Company company);

    // Find jobs by type (ADD THIS METHOD)
    List<JobPost> findByType(String type);

    // Find jobs by location (ADD THIS METHOD)
    List<JobPost> findByLocation(String location);

    // Find jobs by type and status
    List<JobPost> findByTypeAndStatus(String type, JobPost.JobStatus status);

    // Find jobs by location and status
    List<JobPost> findByLocationAndStatus(String location, JobPost.JobStatus status);

    // Paginated version of findByStatus
    Page<JobPost> findByStatus(JobPost.JobStatus status, Pageable pageable);

    // Get distinct job types for filters
    @Query("SELECT DISTINCT j.type FROM JobPost j WHERE j.status = 'ACTIVE' AND j.type IS NOT NULL")
    List<String> findDistinctTypes();

    // Get distinct locations for filters
    @Query("SELECT DISTINCT j.location FROM JobPost j WHERE j.status = 'ACTIVE' AND j.location IS NOT NULL")
    List<String> findDistinctLocations();
}