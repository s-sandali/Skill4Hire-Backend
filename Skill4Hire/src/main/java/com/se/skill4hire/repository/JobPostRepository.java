package com.se.skill4hire.repository;

import com.se.skill4hire.entity.JobPost;
import com.se.skill4hire.entity.auth.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {
    List<JobPost> findByCompany(Company company);
    List<JobPost> findByCompanyAndStatus(Company company, JobPost.JobStatus status);
    List<JobPost> findByStatus(JobPost.JobStatus status);
    Optional<JobPost> findByIdAndCompany(Long id, Company company);
}