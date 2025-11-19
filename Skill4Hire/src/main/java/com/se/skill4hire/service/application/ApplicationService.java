package com.se.skill4hire.service.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.ApplicationRepository;
import com.se.skill4hire.repository.RecommendationRepository;
import com.se.skill4hire.repository.auth.CandidateAuthRepository;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import com.se.skill4hire.repository.profile.CandidateProfileRepository;
import com.se.skill4hire.service.exception.ApplicationNotFoundException;
import com.se.skill4hire.service.exception.JobNotFoundException;
import com.se.skill4hire.service.notification.CompanyNotificationService;
import com.se.skill4hire.service.notification.NotificationService;
import com.se.skill4hire.service.notification.EmployeeNotificationService;

@Service
public class ApplicationService {
    private final ApplicationRepository repository;
    private final JobPostRepository jobPostRepository;
    private final CompanyAuthRepository companyAuthRepository;
    private final RecommendationRepository recommendationRepository;
    private final CompanyNotificationService companyNotificationService;
    private final NotificationService candidateNotificationService;
    private final CandidateAuthRepository candidateAuthRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final EmployeeNotificationService employeeNotificationService;

    public ApplicationService(ApplicationRepository repository,
                              JobPostRepository jobPostRepository,
                              CompanyAuthRepository companyAuthRepository,
                              RecommendationRepository recommendationRepository,
                              CompanyNotificationService companyNotificationService,
                              NotificationService candidateNotificationService,
                              CandidateAuthRepository candidateAuthRepository,
                              CandidateProfileRepository candidateProfileRepository,
                              EmployeeNotificationService employeeNotificationService) {
        this.repository = repository;
        this.jobPostRepository = jobPostRepository;
        this.companyAuthRepository = companyAuthRepository;
        this.recommendationRepository = recommendationRepository;
        this.companyNotificationService = companyNotificationService;
        this.candidateNotificationService = candidateNotificationService;
        this.candidateAuthRepository = candidateAuthRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.employeeNotificationService = employeeNotificationService;
    }

    // Existing: companies view by status
    public List<CompanyView> getCompaniesByStatus(String candidateId, Application.ApplicationStatus status) {
        return repository.findByCandidateIdAndStatus(candidateId, status)
                .stream()
                .map(a -> new CompanyView(a.getCompanyId(), a.getCompanyName(), a.getStatus().name(), a.getAppliedAt()))
                .toList();
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
        // Notify company
        companyNotificationService.notifyDirectApplication(a.getCompanyId(), a.getCandidateId(), a.getCompanyName(), saved.getId());
        // Notify candidate
        candidateNotificationService.notifyApplicationSubmitted(candidateId, null, companyName);
        // Notify employees (no jobId in this path)
        try { employeeNotificationService.notifyNewApplication(candidateId, a.getJobPostId()); } catch (Exception ignored) {}
        return toDTO(saved);
    }

    // Create application for a specific job post (by EMPLOYEE or candidate via job)
    public ApplicationDTO createForJob(String candidateId, String jobPostId) {
        JobPost job = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new JobNotFoundException(jobPostId));

        repository.findByCandidateIdAndJobPostId(candidateId, jobPostId)
                .ifPresent(existing -> { throw new IllegalStateException("Application already exists for this candidate and job"); });

        Application a = new Application();
        a.setCandidateId(candidateId);
        a.setCompanyId(job.getCompanyId());
        if (job.getCompanyId() != null) {
            companyAuthRepository.findById(job.getCompanyId()).map(Company::getName).ifPresent(a::setCompanyName);
        }
        a.setJobPostId(jobPostId);
        a.setStatus(Application.ApplicationStatus.APPLIED);
        a.setAppliedAt(LocalDateTime.now());

