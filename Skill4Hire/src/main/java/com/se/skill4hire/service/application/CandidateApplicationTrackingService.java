package com.se.skill4hire.service.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.entity.Application;
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


    public List<ApplicationDTO> getAllApplicationsForCandidate(Long candidateId) {
        return applicationRepository
                .findByCandidateId(candidateId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ApplicationDTO toDTO(Application app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setCandidateId(app.getCandidateId());
        dto.setCompanyId(app.getCompanyId());
        dto.setCompanyName(app.getCompanyName());
        dto.setStatus(app.getStatus() != null ? app.getStatus().name() : null);
        dto.setAppliedAt(app.getAppliedAt());
        
        if (app.getJobPost() != null) {
            dto.setJobPostId(app.getJobPost().getId());
            dto.setJobTitle(app.getJobPost().getTitle());
            dto.setJobDescription(app.getJobPost().getDescription());
            dto.setJobType(app.getJobPost().getType());
            dto.setJobLocation(app.getJobPost().getLocation());
            dto.setSalary(app.getJobPost().getSalary());
            dto.setExperienceRequired(app.getJobPost().getExperience());
            dto.setJobDeadline(app.getJobPost().getDeadline() != null ? 
                app.getJobPost().getDeadline().atStartOfDay() : null);
        }
        
        return dto;
    }
}
