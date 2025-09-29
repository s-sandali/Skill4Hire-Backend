package com.se.skill4hire.controller;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.service.application.ApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simple/candidates")
public class SimpleCandidateController {

    private final ApplicationService applicationService;

    public SimpleCandidateController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Get all applications for a candidate with job details and statuses
     */
    @GetMapping(value = "/{candidateId}/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationDTO>> getCandidateApplications(@PathVariable Long candidateId) {
        List<ApplicationDTO> applications = applicationService.list(candidateId, null);
        return ResponseEntity.ok(applications);
    }

    /**
     * Get application summary for a candidate
     */
    @GetMapping(value = "/{candidateId}/applications/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationService.Summary> getApplicationSummary(@PathVariable Long candidateId) {
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }

    /**
     * Get applications by status
     */
    @GetMapping(value = "/{candidateId}/applications/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStatus(@PathVariable Long candidateId, 
                                                                        @PathVariable String status) {
        try {
            com.se.skill4hire.entity.Application.ApplicationStatus appStatus = 
                com.se.skill4hire.entity.Application.ApplicationStatus.valueOf(status.toUpperCase());
            List<ApplicationDTO> applications = applicationService.list(candidateId, appStatus);
            return ResponseEntity.ok(applications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
