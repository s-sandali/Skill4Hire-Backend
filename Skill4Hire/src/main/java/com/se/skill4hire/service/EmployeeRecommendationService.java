package com.se.skill4hire.service;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.entity.Recommendation;
import com.se.skill4hire.dto.candidate.CandidateBasicView;
import com.se.skill4hire.dto.recommendation.RecommendationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeRecommendationService {

    // Get all active jobs
    Page<JobPost> getActiveJobs(Pageable pageable);

    // Get candidate profile by ID
    CandidateProfile getCandidateProfile(String candidateId);

    // Search candidates with optional filters
    Page<CandidateProfile> searchCandidates(String skill, Integer minExperience, Pageable pageable);

    // Basic candidate view by ID (name, skills, picture url, cv availability)
    CandidateBasicView getCandidateBasic(String candidateId);

    // Search basic views of candidates
    Page<CandidateBasicView> searchCandidateBasics(String skill, Integer minExperience, Pageable pageable);

    // Recommend a candidate to a job
    Recommendation recommendCandidate(String employeeId, String candidateId, String jobId, String note);

    // Get employee's recommendations (raw)
    Page<Recommendation> getEmployeeRecommendations(String employeeId, Pageable pageable);

    // Get recommendations for a specific job (raw)
    Page<Recommendation> getJobRecommendations(String jobId, Pageable pageable);

    // Get recommendations for a company's jobs (raw)
    Page<Recommendation> getCompanyRecommendations(String companyId, Pageable pageable);

    // Enriched views including candidate name, title, location and avatar URL
    Page<RecommendationView> getEmployeeRecommendationViews(String employeeId, Pageable pageable);

    Page<RecommendationView> getJobRecommendationViews(String jobId, Pageable pageable);

    Page<RecommendationView> getCompanyRecommendationViews(String companyId, Pageable pageable);
}