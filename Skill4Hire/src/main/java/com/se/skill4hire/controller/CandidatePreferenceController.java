package com.se.skill4hire.controller;

import com.se.skill4hire.service.CandidatePreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/{candidateId}/preferences")
public class CandidatePreferenceController {

    private final CandidatePreferenceService service;

    public CandidatePreferenceController(CandidatePreferenceService service) {
        this.service = service;
    }

    // DTO for list updates
    public record UpdateListRequest(List<String> items) {}

    // Companies
    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCompanies(@PathVariable Long candidateId) {
        return ResponseEntity.ok(service.getPreferredCompanies(candidateId));
    }

    @PutMapping(value = "/companies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> replaceCompanies(@PathVariable Long candidateId, @RequestBody UpdateListRequest body) {
        service.replacePreferredCompanies(candidateId, body != null ? body.items() : List.of());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/companies", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCompanies(@PathVariable Long candidateId, @RequestBody UpdateListRequest body) {
        service.addPreferredCompanies(candidateId, body != null ? body.items() : List.of());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/companies")
    public ResponseEntity<Void> clearCompanies(@PathVariable Long candidateId) {
        service.clearPreferredCompanies(candidateId);
        return ResponseEntity.noContent().build();
    }

    // Job roles
    @GetMapping(value = "/job-roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getJobRoles(@PathVariable Long candidateId) {
        return ResponseEntity.ok(service.getPreferredJobRoles(candidateId));
    }

    @PutMapping(value = "/job-roles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> replaceJobRoles(@PathVariable Long candidateId, @RequestBody UpdateListRequest body) {
        service.replacePreferredJobRoles(candidateId, body != null ? body.items() : List.of());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/job-roles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addJobRoles(@PathVariable Long candidateId, @RequestBody UpdateListRequest body) {
        service.addPreferredJobRoles(candidateId, body != null ? body.items() : List.of());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/job-roles")
    public ResponseEntity<Void> clearJobRoles(@PathVariable Long candidateId) {
        service.clearPreferredJobRoles(candidateId);
        return ResponseEntity.noContent().build();
    }
}

