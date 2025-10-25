package com.se.skill4hire.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.se.skill4hire.entity.Recommendation;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.repository.RecommendationRepository;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import com.se.skill4hire.repository.profile.CandidateProfileRepository;
import com.se.skill4hire.service.notification.CompanyNotificationService;
import com.se.skill4hire.service.notification.NotificationService;

@Service
public class EmployeeRecommendationServiceImpl implements EmployeeRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeRecommendationServiceImpl.class);

    private final JobPostRepository jobPostRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final RecommendationRepository recommendationRepository;
    private final MongoTemplate mongoTemplate;
    private final NotificationService notificationService;
    private final CompanyNotificationService companyNotificationService;
    private final CompanyAuthRepository companyAuthRepository;

    // Constructor injection instead of @RequiredArgsConstructor
    public EmployeeRecommendationServiceImpl(
            JobPostRepository jobPostRepository,
            CandidateProfileRepository candidateProfileRepository,
            RecommendationRepository recommendationRepository,
            MongoTemplate mongoTemplate,
            NotificationService notificationService,
            CompanyNotificationService companyNotificationService,
            CompanyAuthRepository companyAuthRepository) {
        this.jobPostRepository = jobPostRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.recommendationRepository = recommendationRepository;
        this.mongoTemplate = mongoTemplate;
        this.notificationService = notificationService;
        this.companyNotificationService = companyNotificationService;
        this.companyAuthRepository = companyAuthRepository;
    }

    @Override
    public Page<JobPost> getActiveJobs(Pageable pageable) {
        log.info("Fetching active jobs for employee");
        return jobPostRepository.findByStatus(JobPost.JobStatus.ACTIVE, pageable);
    }

    @Override
    public CandidateProfile getCandidateProfile(String candidateId) {
        log.info("Fetching candidate profile: {}", candidateId);
        return candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found: " + candidateId));
    }

    @Override
    public Page<CandidateProfile> searchCandidates(String skill, Integer minExperience, Pageable pageable) {
        log.info("Searching candidates - skill: {}, minExperience: {}", skill, minExperience);

        Query query = new Query().with(pageable);

        // Build criteria based on filters
        if (skill != null && !skill.trim().isEmpty()) {
            query.addCriteria(Criteria.where("skills").in(skill.trim()));
        }

        if (minExperience != null && minExperience > 0) {
            query.addCriteria(Criteria.where("experience.yearsOfExperience").gte(minExperience));
        }

        // Execute query
        return org.springframework.data.support.PageableExecutionUtils.getPage(
                mongoTemplate.find(query, CandidateProfile.class, "candidate_profiles"),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), CandidateProfile.class, "candidate_profiles")
        );
    }

    @Override
    public Recommendation recommendCandidate(String employeeId, String candidateId, String jobId, String note) {
        log.info("Creating recommendation - employee: {}, candidate: {}, job: {}",
                employeeId, candidateId, jobId);

        // Verify job exists and is active
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        if (job.getStatus() != JobPost.JobStatus.ACTIVE) {
            throw new RuntimeException("Cannot recommend to inactive job: " + jobId);
        }

        // Verify candidate exists
        CandidateProfile candidate = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found: " + candidateId));

        // Check for duplicate recommendation
        Optional<Recommendation> existing = recommendationRepository
                .findByEmployeeIdAndCandidateIdAndJobId(employeeId, candidateId, jobId);

        if (existing.isPresent()) {
            throw new RuntimeException("Recommendation already exists for this candidate and job");
        }

        // Create new recommendation
        Recommendation recommendation = new Recommendation();
        recommendation.setEmployeeId(employeeId);
        recommendation.setCandidateId(candidateId);
        recommendation.setJobId(jobId);
        recommendation.setNote(note);
        recommendation.setStatus("CREATED");

        try {
            Recommendation saved = recommendationRepository.save(recommendation);
            log.info("Successfully created recommendation: {}", saved.getId());

            // Notify candidate
            String companyName = null;
            if (job.getCompanyId() != null) {
                companyName = companyAuthRepository.findById(job.getCompanyId())
                        .map(Company::getName)
                        .orElse(null);
            }
        notificationService.notifyCandidateRecommended(candidateId, companyName, job);
        companyNotificationService.notifyCandidateRecommended(
            job,
            candidateId,
            candidate.getName(),
            employeeId,
            saved.getId()
        );

            return saved;
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Recommendation already exists for this candidate and job");
        }
    }

    @Override
    public Page<Recommendation> getEmployeeRecommendations(String employeeId, Pageable pageable) {
        log.info("Fetching recommendations for employee: {}", employeeId);
        return recommendationRepository.findByEmployeeId(employeeId, pageable);
    }

    @Override
    public Page<Recommendation> getJobRecommendations(String jobId, Pageable pageable) {
        log.info("Fetching recommendations for job: {}", jobId);
        return recommendationRepository.findByJobId(jobId, pageable);
    }

    @Override
    public Page<Recommendation> getCompanyRecommendations(String companyId, Pageable pageable) {
        log.info("Fetching recommendations for company: {}", companyId);

        // Get all jobs for this company
        List<JobPost> companyJobs = jobPostRepository.findByCompanyId(companyId);

        if (companyJobs.isEmpty()) {
            return Page.empty(pageable);
        }

        // Get job IDs
        List<String> jobIds = companyJobs.stream()
                .map(JobPost::getId)
                .collect(Collectors.toList());

        // Use MongoTemplate to query multiple job IDs
        Query query = new Query()
                .with(pageable)
                .addCriteria(Criteria.where("jobId").in(jobIds))
                .with(Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Recommendation> recommendations = mongoTemplate.find(query, Recommendation.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Recommendation.class);

        return new PageImpl<>(recommendations, pageable, total);
    }
}