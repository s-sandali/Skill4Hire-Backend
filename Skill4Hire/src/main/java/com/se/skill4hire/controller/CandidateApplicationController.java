package com.se.skill4hire.controller;

import com.se.skill4hire.entity.Application;
import com.se.skill4hire.service.application.ApplicationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/{candidateId}/applications/companies")
public class CandidateApplicationController {

    private final ApplicationService service;

    public CandidateApplicationController(ApplicationService service) {
        this.service = service;
    }

    // Generic filter endpoint: /?status=APPLIED|SHORTLISTED|REJECTED
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listByStatus(@PathVariable Long candidateId,
                                                                             @RequestParam("status") String status) {
        Application.ApplicationStatus st = Application.ApplicationStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(service.getCompaniesByStatus(candidateId, st));
    }

    @GetMapping(value = "/applied", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listApplied(@PathVariable Long candidateId) {
        return ResponseEntity.ok(service.getCompaniesByStatus(candidateId, Application.ApplicationStatus.APPLIED));
    }

    @GetMapping(value = "/shortlisted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listShortlisted(@PathVariable Long candidateId) {
        return ResponseEntity.ok(service.getCompaniesByStatus(candidateId, Application.ApplicationStatus.SHORTLISTED));
    }

    @GetMapping(value = "/rejected", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listRejected(@PathVariable Long candidateId) {
        return ResponseEntity.ok(service.getCompaniesByStatus(candidateId, Application.ApplicationStatus.REJECTED));
    }
}

