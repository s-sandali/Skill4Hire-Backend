package com.se.skill4hire.service.notification;

import com.se.skill4hire.entity.EmployeeNotification;
import com.se.skill4hire.entity.auth.Candidate;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.EmployeeNotificationRepository;
import com.se.skill4hire.repository.auth.CandidateAuthRepository;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import com.se.skill4hire.repository.auth.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeNotificationService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeNotificationService.class);

    private final EmployeeNotificationRepository employeeNotificationRepository;
    private final CandidateAuthRepository candidateAuthRepository;
    private final CompanyAuthRepository companyAuthRepository;
    private final JobPostRepository jobPostRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeNotificationService(EmployeeNotificationRepository employeeNotificationRepository,
                                       CandidateAuthRepository candidateAuthRepository,
                                       CompanyAuthRepository companyAuthRepository,
                                       JobPostRepository jobPostRepository,
                                       EmployeeRepository employeeRepository) {
        this.employeeNotificationRepository = employeeNotificationRepository;
        this.candidateAuthRepository = candidateAuthRepository;
        this.companyAuthRepository = companyAuthRepository;
        this.jobPostRepository = jobPostRepository;
        this.employeeRepository = employeeRepository;
    }

    // Notify when candidate creates/updates profile - JUST PASS CANDIDATE ID
    public void notifyCandidateProfileCreated(String candidateId) {
        try {
            Candidate candidate = candidateAuthRepository.findById(candidateId).orElse(null);
            if (candidate != null) {
                String message = String.format("Candidate %s has created/updated their profile", candidate.getName());
                createNotificationForAllEmployees(message, "CANDIDATE_PROFILE_CREATED");
                log.info("Employee notification created for candidate profile: {}", candidate.getName());
            }
        } catch (Exception e) {
            log.error("Failed to create candidate profile notification for candidateId: {}", candidateId, e);
        }
    }

    // Notify when company posts job - JUST PASS JOB POST ID AND COMPANY ID
    public void notifyJobPostCreated(String jobPostId, String companyId) {
        try {
            Company company = companyAuthRepository.findById(companyId).orElse(null);
            JobPost jobPost = jobPostRepository.findById(jobPostId).orElse(null);

            if (company != null && jobPost != null) {
                String message = String.format("Company %s posted a new job: %s", company.getName(), jobPost.getTitle());
                createNotificationForAllEmployees(message, "JOB_POST_CREATED");
                log.info("Employee notification created for job post: {} by {}", jobPost.getTitle(), company.getName());
            }
        } catch (Exception e) {
            log.error("Failed to create job post notification for jobPostId: {}", jobPostId, e);
        }
    }

    // Notify when new application is submitted - JUST PASS CANDIDATE ID AND JOB POST ID
    public void notifyNewApplication(String candidateId, String jobPostId) {
        try {
            Candidate candidate = candidateAuthRepository.findById(candidateId).orElse(null);
            JobPost jobPost = jobPostRepository.findById(jobPostId).orElse(null);

            if (candidate != null && jobPost != null) {
                String message = String.format("Candidate %s applied for job: %s", candidate.getName(), jobPost.getTitle());
                createNotificationForAllEmployees(message, "NEW_APPLICATION");
                log.info("Employee notification created for new application: {} for job {}", candidate.getName(), jobPost.getTitle());
            } else if (jobPost != null) {
                // Fallback if candidate not found
                String message = String.format("Candidate %s applied for job: %s", candidateId, jobPost.getTitle());
                createNotificationForAllEmployees(message, "NEW_APPLICATION");
                log.info("Employee notification created for new application: {} for job {}", candidateId, jobPost.getTitle());
            }
        } catch (Exception e) {
            log.error("Failed to create new application notification for candidateId: {}, jobPostId: {}", candidateId, jobPostId, e);
        }
    }

    // Create notifications for all employees
    private void createNotificationForAllEmployees(String message, String type) {
        try {
            // Get all employees from the repository
            List<com.se.skill4hire.entity.auth.Employee> employees = employeeRepository.findAll();

            for (com.se.skill4hire.entity.auth.Employee employee : employees) {
                EmployeeNotification notification = new EmployeeNotification();
                notification.setEmployeeId(employee.getId());
                notification.setMessage(message);
                notification.setType(type);
                employeeNotificationRepository.save(notification);
            }

            log.debug("Created {} notifications for {} employees", type, employees.size());
        } catch (Exception e) {
            log.error("Failed to create notifications for all employees", e);
        }
    }

    // Get all notifications for an employee
    public List<EmployeeNotification> listForEmployee(String employeeId) {
        return employeeNotificationRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    // Get unread notifications for an employee
    public List<EmployeeNotification> getUnreadNotifications(String employeeId) {
        return employeeNotificationRepository.findByEmployeeIdAndReadFalseOrderByCreatedAtDesc(employeeId);
    }

    // Get count of unread notifications
    public long getUnreadCount(String employeeId) {
        return employeeNotificationRepository.countByEmployeeIdAndReadFalse(employeeId);
    }

    // Mark notification as read
    public void markAsRead(String notificationId, String employeeId) {
        EmployeeNotification notification = employeeNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("Access denied");
        }

        notification.setRead(true);
        employeeNotificationRepository.save(notification);
        log.debug("Marked notification {} as read for employee {}", notificationId, employeeId);
    }

    // Mark all notifications as read for an employee
    public void markAllAsRead(String employeeId) {
        List<EmployeeNotification> unreadNotifications = getUnreadNotifications(employeeId);
        for (EmployeeNotification notification : unreadNotifications) {
            notification.setRead(true);
        }
        employeeNotificationRepository.saveAll(unreadNotifications);
        log.debug("Marked all notifications as read for employee {}", employeeId);
    }

    // Delete a notification
    public void deleteNotification(String notificationId, String employeeId) {
        EmployeeNotification notification = employeeNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("Access denied");
        }

        employeeNotificationRepository.delete(notification);
        log.debug("Deleted notification {} for employee {}", notificationId, employeeId);
    }
}