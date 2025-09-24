package com.se.skill4hire.controller.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.service.application.CandidateApplicationTrackingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates/applications")
public class CandidateApplicationController {

    private final CandidateApplicationTrackingService trackingService;

    public CandidateApplicationController(CandidateApplicationTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getSelfSubmitted(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(trackingService.getSelfSubmittedApplications(candidateId));
    }

    @GetMapping("/employer")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getEmployerSubmitted(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(trackingService.getEmployerSubmittedApplications(candidateId));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<Map<String, List<ApplicationDTO>>> getAllCategorized(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        List<ApplicationDTO> mine = trackingService.getSelfSubmittedApplications(candidateId);
        List<ApplicationDTO> employer = trackingService.getEmployerSubmittedApplications(candidateId);
        Map<String, List<ApplicationDTO>> body = new HashMap<>();
        body.put("selfSubmitted", mine);
        body.put("employerSubmitted", employer);
        return ResponseEntity.ok(body);
    }
}

