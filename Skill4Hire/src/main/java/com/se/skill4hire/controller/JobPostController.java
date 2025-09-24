package com.se.skill4hire.controller;

import com.se.skill4hire.entity.JobPost;
import com.se.skill4hire.service.exception.JobNotFoundException;
import com.se.skill4hire.service.job.JobPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobposts")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    //  Create a new job post
    @PostMapping
    public ResponseEntity<JobPost> createJobPost(@Valid @RequestBody JobPost jobPost) {
        JobPost createdJobPost = jobPostService.createJobPost(jobPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobPost);
    }

    //  Get all job posts
    @GetMapping
    public ResponseEntity<List<JobPost>> getAllJobPosts() {
        List<JobPost> jobPosts = jobPostService.getAllJobPosts();
        if (jobPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosts);
    }

    // Get job post by ID
@GetMapping("/{id}")
public ResponseEntity<JobPost> getJobPostById(@PathVariable Long id) {
    JobPost jobPost = jobPostService.getJobPostById(id)
            .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));
    return ResponseEntity.ok(jobPost);
}


    //  Update job post
    @PutMapping("/{id}")
    public ResponseEntity<JobPost> updateJobPost(@PathVariable Long id,
                                                 @Valid @RequestBody JobPost jobPost) {
        JobPost updatedJobPost = jobPostService.updateJobPost(id, jobPost);
        return ResponseEntity.ok(updatedJobPost);
    }

    //  Delete job post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobPost(@PathVariable Long id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.ok("Job deleted successfully with id: " + id);
    }

    //  Get job posts by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<JobPost>> getJobPostsByType(@PathVariable String type) {
        List<JobPost> jobPosts = jobPostService.getJobPostsByType(type);
        if (jobPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosts);
    }

    //  Get job posts by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobPost>> getJobPostsByLocation(@PathVariable String location) {
        List<JobPost> jobPosts = jobPostService.getJobPostsByLocation(location);
        if (jobPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosts);
    }
}
