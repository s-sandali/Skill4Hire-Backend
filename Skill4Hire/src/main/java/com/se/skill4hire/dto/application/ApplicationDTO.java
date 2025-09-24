package com.se.skill4hire.dto.application;

import com.se.skill4hire.entity.Application.SubmissionSource;

import java.time.Instant;

public class ApplicationDTO {
    private Long id;
    private Long candidateId;
    private Long companyId;
    private Long jobPostId;
    private SubmissionSource submittedBy;
    private Long createdById;
    private String status;
    private Instant createdAt;

    public ApplicationDTO() {}

    public ApplicationDTO(Long id, Long candidateId, Long companyId, Long jobPostId,
                          SubmissionSource submittedBy, Long createdById, String status, Instant createdAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.companyId = companyId;
        this.jobPostId = jobPostId;
        this.submittedBy = submittedBy;
        this.createdById = createdById;
        this.status = status;
        this.createdAt = createdAt;
    }

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
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
