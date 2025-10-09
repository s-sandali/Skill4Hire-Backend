package com.se.skill4hire.service.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.repository.ApplicationRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.service.exception.ApplicationNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository repository;
    private final JobPostRepository jobPostRepository;

    public ApplicationService(ApplicationRepository repository, JobPostRepository jobPostRepository) {
        this.repository = repository;
        this.jobPostRepository = jobPostRepository;
    }

    // Existing: companies view by status
    public List<CompanyView> getCompaniesByStatus(String candidateId, Application.ApplicationStatus status) {
        return repository.findByCandidateIdAndStatus(candidateId, status)
                .stream()
                .map(a -> new CompanyView(a.getCompanyId(), a.getCompanyName(), a.getStatus().name(), a.getAppliedAt()))
                .collect(Collectors.toList());
    }

    // Create a new application (defaults to APPLIED)
    public ApplicationDTO create(String candidateId, String companyId, String companyName) {
        Application a = new Application();
        a.setCandidateId(candidateId);
        a.setCompanyId(companyId);
        a.setCompanyName(companyName);
        a.setStatus(Application.ApplicationStatus.APPLIED);
        a.setAppliedAt(LocalDateTime.now());
        Application saved = repository.save(a);
        return toDTO(saved);
    }

    public List<ApplicationDTO> list(String candidateId, Application.ApplicationStatus status) {
        List<Application> apps = (status == null)
                ? repository.findByCandidateId(candidateId)
                : repository.findByCandidateIdAndStatus(candidateId, status);
        return apps.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Summary summary(String candidateId) {
        long applied = repository.findByCandidateIdAndStatus(candidateId, Application.ApplicationStatus.APPLIED).size();
        long shortlisted = repository.findByCandidateIdAndStatus(candidateId, Application.ApplicationStatus.SHORTLISTED).size();
        long rejected = repository.findByCandidateIdAndStatus(candidateId, Application.ApplicationStatus.REJECTED).size();
        return new Summary(applied, shortlisted, rejected);
    }

    // Update status for an application
    public ApplicationDTO updateStatus(String applicationId, Application.ApplicationStatus status, String reason, String decidedBy) {
        Application a = repository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
        if (!Objects.equals(a.getStatus(), status)) {
            a.setStatus(status);
            a.setDecisionAt(LocalDateTime.now());
            a.setDecidedBy(decidedBy);
            if (status == Application.ApplicationStatus.REJECTED) {
                a.setRejectionReason(reason);
            }
        }
        return toDTO(repository.save(a));
    }

    private ApplicationDTO toDTO(Application a) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(a.getId());
        dto.setCandidateId(a.getCandidateId());
        dto.setCompanyId(a.getCompanyId());
        dto.setCompanyName(a.getCompanyName());
        dto.setStatus(a.getStatus() != null ? a.getStatus().name() : null);
        dto.setAppliedAt(a.getAppliedAt());
        
        // Job details: load by jobPostId if available
        if (a.getJobPostId() != null) {
            dto.setJobPostId(a.getJobPostId());
            jobPostRepository.findById(a.getJobPostId()).ifPresent(jp -> {
                dto.setJobTitle(jp.getTitle());
                dto.setJobDescription(jp.getDescription());
                dto.setJobType(jp.getType());
                dto.setJobLocation(jp.getLocation());
                dto.setSalary(jp.getSalary());
                dto.setExperienceRequired(jp.getExperience());
                dto.setJobDeadline(jp.getDeadline() != null ? jp.getDeadline().atStartOfDay() : null);
            });
        }
        
        return dto;
    }

    public record CompanyView(String companyId, String companyName, String status, java.time.LocalDateTime appliedAt) {}
    public record Summary(long applied, long shortlisted, long rejected) {}
}
