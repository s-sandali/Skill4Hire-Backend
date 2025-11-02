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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeNotificationService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeNotificationService.class);

    // Categories for frontend grouping
    public static final String CAT_REGISTRATIONS = "REGISTRATIONS";
    public static final String CAT_JOB_POSTS = "JOB_POSTS";
    public static final String CAT_APPLICATION_UPDATES = "APPLICATION_UPDATES";

    // Types
    public static final String TYPE_CANDIDATE_REGISTERED = "CANDIDATE_REGISTERED";
    public static final String TYPE_COMPANY_REGISTERED = "COMPANY_REGISTERED";
    public static final String TYPE_CANDIDATE_PROFILE_CREATED = "CANDIDATE_PROFILE_CREATED";
    public static final String TYPE_JOB_POST_CREATED = "JOB_POST_CREATED";
    public static final String TYPE_APPLICATION_APPLIED = "APPLICATION_APPLIED";
    public static final String TYPE_APPLICATION_SHORTLISTED = "APPLICATION_SHORTLISTED";
    public static final String TYPE_APPLICATION_REJECTED = "APPLICATION_REJECTED";
    public static final String TYPE_APPLICATION_INTERVIEW = "APPLICATION_INTERVIEW";
    public static final String TYPE_APPLICATION_HIRED = "APPLICATION_HIRED";

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

    private void createNotificationForAllEmployees(String title, String message, String type, String category) {
        try {
            List<com.se.skill4hire.entity.auth.Employee> employees = employeeRepository.findAll();
            for (com.se.skill4hire.entity.auth.Employee employee : employees) {
                EmployeeNotification n = new EmployeeNotification();
                n.setEmployeeId(employee.getId());
                n.setTitle(title);
                n.setMessage(message);
                n.setType(type);
                n.setCategory(category);
                employeeNotificationRepository.save(n);
            }
            log.debug("Created {} notifications for {} employees", type, employees.size());
        } catch (Exception e) {
            log.error("Failed to create notifications for all employees", e);
        }
    }

    // Helper to create a manual/sample notification for a single employee (useful for quick verification)
    public EmployeeNotification createSampleForEmployee(String employeeId) {
        EmployeeNotification n = new EmployeeNotification();
        n.setEmployeeId(employeeId);
        n.setTitle("Welcome");
        n.setMessage("This is a sample notification for verification.");
        n.setType(TYPE_CANDIDATE_REGISTERED);
        n.setCategory(CAT_REGISTRATIONS);
        EmployeeNotification saved = employeeNotificationRepository.save(n);
        log.info("Seeded sample notification {} for employee {}", saved.getId(), employeeId);
        return saved;
    }

    // Notify when candidate creates/updates profile - JUST PASS CANDIDATE ID
    public void notifyCandidateProfileCreated(String candidateId) {
        try {
            Candidate candidate = candidateAuthRepository.findById(candidateId).orElse(null);
            if (candidate != null) {
                String title = "Candidate Profile";
                String message = String.format("%s created/updated their profile", candidate.getName());
                createNotificationForAllEmployees(title, message, TYPE_CANDIDATE_PROFILE_CREATED, CAT_REGISTRATIONS);
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
                String title = "New Job Post";
                String message = String.format("%s posted a new job: %s", company.getName(), jobPost.getTitle());
                createNotificationForAllEmployees(title, message, TYPE_JOB_POST_CREATED, CAT_JOB_POSTS);
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

            if (jobPost == null) {
                log.warn("notifyNewApplication: job post not found for {}", jobPostId);
                return;
            }
            String candidateName = candidate != null ? candidate.getName() : candidateId;
            String title = "New Application";
            String message = String.format("%s applied for %s", candidateName, jobPost.getTitle());
            createNotificationForAllEmployees(title, message, TYPE_APPLICATION_APPLIED, CAT_APPLICATION_UPDATES);
        } catch (Exception e) {
            log.error("Failed to create new application notification for candidateId: {}, jobPostId: {}", candidateId, jobPostId, e);
        }
    }

    // Notify when application status changes (shortlisted, interview, rejected, hired)
    public void notifyApplicationStatusChanged(String candidateId, String jobPostId, com.se.skill4hire.entity.Application.ApplicationStatus status) {
        try {
            Candidate candidate = candidateAuthRepository.findById(candidateId).orElse(null);
            JobPost jobPost = jobPostRepository.findById(jobPostId).orElse(null);
            String candidateName = candidate != null ? candidate.getName() : candidateId;
            String jobTitle = jobPost != null ? jobPost.getTitle() : "the job";
            String type;
            String title;
            String message;
            switch (status) {
                case SHORTLISTED -> { type = TYPE_APPLICATION_SHORTLISTED; title = "Application Shortlisted"; message = String.format("%s shortlisted for %s", candidateName, jobTitle); }
                case INTERVIEW -> { type = TYPE_APPLICATION_INTERVIEW; title = "Interview Scheduled"; message = String.format("Interview scheduled: %s for %s", candidateName, jobTitle); }
                case HIRED -> { type = TYPE_APPLICATION_HIRED; title = "Candidate Hired"; message = String.format("%s hired for %s", candidateName, jobTitle); }
                case REJECTED -> { type = TYPE_APPLICATION_REJECTED; title = "Application Rejected"; message = String.format("%s rejected for %s", candidateName, jobTitle); }
                default -> { type = TYPE_APPLICATION_APPLIED; title = "Application Updated"; message = String.format("%s status updated for %s", candidateName, jobTitle); }
            }
            createNotificationForAllEmployees(title, message, type, CAT_APPLICATION_UPDATES);
        } catch (Exception e) {
            log.error("Failed to create application status change notification", e);
        }
    }

    // Notify when a new candidate registers
    public void notifyCandidateRegistered(String candidateId) {
        try {
            Candidate candidate = candidateAuthRepository.findById(candidateId).orElse(null);
            String name = candidate != null ? candidate.getName() : candidateId;
            createNotificationForAllEmployees("New Candidate", String.format("%s joined Skill4Hire", name), TYPE_CANDIDATE_REGISTERED, CAT_REGISTRATIONS);
        } catch (Exception e) {
            log.error("Failed to create candidate registered notification", e);
        }
    }

    // Notify when a new company registers
    public void notifyCompanyRegistered(String companyId) {
        try {
            Company company = companyAuthRepository.findById(companyId).orElse(null);
            String name = company != null ? company.getName() : companyId;
            createNotificationForAllEmployees("New Company", String.format("%s created a company account", name), TYPE_COMPANY_REGISTERED, CAT_REGISTRATIONS);
        } catch (Exception e) {
            log.error("Failed to create company registered notification", e);
        }
    }

    // Queries
    public List<EmployeeNotification> listForEmployee(String employeeId) {
        return employeeNotificationRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    public Page<EmployeeNotification> listForEmployeePaged(String employeeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return employeeNotificationRepository.findByEmployeeId(employeeId, pageable);
    }

    public List<EmployeeNotification> getUnreadNotifications(String employeeId) {
        return employeeNotificationRepository.findByEmployeeIdAndReadFalseOrderByCreatedAtDesc(employeeId);
    }

    public long getUnreadCount(String employeeId) { return employeeNotificationRepository.countByEmployeeIdAndReadFalse(employeeId); }

    public void markAsRead(String notificationId, String employeeId) {
        EmployeeNotification notification = employeeNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!notification.getEmployeeId().equals(employeeId)) throw new RuntimeException("Access denied");
        notification.setRead(true);
        employeeNotificationRepository.save(notification);
        log.debug("Marked notification {} as read for employee {}", notificationId, employeeId);
    }

    public void markAllAsRead(String employeeId) {
        List<EmployeeNotification> unreadNotifications = getUnreadNotifications(employeeId);
        for (EmployeeNotification notification : unreadNotifications) notification.setRead(true);
        if (!unreadNotifications.isEmpty()) employeeNotificationRepository.saveAll(unreadNotifications);
        log.debug("Marked all notifications as read for employee {}", employeeId);
    }

    public void deleteNotification(String notificationId, String employeeId) {
        EmployeeNotification notification = employeeNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!notification.getEmployeeId().equals(employeeId)) throw new RuntimeException("Access denied");
        employeeNotificationRepository.delete(notification);
        log.debug("Deleted notification {} for employee {}", notificationId, employeeId);
    }
}
