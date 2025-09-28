package com.se.skill4hire.dto.application;

import java.time.LocalDateTime;

public class ApplicationDTO {
    private Long id;
    private Long candidateId;
    private Long companyId;
    private String companyName;
    private String status;
    private LocalDateTime appliedAt;
    
    // Job details
    private Long jobPostId;
    private String jobTitle;
    private String jobDescription;
    private String jobType;
    private String jobLocation;
    private Double salary;
    private Integer experienceRequired;
    private LocalDateTime jobDeadline;
    
    // Explicit setters for required fields
    public void setId(Long id) { this.id = id; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setStatus(String status) { this.status = status; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
    public void setJobPostId(Long jobPostId) { this.jobPostId = jobPostId; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setJobLocation(String jobLocation) { this.jobLocation = jobLocation; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setExperienceRequired(Integer experienceRequired) { this.experienceRequired = experienceRequired; }
    public void setJobDeadline(LocalDateTime jobDeadline) { this.jobDeadline = jobDeadline; }
    
    // Getters
    public Long getId() { return id; }
    public Long getCandidateId() { return candidateId; }
    public Long getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getStatus() { return status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public Long getJobPostId() { return jobPostId; }
    public String getJobTitle() { return jobTitle; }
    public String getJobDescription() { return jobDescription; }
    public String getJobType() { return jobType; }
    public String getJobLocation() { return jobLocation; }
    public Double getSalary() { return salary; }
    public Integer getExperienceRequired() { return experienceRequired; }
    public LocalDateTime getJobDeadline() { return jobDeadline; }
}
