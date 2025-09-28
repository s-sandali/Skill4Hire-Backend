package com.se.skill4hire.dto.job;

import java.time.LocalDate;

public class JobPostDTO {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String location;
    private Double salary;
    private Integer experience;
    private LocalDate deadline;

    // Default constructor
    public JobPostDTO() {}

    // Constructor for creating new job posts (without ID)
    public JobPostDTO(String title, String description, String type, String location, 
                     Double salary, Integer experience, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.salary = salary;
        this.experience = experience;
        this.deadline = deadline;
    }

    // Full constructor
    public JobPostDTO(Long id, String title, String description, String type, String location, 
                     Double salary, Integer experience, LocalDate deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.salary = salary;
        this.experience = experience;
        this.deadline = deadline;
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
}
