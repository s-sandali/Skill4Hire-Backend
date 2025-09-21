package com.se.skill4hire.controller;

import com.se.skill4hire.entity.JobPost;
import com.se.skill4hire.service.job.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobposts")
@CrossOrigin(origins = "*") // Configure CORS as needed
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    // Create a new job post
    @PostMapping
    public ResponseEntity<JobPost> createJobPost(@RequestBody JobPost jobPost) {
        try {
            JobPost createdJobPost = jobPostService.createJobPost(jobPost);
            return new ResponseEntity<>(createdJobPost, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all job posts
    @GetMapping
    public ResponseEntity<List<JobPost>> getAllJobPosts() {
        try {
            List<JobPost> jobPosts = jobPostService.getAllJobPosts();
            if (jobPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jobPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get job post by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobPost> getJobPostById(@PathVariable("id") Long id) {
        try {
            Optional<JobPost> jobPost = jobPostService.getJobPostById(id);
            if (jobPost.isPresent()) {
                return new ResponseEntity<>(jobPost.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update job post
    @PutMapping("/{id}")
    public ResponseEntity<JobPost> updateJobPost(@PathVariable("id") Long id, @RequestBody JobPost jobPost) {
        try {
            JobPost updatedJobPost = jobPostService.updateJobPost(id, jobPost);
            return new ResponseEntity<>(updatedJobPost, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete job post
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteJobPost(@PathVariable("id") Long id) {
        try {
            jobPostService.deleteJobPost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get job posts by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<JobPost>> getJobPostsByType(@PathVariable("type") String type) {
        try {
            List<JobPost> jobPosts = jobPostService.getJobPostsByType(type);
            if (jobPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jobPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get job posts by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobPost>> getJobPostsByLocation(@PathVariable("location") String location) {
        try {
            List<JobPost> jobPosts = jobPostService.getJobPostsByLocation(location);
            if (jobPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(jobPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}