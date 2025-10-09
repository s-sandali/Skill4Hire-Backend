package com.se.skill4hire.service.application;

import com.se.skill4hire.entity.Application;
import com.se.skill4hire.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateApplicationTrackingService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationService applicationService;

    public List<Application> getApplicationsByCandidate(String candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    public List<Application> getApplicationsByCandidateAndStatus(String candidateId, Application.ApplicationStatus status) {
        return applicationRepository.findByCandidateIdAndStatus(candidateId, status);
    }

    public List<Application> getApplicationsWithJobDetails(String candidateId) {
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);
        // Since jobPost is now jobPostId, we can fetch job details separately if needed
        // For now, return applications as is
        return applications;
    }

    // Bridge method to return DTOs using ApplicationService
    public List<com.se.skill4hire.dto.application.ApplicationDTO> getAllApplicationsForCandidate(String candidateId) {
        return applicationService.list(candidateId, null);
    }
}
