package com.se.skill4hire.controller;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.entity.Recommendation;
import com.se.skill4hire.service.EmployeeRecommendationService;
import com.se.skill4hire.service.CandidateCvService;
import com.se.skill4hire.service.job.JobPostService;
import com.se.skill4hire.entity.candidate.CandidateCv;
import com.se.skill4hire.dto.candidate.CandidateBasicView;
import com.se.skill4hire.dto.job.EnrichedJobPostDTO;
import com.se.skill4hire.service.job.JobSearchService;
import com.se.skill4hire.dto.recommendation.RecommendationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EmployeeRecommendationController {

    private final EmployeeRecommendationService employeeRecommendationService;
    private final CandidateCvService candidateCvService;
    private final JobPostService jobPostService;
    private final JobSearchService jobSearchService;

    // Constructor injection instead of @RequiredArgsConstructor
    public EmployeeRecommendationController(EmployeeRecommendationService employeeRecommendationService,
                                            CandidateCvService candidateCvService,
                                            JobPostService jobPostService,
                                            JobSearchService jobSearchService) {
        this.employeeRecommendationService = employeeRecommendationService;
        this.candidateCvService = candidateCvService;
        this.jobPostService = jobPostService;
        this.jobSearchService = jobSearchService;
    }

    // ==================== EMPLOYEE ENDPOINTS ====================

    @GetMapping("/employees/jobs/active")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<EnrichedJobPostDTO> getActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<JobPost> raw = employeeRecommendationService.getActiveJobs(pageable);
        List<EnrichedJobPostDTO> mapped = raw.getContent().stream()
                .map(jobSearchService::toEnriched)
                .collect(Collectors.toList());
        return new PageImpl<>(mapped, pageable, raw.getTotalElements());
    }

    // Basic candidate card for employees
    @GetMapping("/employees/candidates/{candidateId}/basic")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public CandidateBasicView getCandidateBasic(@PathVariable String candidateId, HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        return employeeRecommendationService.getCandidateBasic(candidateId);
    }

    @GetMapping("/employees/candidates/{candidateId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public CandidateProfile getCandidateProfile(@PathVariable String candidateId, HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        return employeeRecommendationService.getCandidateProfile(candidateId);
    }

    @GetMapping("/employees/candidates/{candidateId}/cv")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<byte[]> downloadCandidateCv(@PathVariable String candidateId, HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        CandidateCv cv = candidateCvService.getByCandidateId(candidateId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cv.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(cv.getContentType()))
                .body(cv.getData());
    }

    @GetMapping("/employees/candidates")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<CandidateProfile> searchCandidates(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return employeeRecommendationService.searchCandidates(skill, minExperience, pageable);
    }

    // Search candidate basics list for quick browse
    @GetMapping("/employees/candidates/basic")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<CandidateBasicView> searchCandidateBasics(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return employeeRecommendationService.searchCandidateBasics(skill, minExperience, pageable);
    }

    @PostMapping("/employees/recommendations")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public Recommendation recommendCandidate(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        String candidateId = request.get("candidateId");
        String jobId = request.get("jobId");
        String note = request.get("note");

        return employeeRecommendationService.recommendCandidate(employeeId, candidateId, jobId, note);
    }

    @GetMapping("/employees/recommendations")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<RecommendationView> getMyRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getEmployeeRecommendationViews(employeeId, pageable);
    }

    @GetMapping("/employees/jobs/{jobId}/recommendations")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<RecommendationView> getJobRecommendations(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getJobRecommendationViews(jobId, pageable);
    }

    // ==================== COMPANY ENDPOINTS ====================

    @GetMapping("/companies/recommendations")
    @PreAuthorize("hasAuthority('COMPANY')")
    public Page<RecommendationView> getCompanyRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        // CompanyAuthService sets 'userId' in session
        String companyId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getCompanyRecommendationViews(companyId, pageable);
    }

    @GetMapping("/companies/jobs/{jobId}/recommendations")
    @PreAuthorize("hasAuthority('COMPANY')")
    public Page<RecommendationView> getCompanyJobRecommendations(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        // CompanyAuthService sets 'userId' in session
        String companyId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Ensure the job belongs to the requesting company; otherwise, forbid access
        if (!jobPostService.existsByIdAndCompanyId(jobId, companyId)) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this job post");
        }
        return employeeRecommendationService.getJobRecommendationViews(jobId, pageable);
    }
}
