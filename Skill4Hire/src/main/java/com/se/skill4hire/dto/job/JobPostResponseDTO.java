package com.se.skill4hire.dto.job;

import com.se.skill4hire.entity.job.JobPost;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobPostResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String location;
    private Double salary;
    private Integer experience;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private Long companyId;
    private String companyName;

    public JobPostResponseDTO(JobPost jobPost) {
        this.id = jobPost.getId();
        this.title = jobPost.getTitle();
        this.description = jobPost.getDescription();
        this.type = jobPost.getType();
        this.location = jobPost.getLocation();
        this.salary = jobPost.getSalary();
        this.experience = jobPost.getExperience();
        this.deadline = jobPost.getDeadline();
        this.createdAt = jobPost.getCreatedAt();
        this.updatedAt = jobPost.getUpdatedAt();
        this.status = jobPost.getStatus().name();

        if (jobPost.getCompany() != null) {
            this.companyId = jobPost.getCompany().getId();
            this.companyName = jobPost.getCompany().getName();
        }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}