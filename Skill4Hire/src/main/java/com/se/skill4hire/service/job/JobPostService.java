package com.se.skill4hire.service.job;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.entity.job.JobPostSpecifications;
import com.se.skill4hire.repository.job.JobPostRepository;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.service.exception.JobNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private CompanyAuthRepository companyAuthRepository;

    // Create a new job post for a specific company
    public JobPost createJobPost(JobPost jobPost, Long companyId) {
        Company company = companyAuthRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        jobPost.setCompany(company);
        return jobPostRepository.save(jobPost);
    }

    // Get all job posts for a specific company
    public List<JobPost> getJobPostsByCompany(Long companyId) {
        Company company = companyAuthRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return jobPostRepository.findByCompany(company);
    }

    // Get all active job posts (for candidates) - UPDATED
    public List<JobPost> getActiveJobPosts() {
        return jobPostRepository.findByStatus(JobPost.JobStatus.ACTIVE);
    }

    // Update job post (only if belongs to company)
    public JobPost updateJobPost(Long id, JobPost updatedJobPost, Long companyId) {
        Company company = companyAuthRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return jobPostRepository.findByIdAndCompany(id, company)
                .map(jobPost -> {
                    jobPost.setTitle(updatedJobPost.getTitle());
                    jobPost.setDescription(updatedJobPost.getDescription());
                    jobPost.setType(updatedJobPost.getType());
                    jobPost.setLocation(updatedJobPost.getLocation());
                    jobPost.setSalary(updatedJobPost.getSalary());
                    jobPost.setExperience(updatedJobPost.getExperience());
                    jobPost.setDeadline(updatedJobPost.getDeadline());
                    jobPost.setStatus(updatedJobPost.getStatus());
                    return jobPostRepository.save(jobPost);
                })
                .orElseThrow(() -> new JobNotFoundException("Job post not found with id: " + id + " for company: " + companyId));
    }

    // Delete job post (only if belongs to company)
    public void deleteJobPost(Long id, Long companyId) {
        Company company = companyAuthRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        JobPost jobPost = jobPostRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new JobNotFoundException("Job post not found with id: " + id + " for company: " + companyId));

        jobPostRepository.delete(jobPost);
    }

    // Get job post by ID (with company check for owners, without for candidates)
    public Optional<JobPost> getJobPostById(Long id) {
        return jobPostRepository.findById(id);
    }

    // Get all job posts (employee only) - includes all statuses
    public List<JobPost> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    // Get job posts by type - FIXED: Now using the repository method
    public List<JobPost> getJobPostsByType(String type) {
        // Return only active jobs for public access
        return jobPostRepository.findByTypeAndStatus(type, JobPost.JobStatus.ACTIVE);
    }

    // Get job posts by location - FIXED: Now using the repository method
    public List<JobPost> getJobPostsByLocation(String location) {
        // Return only active jobs for public access
        return jobPostRepository.findByLocationAndStatus(location, JobPost.JobStatus.ACTIVE);
    }

    // Additional method to get job posts by type for a specific company
    public List<JobPost> getJobPostsByTypeForCompany(String type, Long companyId) {
        Company company = companyAuthRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return jobPostRepository.findByTypeAndStatus(type, JobPost.JobStatus.ACTIVE)
                .stream()
                .filter(job -> job.getCompany().getId().equals(companyId))
                .toList();
    }
    // Add to JobPostService
    public List<JobPost> searchJobs(String keyword, String type, String location, Double minSalary, Integer maxExperience) {
        Specification<JobPost> spec = Specification.where(JobPostSpecifications.isActive());

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(JobPostSpecifications.titleOrDescriptionContains(keyword));
        }
        if (type != null) {
            spec = spec.and(JobPostSpecifications.typeEquals(type));
        }
        if (location != null) {
            spec = spec.and(JobPostSpecifications.locationEquals(location));
        }
        if (minSalary != null) {
            spec = spec.and(JobPostSpecifications.salaryAtLeast(minSalary));
        }
        if (maxExperience != null) {
            spec = spec.and(JobPostSpecifications.experienceAtMost(maxExperience));
        }

        return jobPostRepository.findAll(spec);
    }

    public List<String> getDistinctJobTypes() {
        return jobPostRepository.findDistinctTypes();
    }

    public List<String> getDistinctLocations() {
        return jobPostRepository.findDistinctLocations();
    }
}