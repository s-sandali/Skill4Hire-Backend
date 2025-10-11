package com.se.skill4hire.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.CandidateLoginRequest;
import com.se.skill4hire.dto.auth.CandidateRegRequest;
import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.candidate.CandidateCv;
import com.se.skill4hire.service.CandidateCvService;
import com.se.skill4hire.service.application.ApplicationService;
import com.se.skill4hire.service.application.CandidateApplicationTrackingService;
import com.se.skill4hire.service.auth.CandidateAuthService;
import com.se.skill4hire.service.profile.CandidateService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateAuthService candidateAuthService;
    private final CandidateService candidateService;
    private final ApplicationService applicationService;
    private final CandidateApplicationTrackingService trackingService;
    private final CandidateCvService candidateCvService;

    public CandidateController(CandidateAuthService candidateAuthService,
                               CandidateService candidateService,
                               ApplicationService applicationService,
                               CandidateApplicationTrackingService trackingService,
                               CandidateCvService candidateCvService) {
        this.candidateAuthService = candidateAuthService;
        this.candidateService = candidateService;
        this.applicationService = applicationService;
        this.trackingService = trackingService;
        this.candidateCvService = candidateCvService;
    }

    // ======================== Auth ========================

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CandidateRegRequest request) {
        AuthResponse response = candidateAuthService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody CandidateLoginRequest request, HttpSession session) {

        AuthResponse response = candidateAuthService.login(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = candidateAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<AuthResponse> getCurrentCandidate(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }

        if (!"CANDIDATE".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(new AuthResponse("Access denied - not a candidate", false));
        }

        return ResponseEntity.ok(new AuthResponse("You are logged in as candidate ID: " + userId, true, userId, role));
    }

    // ======================== Profile ========================

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<CandidateProfileDTO> getProfile(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        CandidateProfileDTO profile = candidateService.getProfile(candidateId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<CandidateProfileDTO> updateProfile(@Valid @RequestBody CandidateProfileDTO profileDTO,
                                                             HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        CandidateProfileDTO updatedProfile = candidateService.updateProfile(candidateId, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/profile/completeness")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<ProfileCompletenessDTO> getProfileCompleteness(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        ProfileCompletenessDTO completeness = candidateService.getProfileCompleteness(candidateId);
        return ResponseEntity.ok(completeness);
    }

    @PostMapping("/upload/resume")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Map<String, String>> uploadResume(@RequestParam("resume") MultipartFile file,
                                                            HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        String fileName = candidateService.uploadResume(candidateId, file);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Resume uploaded successfully");
        response.put("fileName", fileName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/profile-picture")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                    HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        String fileName = candidateService.uploadProfilePicture(candidateId, file);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture uploaded successfully");
        response.put("fileName", fileName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/skills")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<List<String>> addSkill(@RequestParam("skill") String skill,
                                                 HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        List<String> updatedSkills = candidateService.addSkill(candidateId, skill);
        return ResponseEntity.ok(updatedSkills);
    }

    @DeleteMapping("/skills/{skill}")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<List<String>> removeSkill(@PathVariable String skill,
                                                    HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        String decodedSkill = URLDecoder.decode(skill, StandardCharsets.UTF_8);
        List<String> updatedSkills = candidateService.removeSkill(candidateId, decodedSkill);
        return ResponseEntity.ok(updatedSkills);
    }

    // ======================== Applications ========================

    // Session-scoped
    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDTO>> getMyApplications(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        String candidateId = userIdObj == null ? null : String.valueOf(userIdObj);
        return ResponseEntity.ok(applicationService.list(candidateId, null));
    }

    @GetMapping("/applications/status/{status}")
    public ResponseEntity<List<ApplicationDTO>> getMyApplicationsByStatus(@PathVariable String status,
                                                                          HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        String candidateId = userIdObj == null ? null : String.valueOf(userIdObj);
        Application.ApplicationStatus appStatus = Application.ApplicationStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(applicationService.list(candidateId, appStatus));
    }

    @GetMapping("/applications/summary")
    public ResponseEntity<ApplicationService.Summary> getMyApplicationSummary(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        String candidateId = userIdObj == null ? null : String.valueOf(userIdObj);
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }

    @GetMapping("/applications/all")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<List<ApplicationDTO>> getAllApplications(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        return ResponseEntity.ok(trackingService.getAllApplicationsForCandidate(candidateId));
    }

    // CandidateId-scoped
    @GetMapping(value = "/{candidateId}/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationDTO>> getCandidateApplications(@PathVariable String candidateId) {
        List<ApplicationDTO> applications = applicationService.list(candidateId, null);
        return ResponseEntity.ok(applications);
    }

    @GetMapping(value = "/{candidateId}/applications/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationService.Summary> getCandidateApplicationSummary(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }

    @GetMapping(value = "/{candidateId}/applications/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationDTO>> getCandidateApplicationsByStatus(@PathVariable String candidateId,
                                                                                 @PathVariable String status) {
        try {
            Application.ApplicationStatus appStatus = Application.ApplicationStatus.valueOf(status.toUpperCase());
            List<ApplicationDTO> applications = applicationService.list(candidateId, appStatus);
            return ResponseEntity.ok(applications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{candidateId}/applications/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listCompaniesByStatus(@PathVariable String candidateId,
                                                                                      @RequestParam(value = "status", required = false) String status) {
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Application.ApplicationStatus st = Application.ApplicationStatus.valueOf(status.toUpperCase(java.util.Locale.ROOT));
            return ResponseEntity.ok(applicationService.getCompaniesByStatus(candidateId, st));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{candidateId}/applications/companies/applied", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listApplied(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.getCompaniesByStatus(candidateId, Application.ApplicationStatus.APPLIED));
    }

    @GetMapping(value = "/{candidateId}/applications/companies/shortlisted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listShortlisted(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.getCompaniesByStatus(candidateId, Application.ApplicationStatus.SHORTLISTED));
    }

    @GetMapping(value = "/{candidateId}/applications/companies/rejected", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationService.CompanyView>> listRejected(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.getCompaniesByStatus(candidateId, Application.ApplicationStatus.REJECTED));
    }

    // ======================== State ========================

    @GetMapping(value = "/{candidateId}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCandidateState(@PathVariable String candidateId) {
        Map<String, Object> state = new HashMap<>();
        ApplicationService.Summary appSummary = applicationService.summary(candidateId);
        state.put("applications", appSummary);
        ProfileCompletenessDTO profileCompleteness = candidateService.getProfileCompleteness(candidateId);
        state.put("profileCompleteness", profileCompleteness);
        String overallStatus = determineOverallStatus(appSummary, profileCompleteness);
        state.put("overallStatus", overallStatus);
        return ResponseEntity.ok(state);
    }

    @GetMapping(value = "/{candidateId}/state/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationService.Summary> getApplicationStats(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.summary(candidateId));
    }

    @GetMapping(value = "/{candidateId}/state/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileCompletenessDTO> getProfileState(@PathVariable String candidateId) {
        return ResponseEntity.ok(candidateService.getProfileCompleteness(candidateId));
    }

    @GetMapping(value = "/{candidateId}/state/tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getCandidateTracking(@PathVariable String candidateId) {
        Map<String, Object> tracking = new HashMap<>();
        ApplicationService.Summary appSummary = applicationService.summary(candidateId);
        tracking.put("applications", appSummary);
        ProfileCompletenessDTO profileCompleteness = candidateService.getProfileCompleteness(candidateId);
        tracking.put("profileCompleteness", profileCompleteness);
        Map<String, Object> cvStatus = new HashMap<>();
        cvStatus.put("hasResume", profileCompleteness.getCompletenessPercentage() > 0);
        cvStatus.put("completenessPercentage", profileCompleteness.getCompletenessPercentage());
        tracking.put("cvStatus", cvStatus);
        String overallStatus = determineOverallStatus(appSummary, profileCompleteness);
        tracking.put("overallStatus", overallStatus);
        return ResponseEntity.ok(tracking);
    }

    @GetMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Map<String, Object>> getMyState(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        return getCandidateState(candidateId);
    }

    // ======================== CV ========================

    @PostMapping(value = "/{candidateId}/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateCvResponse> uploadCv(@PathVariable String candidateId,
                                                        @RequestPart("file") MultipartFile file) throws Exception {
        CandidateCv saved = candidateCvService.upload(candidateId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(CandidateCvResponse.from(saved));
    }

    @PutMapping(value = "/{candidateId}/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateCvResponse> replaceCv(@PathVariable String candidateId,
                                                         @RequestPart("file") MultipartFile file) throws Exception {
        CandidateCv saved = candidateCvService.upload(candidateId, file);
        return ResponseEntity.ok(CandidateCvResponse.from(saved));
    }

    @GetMapping(value = "/{candidateId}/cv")
    public ResponseEntity<byte[]> downloadCv(@PathVariable String candidateId) {
        CandidateCv cv = candidateCvService.getByCandidateId(candidateId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cv.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(cv.getContentType()))
                .body(cv.getData());
    }

    @DeleteMapping(value = "/{candidateId}/cv")
    public ResponseEntity<Void> deleteCv(@PathVariable String candidateId) {
        candidateCvService.deleteByCandidateId(candidateId);
        return ResponseEntity.noContent().build();
    }

    private String determineOverallStatus(ApplicationService.Summary appSummary, ProfileCompletenessDTO profileCompleteness) {
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

    public record CandidateCvResponse(String id, String candidateId, String filename, String contentType) {
        public static CandidateCvResponse from(CandidateCv cv) {
            return new CandidateCvResponse(cv.getId(), cv.getCandidateId(), cv.getFilename(), cv.getContentType());
        }
    }
}
