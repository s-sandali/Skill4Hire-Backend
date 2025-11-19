package com.se.skill4hire.service.notification;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.se.skill4hire.entity.CompanyNotification;
import com.se.skill4hire.entity.auth.Candidate;
import com.se.skill4hire.entity.auth.Employee;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.CompanyNotificationRepository;
import com.se.skill4hire.repository.auth.CandidateAuthRepository;
import com.se.skill4hire.repository.auth.EmployeeRepository;

@Service
public class CompanyNotificationService {
    public static final String TYPE_NEW_APPLICATION = "NEW_APPLICATION";
    public static final String TYPE_CANDIDATE_RECOMMENDED = "CANDIDATE_RECOMMENDED";

    private static final Logger log = LoggerFactory.getLogger(CompanyNotificationService.class);

    private final CompanyNotificationRepository companyNotificationRepository;
    private final CandidateAuthRepository candidateAuthRepository;
    private final EmployeeRepository employeeRepository;

    public CompanyNotificationService(CompanyNotificationRepository companyNotificationRepository,
                                      CandidateAuthRepository candidateAuthRepository,
                                      EmployeeRepository employeeRepository) {
        this.companyNotificationRepository = companyNotificationRepository;
        this.candidateAuthRepository = candidateAuthRepository;
        this.employeeRepository = employeeRepository;
    }

    public void notifyNewApplication(JobPost jobPost, String candidateId, String applicationId) {
        if (jobPost == null || jobPost.getCompanyId() == null) {
            log.debug("Skipping company notification for application; missing job or company");
            return;
        }
        CompanyNotification notification = new CompanyNotification();
        notification.setCompanyId(jobPost.getCompanyId());
        notification.setType(TYPE_NEW_APPLICATION);
        notification.setCandidateId(candidateId);
        notification.setJobPostId(jobPost.getId());
        notification.setReferenceId(applicationId);
        notification.setMessage(buildApplicationMessage(candidateId, jobPost.getTitle()));
        companyNotificationRepository.save(notification);
        log.info("Company notification created for new application. companyId={}, jobId={}, candidateId={}",
                jobPost.getCompanyId(), jobPost.getId(), candidateId);
    }

    public void notifyDirectApplication(String companyId, String candidateId, String companyName, String applicationId) {
        if (companyId == null) {
            log.debug("Skipping direct application notification; missing companyId");
            return;
        }
        CompanyNotification notification = new CompanyNotification();
        notification.setCompanyId(companyId);
        notification.setType(TYPE_NEW_APPLICATION);
        notification.setCandidateId(candidateId);
        notification.setReferenceId(applicationId);
        notification.setMessage(buildApplicationMessage(candidateId, companyName));
        companyNotificationRepository.save(notification);
        log.info("Company notification created for direct application. companyId={}, candidateId={}", companyId, candidateId);
    }

    public void notifyCandidateRecommended(JobPost jobPost,
                                           String candidateId,
                                           String candidateName,
                                           String employeeId,
                                           String recommendationId) {
        if (jobPost == null || jobPost.getCompanyId() == null) {
            log.debug("Skipping company recommendation notification; missing job or company");
            return;
        }
        CompanyNotification notification = new CompanyNotification();
        notification.setCompanyId(jobPost.getCompanyId());
        notification.setType(TYPE_CANDIDATE_RECOMMENDED);
        notification.setCandidateId(candidateId);
        notification.setJobPostId(jobPost.getId());
        notification.setReferenceId(recommendationId);
        notification.setEmployeeId(employeeId);
        notification.setMessage(buildRecommendationMessage(candidateId, candidateName, employeeId, jobPost.getTitle()));
        companyNotificationRepository.save(notification);
        log.info("Company notification created for recommendation. companyId={}, jobId={}, candidateId={}, employeeId={}",
                jobPost.getCompanyId(), jobPost.getId(), candidateId, employeeId);
    }

    public List<CompanyNotification> listForCompany(String companyId) {
        if (companyId == null) {
            return Collections.emptyList();
        }
        return companyNotificationRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);
    }

    public List<CompanyNotification> getUnread(String companyId) {
        if (companyId == null) {
            return Collections.emptyList();
        }
        return companyNotificationRepository.findByCompanyIdAndReadFalseOrderByCreatedAtDesc(companyId);
    }

    public long getUnreadCount(String companyId) {
        if (companyId == null) {
            return 0;
        }
        return companyNotificationRepository.countByCompanyIdAndReadFalse(companyId);
    }

    public void markAsRead(String notificationId, String companyId) {
        companyNotificationRepository.findById(notificationId).ifPresent(notification -> {
            if (!notification.getCompanyId().equals(companyId)) {
                throw new RuntimeException("Access denied");
            }
            if (!notification.isRead()) {
                notification.setRead(true);
                companyNotificationRepository.save(notification);
            }
        });
    }

    public void markAllAsRead(String companyId) {
        List<CompanyNotification> unread = getUnread(companyId);
        if (unread.isEmpty()) {
            return;
        }
        unread.forEach(notification -> notification.setRead(true));
        companyNotificationRepository.saveAll(unread);
    }

    public void delete(String notificationId, String companyId) {
        companyNotificationRepository.findById(notificationId).ifPresent(notification -> {
            if (!notification.getCompanyId().equals(companyId)) {
                throw new RuntimeException("Access denied");
            }
            companyNotificationRepository.delete(notification);
        });
    }

    private String buildApplicationMessage(String candidateId, String context) {
        String candidateName = candidateAuthRepository.findById(candidateId)
                .map(Candidate::getName)
                .filter(name -> name != null && !name.isBlank())
                .orElse(candidateId);
        String target = (context != null && !context.isBlank()) ? context : "your company";
        return String.format("%s applied for %s.", candidateName, target);
    }

    private String buildRecommendationMessage(String candidateId,
                                              String candidateName,
                                              String employeeId,
                                              String jobTitle) {
        String resolvedCandidateName = (candidateName != null && !candidateName.isBlank())
                ? candidateName
                : candidateAuthRepository.findById(candidateId)
                    .map(Candidate::getName)
                    .filter(name -> name != null && !name.isBlank())
                    .orElse(candidateId);

        String recommender = employeeRepository.findById(employeeId)
                .map(Employee::getName)
                .filter(name -> name != null && !name.isBlank())
                .orElse("A Skill4Hire recruiter");

        String role = (jobTitle != null && !jobTitle.isBlank()) ? jobTitle : "one of your roles";
        return String.format("%s recommended %s for %s.", recommender, resolvedCandidateName, role);
    }
}
