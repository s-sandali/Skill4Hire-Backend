package com.se.skill4hire.controller.profile;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.dto.application.ApplicationStatusUpdateRequest;
import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.service.application.ApplicationService;
import com.se.skill4hire.service.profile.EmployeeProfileService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;
    private final ApplicationService applicationService;

    public EmployeeProfileController(EmployeeProfileService employeeProfileService,
                                     ApplicationService applicationService) {
        this.employeeProfileService = employeeProfileService;
        this.applicationService = applicationService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<EmployeeProfileDTO> getProfile(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        EmployeeProfileDTO profile = employeeProfileService.getProfile(employeeId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<EmployeeProfileDTO> updateProfile(
            @Valid @RequestBody EmployeeProfileDTO profileDTO,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        EmployeeProfileDTO updatedProfile = employeeProfileService.updateProfile(employeeId, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/profile/completeness")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<ProfileCompletenessDTO> getProfileCompleteness(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        ProfileCompletenessDTO completeness = employeeProfileService.getProfileCompleteness(employeeId);
        return ResponseEntity.ok(completeness);
    }

    @PostMapping("/upload/profile-picture")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        String publicPath = employeeProfileService.uploadProfilePicture(employeeId, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture uploaded successfully");
        response.put("profilePicturePath", publicPath);
        response.put("profilePictureUrl", publicPath);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/applications/{applicationId}/status")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<ApplicationDTO> updateApplicationStatus(@PathVariable String applicationId,
                                                                  @Valid @RequestBody ApplicationStatusUpdateRequest request,
                                                                  HttpSession session) {
        Application.ApplicationStatus status = parseStatus(request.getStatus());
        if (status == Application.ApplicationStatus.REJECTED && (request.getReason() == null || request.getReason().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rejection reason is required when rejecting an application");
        }

        String employeeId = (String) session.getAttribute("userId");
        String decidedBy = employeeId != null ? "EMPLOYEE_" + employeeId : "EMPLOYEE";

        ApplicationDTO updated = applicationService.updateStatus(applicationId, status, request.getReason(), decidedBy);
        return ResponseEntity.ok(updated);
    }

    private Application.ApplicationStatus parseStatus(String status) {
        try {
            return Application.ApplicationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value: " + status, ex);
        }
    }

    // ======================== Applications (Employee creates on behalf of candidate) ========================

    @PostMapping("/applications")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<ApplicationDTO> submitCandidateToJob(@RequestBody CreateApplicationRequest request) {
        if (request == null || request.candidateId == null || request.candidateId.isBlank()
                || request.jobPostId == null || request.jobPostId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "candidateId and jobPostId are required");
        }
        try {
            ApplicationDTO dto = applicationService.createForJob(request.candidateId, request.jobPostId);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (com.se.skill4hire.service.exception.JobNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
        }
    }

    @GetMapping("/metrics")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Map<String, Object> metrics = new HashMap<>();
        try {
            long totalCandidates = employeeProfileService.getTotalCandidatesCount();
            long activeJobs = employeeProfileService.getActiveJobsCount();
            long upcomingInterviews = employeeProfileService.getUpcomingInterviewsCount(employeeId);
            long newApplications = employeeProfileService.getNewApplicationsCount();

            metrics.put("totalCandidates", totalCandidates);
            metrics.put("activeJobs", activeJobs);
            metrics.put("upcomingInterviews", upcomingInterviews);
            metrics.put("newApplications", newApplications);
        } catch (Exception e) {
            metrics.put("totalCandidates", 0);
            metrics.put("activeJobs", 0);
            metrics.put("upcomingInterviews", 0);
            metrics.put("newApplications", 0);
        }
        return ResponseEntity.ok(metrics);
    }

    // Inline request payload to avoid extra small files
    public static class CreateApplicationRequest {
        public String candidateId;
        public String jobPostId;
        public String getCandidateId() { return candidateId; }
        public void setCandidateId(String candidateId) { this.candidateId = candidateId; }
        public String getJobPostId() { return jobPostId; }
        public void setJobPostId(String jobPostId) { this.jobPostId = jobPostId; }
    }
}
