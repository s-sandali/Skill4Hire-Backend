package com.se.skill4hire.controller;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.entity.Recommendation;
import com.se.skill4hire.service.EmployeeRecommendationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRecommendationController {

    private final EmployeeRecommendationService employeeRecommendationService;

    // Constructor injection instead of @RequiredArgsConstructor
    public EmployeeRecommendationController(EmployeeRecommendationService employeeRecommendationService) {
        this.employeeRecommendationService = employeeRecommendationService;
    }

    // ==================== EMPLOYEE ENDPOINTS ====================

    @GetMapping("/employees/jobs/active")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<JobPost> getActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getActiveJobs(pageable);
    }

    @GetMapping("/employees/candidates/{candidateId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public CandidateProfile getCandidateProfile(@PathVariable String candidateId, HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        return employeeRecommendationService.getCandidateProfile(candidateId);
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
    public Page<Recommendation> getMyRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getEmployeeRecommendations(employeeId, pageable);
    }

    @GetMapping("/employees/jobs/{jobId}/recommendations")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Page<Recommendation> getJobRecommendations(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getJobRecommendations(jobId, pageable);
    }

    // ==================== COMPANY ENDPOINTS ====================

    @GetMapping("/companies/recommendations")
    @PreAuthorize("hasAuthority('COMPANY')")
    public Page<Recommendation> getCompanyRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getCompanyRecommendations(companyId, pageable);
    }

    @GetMapping("/companies/jobs/{jobId}/recommendations")
    @PreAuthorize("hasAuthority('COMPANY')")
    public Page<Recommendation> getCompanyJobRecommendations(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return employeeRecommendationService.getJobRecommendations(jobId, pageable);
    }
}