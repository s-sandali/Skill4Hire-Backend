package com.se.skill4hire.dto.job;

import com.se.skill4hire.entity.job.JobPost;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EnrichedJobPostDTO {
    private String id;
    private String title;
    private String description;
    private String type;
    private String location;
    private Double salary;
    private Integer experience;
    private LocalDate deadline;
    private String companyId;
    private JobPost.JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> skills;

    private String companyName;
    private String companyLogo; // data URL or absolute URL

    public EnrichedJobPostDTO() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public JobPost.JobStatus getStatus() { return status; }
    public void setStatus(JobPost.JobStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyLogo() { return companyLogo; }
    public void setCompanyLogo(String companyLogo) { this.companyLogo = companyLogo; }
}

