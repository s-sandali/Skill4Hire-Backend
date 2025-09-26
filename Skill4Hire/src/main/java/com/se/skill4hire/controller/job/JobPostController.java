package com.se.skill4hire.controller.job;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.service.exception.JobNotFoundException;
import com.se.skill4hire.service.job.JobPostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobposts")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    // CREATE - Only for companies
    @PostMapping
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<JobPost> createJobPost(@Valid @RequestBody JobPost jobPost, HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        JobPost createdJobPost = jobPostService.createJobPost(jobPost, companyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobPost);
    }

    // GET ALL ACTIVE JOBS (for candidates - public) - UPDATED
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

    // EXISTING FILTER ENDPOINTS (public) - FIXED
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