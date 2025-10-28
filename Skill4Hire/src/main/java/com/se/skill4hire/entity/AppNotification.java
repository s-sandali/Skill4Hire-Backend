package com.se.skill4hire.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notifications")
public class AppNotification {
    @Id
    private String id;
    private String userId; // candidate user id
    private String title;  // short title/subject
    private String message;
    private Instant createdAt;
    private boolean read;
    private String type; // e.g., RECOMMENDATION, APPLICATION_APPLIED, etc.

    // Optional deep-link references
    private String applicationId;
    private String jobPostId;

    public AppNotification() {
        this.createdAt = Instant.now();
        this.read = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getJobPostId() { return jobPostId; }
    public void setJobPostId(String jobPostId) { this.jobPostId = jobPostId; }
}
