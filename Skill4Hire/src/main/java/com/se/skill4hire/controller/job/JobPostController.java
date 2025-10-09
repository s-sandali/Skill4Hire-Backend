package com.se.skill4hire.controller.job;

import com.se.skill4hire.dto.job.JobPostDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.auth.User;
import com.se.skill4hire.entity.auth.CandidateCv;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.service.exception.JobNotFoundException;
import com.se.skill4hire.service.job.JobPostService;
import com.se.skill4hire.service.job.JobSearchService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobposts")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobSearchService jobSearchService;

   // Just the CREATE method - add this to your existing controller

@PostMapping
@PreAuthorize("hasAuthority('COMPANY')")
public ResponseEntity<?> createJobPost(@Valid @RequestBody JobPostDTO jobRequest, HttpSession session) {
    try {
        Long companyId = (Long) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "User not authenticated"));
        }
        
        System.out.println("📦 Creating job for company ID: " + companyId);
        System.out.println("📦 Job data received: " + jobRequest);
        
        // Convert DTO to entity
        JobPost jobPost = new JobPost();
        jobPost.setTitle(jobRequest.getTitle());
        jobPost.setDescription(jobRequest.getDescription());
        jobPost.setType(jobRequest.getType());
        jobPost.setLocation(jobRequest.getLocation());
        jobPost.setSalary(jobRequest.getSalary());
        jobPost.setExperience(jobRequest.getExperience());
        jobPost.setDeadline(jobRequest.getDeadline());
        
        JobPost createdJobPost = jobPostService.createJobPost(jobPost, companyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobPost);
    } catch (Exception e) {
        System.err.println("❌ Error creating job post: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", "Failed to create job post: " + e.getMessage()));
    }
}
    // GET ALL ACTIVE JOBS (for candidates - public)
    @GetMapping
    public ResponseEntity<List<JobPost>> getAllActiveJobPosts() {
        List<JobPost> jobPosts = jobPostService.getActiveJobPosts();
        if (jobPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosts);
    }

    // GET COMPANY'S JOBS (for company dashboard)
    @GetMapping("/my-jobs")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<?> getMyJobPosts(HttpSession session) {
        try {
            Long companyId = (Long) session.getAttribute("userId");
            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not authenticated"));
            }
            
            System.out.println("📦 Fetching jobs for company ID: " + companyId);
            List<JobPost> jobPosts = jobPostService.getJobPostsByCompany(companyId);
            System.out.println("📦 Found " + jobPosts.size() + " jobs");
            return ResponseEntity.ok(jobPosts);
        } catch (Exception e) {
            System.err.println("❌ Error fetching company jobs: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to fetch jobs: " + e.getMessage()));
        }
    }

    // GET JOB BY ID (public)
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobPostById(@PathVariable Long id) {
        try {
            JobPost jobPost = jobPostService.getJobPostById(id)
                    .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));
            return ResponseEntity.ok(jobPost);
        } catch (JobNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to fetch job: " + e.getMessage()));
        }
    }

    // ENHANCED SEARCH WITH SKILL MATCHING
    @GetMapping("/search/with-matching")
    public ResponseEntity<List<JobSearchService.JobWithMatchScore>> searchJobsWithSkillMatching(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "minSalary", required = false) Double minSalary,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            HttpSession session) {

        Long candidateId = (Long) session.getAttribute("userId");
        if (candidateId == null) {
            return ResponseEntity.status(401).build();
        }

        List<JobSearchService.JobWithMatchScore> jobs = jobSearchService.searchJobsWithSkillMatching(
                candidateId, keyword, type, location, minSalary, maxExperience);

        return ResponseEntity.ok(jobs);
    }

    // GET FEATURED JOBS (active jobs with recent deadline)
    @GetMapping("/featured")
    public ResponseEntity<List<JobPost>> getFeaturedJobs() {
        List<JobPost> featuredJobs = jobPostService.getActiveJobPosts().stream()
                .sorted((j1, j2) -> j2.getCreatedAt().compareTo(j1.getCreatedAt()))
                .limit(10)
                .collect(Collectors.toList());

        return ResponseEntity.ok(featuredJobs);
    }

    // GET FILTER OPTIONS FOR DROPDOWNS
    @GetMapping("/filter-options")
    public ResponseEntity<Map<String, List<String>>> getFilterOptions() {
        Map<String, List<String>> filterOptions = new HashMap<>();

        filterOptions.put("types", jobPostService.getDistinctJobTypes());
        filterOptions.put("locations", jobPostService.getDistinctLocations());
        filterOptions.put("salaryRanges", List.of("0-30000", "30000-50000", "50000-70000", "70000-100000", "100000+"));
        filterOptions.put("experienceRanges", List.of("0-2", "2-5", "5-10", "10+"));
        filterOptions.put("candidateStatuses", Arrays.stream(Application.ApplicationStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        filterOptions.put("cvStatuses", Arrays.stream(CandidateCv.CvStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(filterOptions);
    }

    // UPDATE - Only for job owner company
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<?> updateJobPost(@PathVariable Long id,
                                         @Valid @RequestBody JobPost jobPost,
                                         HttpSession session) {
        try {
            Long companyId = (Long) session.getAttribute("userId");
            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not authenticated"));
            }
            
            System.out.println("📦 Updating job ID: " + id + " for company ID: " + companyId);
            System.out.println("📦 Job data received: " + jobPost);
            
            JobPost updatedJobPost = jobPostService.updateJobPost(id, jobPost, companyId);
            return ResponseEntity.ok(updatedJobPost);
        } catch (JobNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error updating job post: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Failed to update job post: " + e.getMessage()));
        }
    }

    // DELETE - Only for job owner company
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<?> deleteJobPost(@PathVariable Long id, HttpSession session) {
        try {
            Long companyId = (Long) session.getAttribute("userId");
            if (companyId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "User not authenticated"));
            }
            
            System.out.println("📦 Deleting job ID: " + id + " for company ID: " + companyId);
            jobPostService.deleteJobPost(id, companyId);
            return ResponseEntity.ok(Map.of("message", "Job deleted successfully with id: " + id));
        } catch (JobNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error deleting job post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to delete job: " + e.getMessage()));
        }
    }

    // EXISTING FILTER ENDPOINTS (public)
    @GetMapping("/type/{type}")
    public ResponseEntity<List<JobPost>> getJobPostsByType(@PathVariable String type) {
        List<JobPost> jobPosts = jobPostService.getJobPostsByType(type);
        if (jobPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosts);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobPost>> getJobPostsByLocation(@PathVariable String location) {
        List<JobPost> jobPosts = jobPostService.getJobPostsByLocation(location);
        if (jobPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosts);
    }

    // BASIC SEARCH (public - no authentication required)
    @GetMapping("/search")
    public ResponseEntity<List<JobPost>> searchJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "minSalary", required = false) Double minSalary,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience) {

        List<JobPost> jobs = jobPostService.searchJobs(keyword, type, location, minSalary, maxExperience);
        return ResponseEntity.ok(jobs);
    }
}