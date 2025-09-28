package com.se.skill4hire.controller.employee;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.dto.application.UpdateApplicationStatusRequest;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.service.application.ApplicationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/employees/applications")
public class EmployeeApplicationStatusController {

    private final ApplicationService applicationService;

    public EmployeeApplicationStatusController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> updateStatus(@PathVariable Long applicationId,
                                          @Valid @RequestBody UpdateApplicationStatusRequest request,
                                          HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (employeeId == null || role == null || !"EMPLOYEE".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only employees can change candidate application status"));
        }

        Application.ApplicationStatus newStatus;
        try {
            newStatus = Application.ApplicationStatus.valueOf(request.getStatus().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid status value: " + request.getStatus()));
        }

        if (newStatus == Application.ApplicationStatus.REJECTED) {
            String reason = request.getReason();
            if (reason == null || reason.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Rejection reason is required when status is REJECTED"));
            }
        }

        ApplicationDTO updated = applicationService.updateStatus(
                applicationId,
                newStatus,
                request.getReason(),
                "EMPLOYEE:" + employeeId);

        return ResponseEntity.ok(updated);
    }
}