        Application saved = repository.save(a);
        // Notify company
        companyNotificationService.notifyNewApplication(job, candidateId, saved.getId());
        // Notify candidate
        candidateNotificationService.notifyApplicationSubmitted(candidateId, job, a.getCompanyName());
        // Notify employees
        try { employeeNotificationService.notifyNewApplication(candidateId, jobPostId); } catch (Exception ignored) {}
        return toDTO(saved);
    }

    public List<ApplicationDTO> list(String candidateId, Application.ApplicationStatus status) {
        List<Application> apps = (status == null)
            ? repository.findByCandidateId(candidateId)
            : repository.findByCandidateIdAndStatus(candidateId, status);
        return apps.stream().map(this::toDTO).toList();
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
            // Notify candidate with friendly message
            JobPost job = null;
            if (a.getJobPostId() != null) {
                job = jobPostRepository.findById(a.getJobPostId()).orElse(null);
            }
            candidateNotificationService.notifyApplicationStatusChanged(
                    a.getCandidateId(), status, job, a.getCompanyName(), reason, a.getId()
            );
            // Notify employees about the application status change
            try { employeeNotificationService.notifyApplicationStatusChanged(a.getCandidateId(), a.getJobPostId(), status); } catch (Exception ignored) {}
        }
        return toDTO(repository.save(a));
    }

    // Company views: list applications by company with optional status
    public java.util.List<ApplicationDTO> listByCompany(String companyId, Application.ApplicationStatus status) {
        java.util.List<Application> apps = (status == null)
                ? repository.findByCompanyId(companyId)
                : repository.findByCompanyIdAndStatus(companyId, status);
        return apps.stream().map(this::toDTO).toList();
    }

    // Company views: list applications by job with optional status
    public java.util.List<ApplicationDTO> listByJob(String jobPostId, Application.ApplicationStatus status) {
        java.util.List<Application> apps = (status == null)
                ? repository.findByJobPostId(jobPostId)
                : repository.findByJobPostIdAndStatus(jobPostId, status);
        return apps.stream().map(this::toDTO).toList();
    }

    private ApplicationDTO toDTO(Application a) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(a.getId());
        dto.setCandidateId(a.getCandidateId());
        enrichCandidateDetails(dto, a.getCandidateId());
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
            // Tag as recommended if any employee recommended this candidate for this job
            boolean recommended = recommendationRepository.existsByCandidateIdAndJobId(a.getCandidateId(), a.getJobPostId());
            dto.setRecommendedBySkill4Hire(recommended);
        }

        return dto;
    }

    private void enrichCandidateDetails(ApplicationDTO dto, String candidateId) {
        if (candidateId == null || candidateId.isBlank()) {
            return;
        }

        candidateAuthRepository.findById(candidateId).ifPresent(candidate -> {
            dto.setCandidateName(candidate.getName());
            dto.setCandidateEmail(candidate.getEmail());
        });

        candidateProfileRepository.findByUserId(candidateId).ifPresent(profile -> {
            if ((dto.getCandidateName() == null || dto.getCandidateName().isBlank()) && profile.getName() != null) {
                dto.setCandidateName(profile.getName());
            }
            if ((dto.getCandidateEmail() == null || dto.getCandidateEmail().isBlank()) && profile.getEmail() != null) {
                dto.setCandidateEmail(profile.getEmail());
            }
            dto.setCandidateTitle(profile.getTitle());
            dto.setCandidateProfilePicturePath(profile.getProfilePicturePath());
            dto.setCandidateProfilePictureUrl(normalizeProfilePictureUrl(profile.getProfilePicturePath()));
        });
    }

    private String normalizeProfilePictureUrl(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String sanitized = raw.trim().replace("\\", "/");
        if (sanitized.startsWith("http://") || sanitized.startsWith("https://")) {
            return sanitized;
        }
        if (sanitized.startsWith("/uploads/")) {
            return sanitized;
        }
        if (sanitized.startsWith("uploads/")) {
            return "/" + sanitized;
        }
        return "/uploads/profile-pictures/" + sanitized.replaceFirst("^/+", "");
    }

    public record CompanyView(String companyId, String companyName, String status, java.time.LocalDateTime appliedAt) {}
    public record Summary(long applied, long shortlisted, long rejected) {}
}

