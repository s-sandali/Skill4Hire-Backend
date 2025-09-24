package com.se.skill4hire.controller;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.service.application.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates/{candidateId}/applications")
public class CandidateApplicationsController {

    private final ApplicationService service;

    public CandidateApplicationsController(ApplicationService service) {
        this.service = service;
    }

    private boolean canAccess(Long pathCandidateId, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if ("ADMIN".equalsIgnoreCase(role)) return true;
        return sessionUserId != null && sessionUserId.equals(pathCandidateId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationDTO>> list(
            @PathVariable Long candidateId,
            @RequestParam(value = "status", required = false) String status,
            HttpSession session) {

        if (!canAccess(candidateId, session)) {
            return ResponseEntity.status(403).build();
        }

        Application.ApplicationStatus st = null;
        if (status != null && !status.isBlank()) {
            st = Application.ApplicationStatus.valueOf(status.toUpperCase());
        }
        return ResponseEntity.ok(service.list(candidateId, st));
    }

    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationService.Summary> summary(
            @PathVariable Long candidateId,
            HttpSession session) {
        if (!canAccess(candidateId, session)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(service.summary(candidateId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationDTO> create(
            @PathVariable Long candidateId,
            @RequestBody Map<String, Object> payload,
            HttpSession session) {
        if (!canAccess(candidateId, session)) {
            return ResponseEntity.status(403).build();
        }
        Long companyId = payload.get("companyId") == null ? null : Long.valueOf(payload.get("companyId").toString());
        String companyName = payload.get("companyName") == null ? null : payload.get("companyName").toString();
        if (companyId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.create(candidateId, companyId, companyName));
    }
}

