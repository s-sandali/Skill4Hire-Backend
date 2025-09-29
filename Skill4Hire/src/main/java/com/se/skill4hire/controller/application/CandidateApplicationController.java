package com.se.skill4hire.controller.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.service.application.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/candidates/applications")
public class CandidateApplicationController {

    private final ApplicationService applicationService;

    public CandidateApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<ApplicationService.Summary> getApplicationSummary(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStatus(@PathVariable String status,
                                                                        HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        Application.ApplicationStatus appStatus = parseStatus(status);
        return ResponseEntity.ok(applicationService.list(candidateId, appStatus));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStatusQuery(@RequestParam("status") String status,
                                                                             HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        Application.ApplicationStatus appStatus = parseStatus(status);
        return ResponseEntity.ok(applicationService.list(candidateId, appStatus));
    }

    private Application.ApplicationStatus parseStatus(String status) {
        try {
            return Application.ApplicationStatus.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status value: " + status, ex);
        }
    }
}
