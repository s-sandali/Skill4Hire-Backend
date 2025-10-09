package com.se.skill4hire.repository.job;

import com.se.skill4hire.entity.job.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends MongoRepository<JobPost, String> {

    // Find jobs by company
    List<JobPost> findByCompanyId(String companyId);

    // Find jobs by company and status
    List<JobPost> findByCompanyIdAndStatus(String companyId, JobPost.JobStatus status);

    // Find jobs by status
    List<JobPost> findByStatus(JobPost.JobStatus status);

    // Find job by ID and company (for security)
    Optional<JobPost> findByIdAndCompanyId(String id, String companyId);

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
    @Aggregation("{ $match: { status: 'ACTIVE', type: { $ne: null } } }, { $group: { _id: '$type' } }, { $project: { _id: 0, type: '$_id' } }")
    List<String> findDistinctTypes();

    // Get distinct locations for filters
    @Aggregation("{ $match: { status: 'ACTIVE', location: { $ne: null } } }, { $group: { _id: '$location' } }, { $project: { _id: 0, location: '$_id' } }")
    List<String> findDistinctLocations();
}