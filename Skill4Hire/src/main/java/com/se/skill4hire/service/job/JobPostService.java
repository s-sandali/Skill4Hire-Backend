package com.se.skill4hire.service.job;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.job.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    // Create a new job post for a specific company
    public JobPost createJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    // Get job post by ID (with company check for owners, without for candidates)
    public Optional<JobPost> getJobPostById(String id) {
        return jobPostRepository.findById(id);
    }

    // Get all job posts (employee only) - includes all statuses
    public List<JobPost> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    // Get all job posts for a specific company
    public List<JobPost> getJobPostsByCompanyId(String companyId) {
        return jobPostRepository.findByCompanyId(companyId);
    }

    // Get all active job posts (for candidates) - UPDATED
    public List<JobPost> getActiveJobPosts() {
        return jobPostRepository.findByStatus(JobPost.JobStatus.ACTIVE);
    }

    // Update job post (only if belongs to company)
    public JobPost updateJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    // Delete job post (only if belongs to company)
    public void deleteJobPost(String id) {
        jobPostRepository.deleteById(id);
    }

    // Get job posts by type - FIXED: Now using the repository method
    public List<JobPost> getJobPostsByType(String type) {
        return jobPostRepository.findByType(type);
    }

    // Get job posts by location - FIXED: Now using the repository method
    public List<JobPost> getJobPostsByLocation(String location) {
        return jobPostRepository.findByLocation(location);
    }

    // Additional method to get job posts by type for a specific company
    public List<JobPost> getJobPostsByTypeForCompany(String type, String companyId) {
        return jobPostRepository.findByTypeAndStatus(type, JobPost.JobStatus.ACTIVE)
                .stream()
                .filter(job -> job.getCompanyId() != null && job.getCompanyId().equals(companyId))
                .toList();
    }

    public List<JobPost> getJobPostsByStatus(JobPost.JobStatus status) {
        return jobPostRepository.findByStatus(status);
    }

    public Page<JobPost> getJobPostsByStatusPaged(JobPost.JobStatus status, Pageable pageable) {
        return jobPostRepository.findByStatus(status, pageable);
    }

    public List<JobPost> getJobPostsByLocationAndStatus(String location, JobPost.JobStatus status) {
        return jobPostRepository.findByLocationAndStatus(location, status);
    }

    public List<String> getDistinctTypes() {
        return jobPostRepository.findDistinctTypes();
    }

    public List<String> getDistinctLocations() {
        return jobPostRepository.findDistinctLocations();
    }

    public boolean existsByIdAndCompanyId(String id, String companyId) {
        return jobPostRepository.findByIdAndCompanyId(id, companyId).isPresent();
    }

    // Alias for controller compatibility
    public List<String> getDistinctJobTypes() {
        return getDistinctTypes();
    }

    // Search helper used by controller and JobSearchService
    public List<JobPost> searchJobs(String keyword, String type, String location, Double minSalary, Double maxSalary, Integer maxExperience, String skill) {
        // Start from active jobs to limit results
        List<JobPost> jobs = jobPostRepository.findByStatus(JobPost.JobStatus.ACTIVE);

        return jobs.stream()
                .filter(j -> {
                    if (keyword != null && !keyword.isBlank()) {
                        String text = (safe(j.getTitle()) + " " + safe(j.getDescription())).toLowerCase();
                        if (!text.contains(keyword.toLowerCase())) return false;
                    }
                    if (type != null && !type.isBlank() && (j.getType() == null || !j.getType().equalsIgnoreCase(type))) return false;
                    if (location != null && !location.isBlank() && (j.getLocation() == null || !j.getLocation().equalsIgnoreCase(location))) return false;
                    if (minSalary != null && (j.getSalary() == null || j.getSalary() < minSalary)) return false;
                    if (maxSalary != null && (j.getSalary() == null || j.getSalary() > maxSalary)) return false;
                    if (maxExperience != null && (j.getExperience() == null || j.getExperience() > maxExperience)) return false;
                    if (skill != null && !skill.isBlank()) {
                        String s = skill.toLowerCase();
                        boolean inList = j.getSkills() != null && j.getSkills().stream().anyMatch(x -> x != null && x.toLowerCase().contains(s));
                        boolean inText = (safe(j.getTitle()) + " " + safe(j.getDescription())).toLowerCase().contains(s);
                        if (!(inList || inText)) return false;
                    }
                    return true;
                })
                .toList();
    }

    private String safe(String s) { return s == null ? "" : s; }
}