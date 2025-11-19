package com.se.skill4hire.service.job;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.service.profile.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.dto.job.EnrichedJobPostDTO;
import com.se.skill4hire.repository.profile.CompanyProfileRepository;
import com.se.skill4hire.entity.CompanyProfile;

@Service
public class JobSearchService {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CompanyAuthRepository companyAuthRepository;

    @Autowired
    private CompanyProfileRepository companyProfileRepository;

    // Enhanced skill dictionary
    private static final Set<String> COMMON_SKILLS = Set.of(
            "java", "python", "javascript", "typescript", "spring", "spring boot",
            "react", "angular", "vue", "node.js", "express", "django", "flask",
            "sql", "mysql", "postgresql", "mongodb", "redis", "oracle",
            "aws", "azure", "gcp", "docker", "kubernetes", "jenkins", "git",
            "html", "css", "sass", "bootstrap", "tailwind", "rest api", "graphql",
            "machine learning", "ai", "data analysis", "big data", "hadoop", "spark",
            "cybersecurity", "devops", "agile", "scrum", "project management",
            "c++", "c#", "ruby", "php", "swift", "kotlin", "go", "rust"
    );

    public List<JobWithMatchScore> searchJobsWithSkillMatching(String candidateId, String keyword, String type,
                                                               String location, Double minSalary, Double maxSalary, Integer maxExperience) {
        // Get candidate skills
        CandidateProfile candidateProfile = candidateService.getCandidateEntity(candidateId);
        List<String> candidateSkills = candidateProfile.getSkills();

        // Search jobs using existing service (no direct skill filter here)
        List<JobPost> jobs = jobPostService.searchJobs(keyword, type, location, minSalary, maxSalary, maxExperience, null);

        // Filter and rank by skill matching
        return jobs.stream()
                .map(job -> {
                    JobMatchResult result = calculateSkillMatch(job, candidateSkills);
                    return new JobWithMatchScore(job, result.getMatchScore(), result.getMatchingSkills());
                })
                .sorted((j1, j2) -> Double.compare(j2.getMatchScore(), j1.getMatchScore()))
                .collect(Collectors.toList());
    }

    // New: Enriched mapping for JobPost with company info
    public EnrichedJobPostDTO toEnriched(JobPost job) {
        if (job == null) return null;
        EnrichedJobPostDTO dto = new EnrichedJobPostDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setType(job.getType());
        dto.setLocation(job.getLocation());
        dto.setSalary(job.getSalary());
        dto.setExperience(job.getExperience());
        dto.setDeadline(job.getDeadline());
        dto.setCompanyId(job.getCompanyId());
        dto.setStatus(job.getStatus());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setSkills(job.getSkills());

        if (job.getCompanyId() != null) {
            // Primary: from auth repository
            Optional<Company> companyOpt = companyAuthRepository.findById(job.getCompanyId());
            String name = null;
            String logo = null;
            if (companyOpt.isPresent()) {
                Company c = companyOpt.get();
                name = safe(c.getName());
                logo = sanitizeLogo(c.getLogo());
            }
            // Fallback: from older company profile collection
            if ((name == null || name.isBlank()) || (logo == null || logo.isBlank())) {
                Optional<CompanyProfile> profileOpt = companyProfileRepository.findById(job.getCompanyId());
                if (profileOpt.isPresent()) {
                    CompanyProfile cp = profileOpt.get();
                    if (name == null || name.isBlank()) name = safe(cp.getCompanyName());
                    if (logo == null || logo.isBlank()) logo = sanitizeLogo(cp.getLogoUrl());
                }
            }
            if (name != null) dto.setCompanyName(name);
            if (logo != null) dto.setCompanyLogo(logo);
        }
        return dto;
    }

    private String sanitizeLogo(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String val = raw.trim();
        if (val.startsWith("data:")) return val; // already a data URL
        // Heuristic: if looks like a URL, return as-is
        if (val.startsWith("http://") || val.startsWith("https://") || val.startsWith("/")) return val;
        // Heuristic: base64 blob without prefix -> default to png
        if (val.matches("[A-Za-z0-9+/=\\r\\n]+")) {
            return "data:image/png;base64," + val.replaceAll("\\r|\\n", "");
        }
        return val; // as-is, let frontend try
    }

    public List<EnrichedJobPostDTO> searchJobsBasicEnriched(String keyword, String type, String location,
                                         Double minSalary, Double maxSalary, Integer maxExperience, String skill) {
        return jobPostService.searchJobs(keyword, type, location, minSalary, maxSalary, maxExperience, skill)
                .stream()
                .map(this::toEnriched)
                .collect(Collectors.toList());
    }

    private JobMatchResult calculateSkillMatch(JobPost job, List<String> candidateSkills) {
        if (candidateSkills == null || candidateSkills.isEmpty()) {
            return new JobMatchResult(0.0, List.of());
        }

        // Extract skills from job description and title
        String jobText = (job.getTitle() + " " + job.getDescription()).toLowerCase();
        Set<String> jobSkills = extractSkillsFromText(jobText);

        if (jobSkills.isEmpty()) {
            return new JobMatchResult(0.0, List.of());
        }

        // Find matching skills (case insensitive)
        List<String> matchingSkills = candidateSkills.stream()
                .map(String::toLowerCase)
                .filter(skill -> jobText.contains(skill.toLowerCase()))
                .collect(Collectors.toList());

        // Calculate match score (percentage of job skills that candidate has)
        double matchScore = jobSkills.isEmpty() ? 0 :
                (double) matchingSkills.size() / jobSkills.size() * 100;

        return new JobMatchResult(matchScore, matchingSkills);
    }

    private Set<String> extractSkillsFromText(String text) {
        return COMMON_SKILLS.stream()
                .filter(skill -> text.contains(skill))
                .collect(Collectors.toSet());
    }

    // Simple search without skill matching (for non-logged in users)
    public List<JobPost> searchJobsBasic(String keyword, String type, String location,
                                         Double minSalary, Integer maxExperience) {
        // Keep legacy signature for compatibility; delegate to new method
        return jobPostService.searchJobs(keyword, type, location, minSalary, null, maxExperience, null);
    }

    private static class JobMatchResult {
        private final double matchScore;
        private final List<String> matchingSkills;

        public JobMatchResult(double matchScore, List<String> matchingSkills) {
            this.matchScore = matchScore;
            this.matchingSkills = matchingSkills;
        }

        public double getMatchScore() { return matchScore; }
        public List<String> getMatchingSkills() { return matchingSkills; }
    }

    public static class JobWithMatchScore {
        private final JobPost jobPost;
        private final double matchScore;
        private final List<String> matchingSkills;

        public JobWithMatchScore(JobPost jobPost, double matchScore, List<String> matchingSkills) {
            this.jobPost = jobPost;
            this.matchScore = matchScore;
            this.matchingSkills = matchingSkills;
        }

        public JobPost getJobPost() { return jobPost; }
        public double getMatchScore() { return matchScore; }
        public List<String> getMatchingSkills() { return matchingSkills; }
    }

    private String safe(String s) { return s == null ? null : s; }
}
