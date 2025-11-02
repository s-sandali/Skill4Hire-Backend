package com.se.skill4hire.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "employee_notifications")
public class EmployeeNotification {
    @Id
    private String id;
    private String employeeId;
    private String title;     // short title for display
    private String message;
    private String type;      // e.g. CANDIDATE_PROFILE_CREATED, JOB_POST_CREATED, APPLICATION_SHORTLISTED
    private String category;  // e.g. REGISTRATIONS, JOB_POSTS, APPLICATION_UPDATES
    private Instant createdAt;
    private boolean read;

    public EmployeeNotification() {
        this.createdAt = Instant.now();
        this.read = false;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}