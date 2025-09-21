package com.se.skill4hire.service.job;

import com.se.skill4hire.entity.JobPost;
import com.se.skill4hire.repository.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    // Create a new job post
    public JobPost createJobPost(JobPost jobPost) {
        return jobPostRepository.save(jobPost);
    }

    // Get all job posts
    public List<JobPost> getAllJobPosts() {
        return jobPostRepository.findAll();
    }

    // Get job post by ID
    public Optional<JobPost> getJobPostById(Long id) {
        return jobPostRepository.findById(id);
    }

    // Update job post
    public JobPost updateJobPost(Long id, JobPost updatedJobPost) {
        return jobPostRepository.findById(id)
                .map(jobPost -> {
                    jobPost.setTitle(updatedJobPost.getTitle());
                    jobPost.setDescription(updatedJobPost.getDescription());
                    jobPost.setType(updatedJobPost.getType());
                    jobPost.setLocation(updatedJobPost.getLocation());
                    jobPost.setSalary(updatedJobPost.getSalary());
                    jobPost.setExperience(updatedJobPost.getExperience());
                    jobPost.setDeadline(updatedJobPost.getDeadline());
                    return jobPostRepository.save(jobPost);
                })
                .orElseThrow(() -> new RuntimeException("Job post not found with id: " + id));
    }

    // Delete job post
    public void deleteJobPost(Long id) {
        if (jobPostRepository.existsById(id)) {
            jobPostRepository.deleteById(id);
        } else {
            throw new RuntimeException("Job post not found with id: " + id);
        }
    }

    // Get job posts by type
    public List<JobPost> getJobPostsByType(String type) {
        return jobPostRepository.findByType(type);
    }

    // Get job posts by location
    public List<JobPost> getJobPostsByLocation(String location) {
        return jobPostRepository.findByLocation(location);
    }
}