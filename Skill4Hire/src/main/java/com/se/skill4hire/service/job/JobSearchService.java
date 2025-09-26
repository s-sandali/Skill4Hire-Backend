package com.se.skill4hire.service.job;

import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.service.profile.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobSearchService {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private CandidateService candidateService;

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

    public List<JobWithMatchScore> searchJobsWithSkillMatching(Long candidateId, String keyword, String type,
                                                               String location, Double minSalary, Integer maxExperience) {
        // Get candidate skills
        CandidateProfile candidateProfile = candidateService.getCandidateEntity(candidateId);
        List<String> candidateSkills = candidateProfile.getSkills();

        // Search jobs using existing service
        List<JobPost> jobs = jobPostService.searchJobs(keyword, type, location, minSalary, maxExperience);

        // Filter and rank by skill matching
        return jobs.stream()
                .map(job -> {
                    JobMatchResult result = calculateSkillMatch(job, candidateSkills);
                    return new JobWithMatchScore(job, result.getMatchScore(), result.getMatchingSkills());
                })
                .sorted((j1, j2) -> Double.compare(j2.getMatchScore(), j1.getMatchScore()))
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
        return jobPostService.searchJobs(keyword, type, location, minSalary, maxExperience);
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
}