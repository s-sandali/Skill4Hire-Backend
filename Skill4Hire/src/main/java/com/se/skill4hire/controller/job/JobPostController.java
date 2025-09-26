package com.se.skill4hire.controller.job;

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

    // CREATE - Only for companies
    @PostMapping
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<JobPost> createJobPost(@Valid @RequestBody JobPost jobPost, HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        JobPost createdJobPost = jobPostService.createJobPost(jobPost, companyId);
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
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<List<JobPost>> getMyJobPosts(HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        List<JobPost> jobPosts = jobPostService.getJobPostsByCompany(companyId);
        return ResponseEntity.ok(jobPosts);
    }

    // GET JOB BY ID (public)
    @GetMapping("/{id}")
    public ResponseEntity<JobPost> getJobPostById(@PathVariable Long id) {
        JobPost jobPost = jobPostService.getJobPostById(id)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));
        return ResponseEntity.ok(jobPost);
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

        return ResponseEntity.ok(filterOptions);
    }

    // UPDATE - Only for job owner company
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<JobPost> updateJobPost(@PathVariable Long id,
                                                 @Valid @RequestBody JobPost jobPost,
                                                 HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        JobPost updatedJobPost = jobPostService.updateJobPost(id, jobPost, companyId);
        return ResponseEntity.ok(updatedJobPost);
    }

    // DELETE - Only for job owner company
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<String> deleteJobPost(@PathVariable Long id, HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        jobPostService.deleteJobPost(id, companyId);
        return ResponseEntity.ok("Job deleted successfully with id: " + id);
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