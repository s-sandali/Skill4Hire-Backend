package com.se.skill4hire.controller.application;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.service.application.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/applications")
public class CandidateApplicationController {

    private final ApplicationService applicationService;

    public CandidateApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getMyApplications(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        String candidateId = userIdObj == null ? null : String.valueOf(userIdObj);
        return ResponseEntity.ok(applicationService.list(candidateId, null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApplicationDTO>> getMyApplicationsByStatus(@PathVariable String status, HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        String candidateId = userIdObj == null ? null : String.valueOf(userIdObj);
        com.se.skill4hire.entity.Application.ApplicationStatus appStatus = com.se.skill4hire.entity.Application.ApplicationStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(applicationService.list(candidateId, appStatus));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApplicationService.Summary> getSummary(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        String candidateId = userIdObj == null ? null : String.valueOf(userIdObj);
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }
}
