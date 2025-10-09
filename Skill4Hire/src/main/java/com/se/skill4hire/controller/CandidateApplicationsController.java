package com.se.skill4hire.controller;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.service.application.ApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateApplicationsController {

    private final ApplicationService applicationService;

    public CandidateApplicationsController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Get all applications for a candidate with job details and statuses
     * Endpoint: GET /api/candidates/{candidateId}/applications
     */
    @GetMapping(value = "/{candidateId}/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationDTO>> getCandidateApplications(@PathVariable String candidateId) {
        List<ApplicationDTO> applications = applicationService.list(candidateId, null);
        return ResponseEntity.ok(applications);
    }
}