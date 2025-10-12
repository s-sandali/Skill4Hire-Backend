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

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyCandidateRecommended(String candidateId, String companyName, JobPost job) {
        String message = String.format("You've been recommended for %s at %s.",
                job != null && job.getTitle() != null ? job.getTitle() : "a job",
                companyName != null ? companyName : (job != null ? job.getCompanyId() : "a company")
        );
        AppNotification n = new AppNotification();
        n.setUserId(candidateId);
        n.setType("RECOMMENDATION");
        n.setMessage(message);
        notificationRepository.save(n);
        log.info("Notification created for candidate {}: {}", candidateId, message);
    }

    public List<AppNotification> listForUser(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

