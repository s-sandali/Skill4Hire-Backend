package com.se.skill4hire.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "employee_notifications")
public class EmployeeNotification {
    @Id
    private String id;
    private String employeeId;
    private String message;
    private String type; // CANDIDATE_PROFILE_CREATED, JOB_POST_CREATED, NEW_APPLICATION
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

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}