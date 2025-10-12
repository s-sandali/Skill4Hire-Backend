package com.se.skill4hire.dto.application;

import java.time.LocalDateTime;

public class ApplicationDTO {
    private String id;
    private String candidateId;
    private String companyId;
    private String companyName;
    private String status;
    private LocalDateTime appliedAt;
    
    // Job details
    private String jobPostId;
    private String jobTitle;
    private String jobDescription;
    private String jobType;
    private String jobLocation;
    private Double salary;
    private Integer experienceRequired;
    private LocalDateTime jobDeadline;

    // Tag if candidate was recommended by a Skill4Hire employee for this job
    private boolean recommendedBySkill4Hire;

    // Explicit setters for required fields
    public void setId(String id) { this.id = id; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setStatus(String status) { this.status = status; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
    public void setJobPostId(String jobPostId) { this.jobPostId = jobPostId; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setJobLocation(String jobLocation) { this.jobLocation = jobLocation; }
    public void setSalary(Double salary) { this.salary = salary; }
    public void setExperienceRequired(Integer experienceRequired) { this.experienceRequired = experienceRequired; }
    public void setJobDeadline(LocalDateTime jobDeadline) { this.jobDeadline = jobDeadline; }
    public void setRecommendedBySkill4Hire(boolean recommendedBySkill4Hire) { this.recommendedBySkill4Hire = recommendedBySkill4Hire; }

    // Getters
    public String getId() { return id; }
    public String getCandidateId() { return candidateId; }
    public String getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getStatus() { return status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public String getJobPostId() { return jobPostId; }
    public String getJobTitle() { return jobTitle; }
    public String getJobDescription() { return jobDescription; }
    public String getJobType() { return jobType; }
    public String getJobLocation() { return jobLocation; }
    public Double getSalary() { return salary; }
    public Integer getExperienceRequired() { return experienceRequired; }
    public LocalDateTime getJobDeadline() { return jobDeadline; }
    public boolean isRecommendedBySkill4Hire() { return recommendedBySkill4Hire; }
}
