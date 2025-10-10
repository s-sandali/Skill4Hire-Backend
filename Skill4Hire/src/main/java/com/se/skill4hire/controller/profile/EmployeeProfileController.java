package com.se.skill4hire.controller.profile;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.dto.application.ApplicationStatusUpdateRequest;
import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.service.application.ApplicationService;
import com.se.skill4hire.service.profile.EmployeeProfileService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

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
        String fileName = employeeProfileService.uploadProfilePicture(employeeId, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture uploaded successfully");
        response.put("fileName", fileName);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/applications/{applicationId}/status")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
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
}
