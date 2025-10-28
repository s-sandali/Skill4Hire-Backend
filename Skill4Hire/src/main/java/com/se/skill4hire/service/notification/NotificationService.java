package com.se.skill4hire.service.notification;

import com.se.skill4hire.entity.AppNotification;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public static final String TYPE_RECOMMENDATION = "RECOMMENDATION";
    public static final String TYPE_APPLICATION_APPLIED = "APPLICATION_APPLIED";
    public static final String TYPE_APPLICATION_SHORTLISTED = "APPLICATION_SHORTLISTED";
    public static final String TYPE_APPLICATION_INTERVIEW = "APPLICATION_INTERVIEW";
    public static final String TYPE_APPLICATION_REJECTED = "APPLICATION_REJECTED";
    public static final String TYPE_APPLICATION_HIRED = "APPLICATION_HIRED";

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyCandidateRecommended(String candidateId, String companyName, JobPost job) {
        String message = String.format("You've been recommended for %s at %s.",
                job != null && job.getTitle() != null ? job.getTitle() : "a job",
                safe(companyName, job != null ? job.getCompanyId() : "a company")
        );
        save(candidateId, TYPE_RECOMMENDATION, "Recommended", message, null, job != null ? job.getId() : null);
    }

    public void notifyApplicationSubmitted(String candidateId, JobPost job, String companyName) {
        String title = job != null && job.getTitle() != null ? job.getTitle() : "a role";
        String company = safe(companyName, job != null ? job.getCompanyId() : null);
        String message = company != null
                ? String.format("Thanks! Your application for %s at %s was sent.", title, company)
                : String.format("Thanks! Your application for %s was sent.", title);
        save(candidateId, TYPE_APPLICATION_APPLIED, "Application sent", message, null, job != null ? job.getId() : null);
    }

    public void notifyApplicationStatusChanged(String candidateId,
                                               com.se.skill4hire.entity.Application.ApplicationStatus status,
                                               JobPost job,
                                               String companyName,
                                               String reason,
                                               String applicationId) {
        String title = job != null && job.getTitle() != null ? job.getTitle() : "the role";
        String company = safe(companyName, job != null ? job.getCompanyId() : null);
        String atCompany = company != null ? " at " + company : "";
        String message;
        String type;
        String subject;
        switch (status) {
            case SHORTLISTED -> {
                type = TYPE_APPLICATION_SHORTLISTED;
                subject = "Shortlisted";
                message = String.format("Great news! You've been shortlisted for %s%s. We'll be in touch with next steps.", title, atCompany);
            }
            case INTERVIEW -> {
                type = TYPE_APPLICATION_INTERVIEW;
                subject = "Interview invitation";
                message = String.format("Interview invitation: %s%s would like to meet you for %s. Check your email for details.",
                        company != null ? company : "The hiring team",
                        company != null ? "" : "",
                        title);
            }
            case HIRED -> {
                type = TYPE_APPLICATION_HIRED;
                subject = "Offer";
                message = String.format("Congratulations! %s%s has moved forward with your application for %s.",
                        company != null ? company : "The company",
                        company != null ? "" : "",
                        title);
            }
            case REJECTED -> {
                type = TYPE_APPLICATION_REJECTED;
                subject = "Application update";
                String base = String.format("Thank you for your interest in %s%s for %s.",
                        company != null ? company : "the company",
                        company != null ? "" : "",
                        title);
                if (reason != null && !reason.isBlank()) {
                    message = base + " After careful consideration, we won't be moving forward at this time. Reason: " + reason;
                } else {
                    message = base + " After careful consideration, we won't be moving forward at this time. We encourage you to explore other roles that match your profile.";
                }
            }
            default -> {
                // Fallback for APPLIED or unknown statuses
                type = TYPE_APPLICATION_APPLIED;
                subject = "Application update";
                message = String.format("Your application for %s%s has been updated.", title, atCompany);
            }
        }
        save(candidateId, type, subject, message, applicationId, job != null ? job.getId() : null);
    }

    public List<AppNotification> listForUser(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public int getUnreadCount(String userId) {
        if (userId == null || userId.isBlank()) return 0;
        return (int) listForUser(userId).stream().filter(n -> !n.isRead()).count();
    }

    public void markAsRead(String userId, String id) {
        if (userId == null || id == null) return;
        notificationRepository.findById(id).ifPresent(n -> {
            if (!userId.equals(n.getUserId())) return; // ignore others' notes
            if (!n.isRead()) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        });
    }

    public void markAllAsRead(String userId) {
        if (userId == null || userId.isBlank()) return;
        List<AppNotification> notes = listForUser(userId);
        boolean any = false;
        for (AppNotification n : notes) {
            if (!n.isRead()) { n.setRead(true); any = true; }
        }
        if (any) notificationRepository.saveAll(notes);
    }

    private void save(String candidateId, String type, String title, String message, String applicationId, String jobPostId) {
        AppNotification n = new AppNotification();
        n.setUserId(candidateId);
        n.setType(type);
        n.setTitle(title);
        n.setMessage(message);
        if (applicationId != null) n.setApplicationId(applicationId);
        if (jobPostId != null) n.setJobPostId(jobPostId);
        notificationRepository.save(n);
        log.info("Notification created for candidate {}: [{}] {}", candidateId, type, message);
    }

    private String safe(String preferred, String fallback) {
        if (preferred != null && !preferred.isBlank()) return preferred;
        if (fallback != null && !fallback.isBlank()) return fallback;
        return null;
    }
}
