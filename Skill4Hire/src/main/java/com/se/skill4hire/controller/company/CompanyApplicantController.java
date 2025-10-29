package com.se.skill4hire.controller.company;

import com.se.skill4hire.dto.application.ApplicationDTO;
import com.se.skill4hire.dto.application.ApplicationStatusUpdateRequest;
import com.se.skill4hire.dto.candidate.CandidateBasicView;
import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.candidate.CandidateCv;
import com.se.skill4hire.repository.ApplicationRepository;
import com.se.skill4hire.service.CandidateCvService;
import com.se.skill4hire.service.application.ApplicationService;
import com.se.skill4hire.service.job.JobPostService;
import com.se.skill4hire.service.profile.CandidateService;
import com.se.skill4hire.service.EmployeeRecommendationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyApplicantController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final JobPostService jobPostService;
    private final CandidateService candidateService;
    private final CandidateCvService candidateCvService;
    private final EmployeeRecommendationService employeeRecommendationService;

    public CompanyApplicantController(ApplicationService applicationService,
                                      ApplicationRepository applicationRepository,
                                      JobPostService jobPostService,
                                      CandidateService candidateService,
                                      CandidateCvService candidateCvService,
                                      EmployeeRecommendationService employeeRecommendationService) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.jobPostService = jobPostService;
        this.candidateService = candidateService;
        this.candidateCvService = candidateCvService;
        this.employeeRecommendationService = employeeRecommendationService;
    }

    // List all applicants for this company (optional status filter)
    @GetMapping("/applications")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<List<ApplicationDTO>> listCompanyApplications(
            @RequestParam(value = "status", required = false) String status,
            HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        Application.ApplicationStatus st = parseStatusNullable(status);
        return ResponseEntity.ok(applicationService.listByCompany(companyId, st));
    }

    // List applicants for a specific job owned by this company (optional status filter)
    @GetMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<List<ApplicationDTO>> listJobApplications(
            @PathVariable String jobId,
            @RequestParam(value = "status", required = false) String status,
            HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (!jobPostService.existsByIdAndCompanyId(jobId, companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this job post");
        }
        Application.ApplicationStatus st = parseStatusNullable(status);
        return ResponseEntity.ok(applicationService.listByJob(jobId, st));
    }

    // Get candidate basic view (name, title, skills, location, picture, cv availability)
    @GetMapping("/candidates/{candidateId}/basic")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<CandidateBasicView> getCandidateBasicForCompany(@PathVariable String candidateId) {
        CandidateBasicView view = employeeRecommendationService.getCandidateBasic(candidateId);
        // Override CV download URL for company path
        if (view != null) {
            view.setCvDownloadUrl("/api/companies/candidates/" + candidateId + "/cv");
        }
        return ResponseEntity.ok(view);
    }

    // Get full candidate profile (DTO)
    @GetMapping("/candidates/{candidateId}/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<CandidateProfileDTO> getCandidateProfileForCompany(@PathVariable String candidateId) {
        CandidateProfileDTO dto = candidateService.getProfile(candidateId);
        return ResponseEntity.ok(dto);
    }

    // Download candidate CV
    @GetMapping("/candidates/{candidateId}/cv")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<byte[]> downloadCandidateCv(@PathVariable String candidateId) {
        CandidateCv cv = candidateCvService.getByCandidateId(candidateId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cv.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(cv.getContentType()))
                .body(cv.getData());
    }

    // Update application status (shortlist/interview/hire/reject), only for owning company
    @PutMapping("/applications/{applicationId}/status")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<ApplicationDTO> updateApplicationStatus(
            @PathVariable String applicationId,
            @Valid @RequestBody ApplicationStatusUpdateRequest request,
            HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        if (application.getCompanyId() == null || !application.getCompanyId().equals(companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this application");
        }

        Application.ApplicationStatus status = parseStatus(request.getStatus());
        if (status == Application.ApplicationStatus.REJECTED && (request.getReason() == null || request.getReason().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rejection reason is required when rejecting an application");
        }

        String decidedBy = "COMPANY_" + companyId;
        ApplicationDTO updated = applicationService.updateStatus(applicationId, status, request.getReason(), decidedBy);
        return ResponseEntity.ok(updated);
    }

    private Application.ApplicationStatus parseStatus(String status) {
        try {
            return Application.ApplicationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value: " + status, ex);
        }
    }

    private Application.ApplicationStatus parseStatusNullable(String status) {
        if (status == null || status.isBlank()) return null;
        return parseStatus(status);
    }
}
