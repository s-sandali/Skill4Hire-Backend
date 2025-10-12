package com.se.skill4hire.controller.job;

import com.se.skill4hire.dto.job.JobPostDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.auth.User;
import com.se.skill4hire.entity.candidate.CandidateCv;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.service.exception.JobNotFoundException;
import com.se.skill4hire.service.job.JobPostService;
import com.se.skill4hire.service.job.JobSearchService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobposts")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobSearchService jobSearchService;

    // CREATE - only for authenticated companies
    @PostMapping
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<JobPost> createJobPost(@Valid @RequestBody JobPost jobPost, HttpServletRequest request) {
        String companyId = getCompanyIdFromSession(request);
        if (companyId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        jobPost.setCompanyId(companyId);
        JobPost createdJobPost = jobPostService.createJobPost(jobPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobPost);
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
    public ResponseEntity<?> getMyJobPosts(HttpServletRequest request) {
        String companyId = getCompanyIdFromSession(request);
        List<JobPost> jobPosts = jobPostService.getJobPostsByCompanyId(companyId);
        return ResponseEntity.ok(jobPosts);
    }

    // GET JOB BY ID (public - only active)
    @GetMapping("/{id}")
    public ResponseEntity<JobPost> getJobPostById(@PathVariable String id) {
        Optional<JobPost> jobPost = jobPostService.getJobPostById(id);
        return jobPost
                .filter(j -> j.getStatus() == JobPost.JobStatus.ACTIVE)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ENHANCED SEARCH WITH SKILL MATCHING
    @GetMapping("/search/with-matching")
    public ResponseEntity<List<JobSearchService.JobWithMatchScore>> searchJobsWithSkillMatching(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "minSalary", required = false) Double minSalary,
            @RequestParam(value = "maxSalary", required = false) Double maxSalary,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            HttpSession session) {

        String candidateId = (String) session.getAttribute("userId");
        if (candidateId == null) {
            return ResponseEntity.status(401).build();
        }

        // Use skill matching ranking; direct skill filter not needed here
        List<JobSearchService.JobWithMatchScore> jobs = jobSearchService.searchJobsWithSkillMatching(
                candidateId, keyword, type, location, minSalary, maxSalary, maxExperience);

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
    public ResponseEntity<JobPost> updateJobPost(@PathVariable String id, @Valid @RequestBody JobPost jobPost, HttpServletRequest request) {
        String companyId = getCompanyIdFromSession(request);
        if (jobPostService.existsByIdAndCompanyId(id, companyId)) {
            jobPost.setId(id);
            jobPost.setCompanyId(companyId);
            JobPost updatedJobPost = jobPostService.updateJobPost(jobPost);
            return ResponseEntity.ok(updatedJobPost);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // DELETE - Only for job owner company
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> deleteJobPost(@PathVariable String id, HttpServletRequest request) {
        String companyId = getCompanyIdFromSession(request);
        if (jobPostService.existsByIdAndCompanyId(id, companyId)) {
            jobPostService.deleteJobPost(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

    // BASIC SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<JobPost>> searchJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "minSalary", required = false) Double minSalary,
            @RequestParam(value = "maxSalary", required = false) Double maxSalary,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "skill", required = false) String skill) {

        List<JobPost> jobs = jobPostService.searchJobs(keyword, type, location, minSalary, maxSalary, maxExperience, skill);
        return ResponseEntity.ok(jobs);
    }

    private String getCompanyIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        String role = (String) session.getAttribute("role");
        if (role != null && role.equalsIgnoreCase("COMPANY")) {
            return (String) session.getAttribute("userId");
        }
        return null;
    }
}
