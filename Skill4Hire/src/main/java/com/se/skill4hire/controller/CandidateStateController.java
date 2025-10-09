package com.se.skill4hire.controller;

import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.service.application.ApplicationService;
import com.se.skill4hire.service.profile.CandidateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates/{candidateId}/state")
public class CandidateStateController {

    private final ApplicationService applicationService;
    private final CandidateService candidateService;

    public CandidateStateController(ApplicationService applicationService, CandidateService candidateService) {
        this.applicationService = applicationService;
        this.candidateService = candidateService;
    }

    /**
     * Get comprehensive candidate state including applications and profile completeness
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCandidateState(@PathVariable String candidateId) {
        Map<String, Object> state = new HashMap<>();
        
        // Application summary
        ApplicationService.Summary appSummary = applicationService.summary(candidateId);
        state.put("applications", appSummary);
        
        // Profile completeness
        ProfileCompletenessDTO profileCompleteness = candidateService.getProfileCompleteness(candidateId);
        state.put("profileCompleteness", profileCompleteness);
        
        // Overall status
        String overallStatus = determineOverallStatus(appSummary, profileCompleteness);
        state.put("overallStatus", overallStatus);
        
        return ResponseEntity.ok(state);
    }

    /**
     * Get only application statistics
     */
    @GetMapping(value = "/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationService.Summary> getApplicationStats(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }

    /**
     * Get only profile completeness
     */
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileCompletenessDTO> getProfileState(@PathVariable String candidateId) {
        return ResponseEntity.ok(candidateService.getProfileCompleteness(candidateId));
    }

    /**
     * Get comprehensive tracking including CV uploads and application statuses
     */
    @GetMapping(value = "/tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCandidateTracking(@PathVariable String candidateId) {
        Map<String, Object> tracking = new HashMap<>();
        
        // Application summary
        ApplicationService.Summary appSummary = applicationService.summary(candidateId);
        tracking.put("applications", appSummary);
        
        // Profile completeness (includes CV upload status)
        ProfileCompletenessDTO profileCompleteness = candidateService.getProfileCompleteness(candidateId);
        tracking.put("profileCompleteness", profileCompleteness);
        
        // CV upload status
        Map<String, Object> cvStatus = new HashMap<>();
        cvStatus.put("hasResume", profileCompleteness.getCompletenessPercentage() > 0);
        cvStatus.put("completenessPercentage", profileCompleteness.getCompletenessPercentage());
        tracking.put("cvStatus", cvStatus);
        
        // Overall status
        String overallStatus = determineOverallStatus(appSummary, profileCompleteness);
        tracking.put("overallStatus", overallStatus);
        
        return ResponseEntity.ok(tracking);
    }

    private String determineOverallStatus(ApplicationService.Summary appSummary, ProfileCompletenessDTO profileCompleteness) {
        // Determine overall status based on applications and profile completeness
        if (profileCompleteness.getCompletenessPercentage() < 50) {
            return "PROFILE_INCOMPLETE";
        } else if (appSummary.applied() == 0) {
            return "NO_APPLICATIONS";
        } else if (appSummary.shortlisted() > 0) {
            return "ACTIVE_CANDIDATE";
        } else if (appSummary.rejected() > 0 && appSummary.applied() > 0) {
            return "NEEDS_IMPROVEMENT";
        } else {
            return "ACTIVE_APPLICANT";
        }
    }
}
