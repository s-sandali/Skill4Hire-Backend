package com.se.skill4hire.controller.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.service.application.CandidateApplicationTrackingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/applications")
public class CandidateApplicationTrackingController {

    private final CandidateApplicationTrackingService trackingService;

    public CandidateApplicationTrackingController(CandidateApplicationTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        return ResponseEntity.ok(trackingService.getAllApplicationsForCandidate(candidateId));
    }
}
