package com.se.skill4hire.entity;

<<<<<<< Updated upstream
public class JobPost {
}
=======
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "jobs")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Column(length = 4000)
    private String description;

    @NotBlank(message = "Job type is required")
    private String type; // e.g., "Full-time", "Part-time", "Contract", "Internship"

    @NotBlank(message = "Location is required")
    private String location;

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    private Double salary;

    @Min(value = 0, message = "Experience must be 0 or greater")
    private Integer experience; // in years

    @Future(message = "Deadline must be in the future")
    private LocalDate deadline;

    // Audit fields (optional but recommended)
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    // Default constructor
    public JobPost() {}

    // Constructor for creating new job posts
    public JobPost(String title, String description, String type, String location, 
                   Double salary, Integer experience, LocalDate deadline) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.salary = salary;
        this.experience = experience;
        this.deadline = deadline;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    // JPA lifecycle methods
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
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

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
>>>>>>> Stashed changes
