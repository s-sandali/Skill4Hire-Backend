package com.se.skill4hire.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "applications")
public class Application {

    public enum SubmissionSource {
        CANDIDATE,
        EMPLOYER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "job_post_id")
    private Long jobPostId;

    @Enumerated(EnumType.STRING)
    @Column(name = "submitted_by", nullable = false)
    private SubmissionSource submittedBy;

    @Column(name = "created_by_id")
    private Long createdById; // candidateId when CANDIDATE; employeeId when EMPLOYER

    @Column(name = "status")
    private String status; // optional: e.g., PENDING, REVIEWED, REJECTED, ACCEPTED

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public Long getJobPostId() { return jobPostId; }
    public void setJobPostId(Long jobPostId) { this.jobPostId = jobPostId; }

    public SubmissionSource getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(SubmissionSource submittedBy) { this.submittedBy = submittedBy; }

    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
}
