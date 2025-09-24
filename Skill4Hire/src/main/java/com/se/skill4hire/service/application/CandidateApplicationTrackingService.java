package com.se.skill4hire.service.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.Application.SubmissionSource;
import com.se.skill4hire.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateApplicationTrackingService {

    private final ApplicationRepository applicationRepository;

    public CandidateApplicationTrackingService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<ApplicationDTO> getSelfSubmittedApplications(Long candidateId) {
        return applicationRepository
                .findByCandidateIdAndSubmittedBy(candidateId, SubmissionSource.CANDIDATE)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getEmployerSubmittedApplications(Long candidateId) {
        return applicationRepository
                .findByCandidateIdAndSubmittedBy(candidateId, SubmissionSource.EMPLOYER)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getAllApplicationsForCandidate(Long candidateId) {
        return applicationRepository
                .findByCandidateId(candidateId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ApplicationDTO toDTO(Application app) {
        return new ApplicationDTO(
                app.getId(),
                app.getCandidateId(),
                app.getCompanyId(),
                app.getJobPostId(),
                app.getSubmittedBy(),
                app.getCreatedById(),
                app.getStatus(),
                app.getCreatedAt()
        );
    }
}
