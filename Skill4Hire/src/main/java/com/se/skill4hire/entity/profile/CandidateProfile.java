package com.se.skill4hire.entity.profile;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "candidate_profiles")
public class CandidateProfile {
    @Id
    private String id;

    private String userId;

    // Added name and email fields (service expects these)
    private String name;
    private String email;

    @Size(max = 15, message = "Phone number must be less than 15 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Location must be less than 100 characters")
    private String location;

    private LocalDate dateOfBirth;

    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    private String headline;

    private List<String> skills;

    private Education education;

    private Experience experience;

    private JobPreferences jobPreferences;

    private NotificationPreferences notificationPreferences;

    private String resumePath;

    @Size(max = 255, message = "Profile picture path must be less than 255 characters")
    private String profilePicturePath;

    private Double profileCompleteness;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CandidateProfile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.skills = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    // Name and email getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public Education getEducation() { return education; }
    public void setEducation(Education education) { this.education = education; }
    public Experience getExperience() { return experience; }
    public void setExperience(Experience experience) { this.experience = experience; }
    public JobPreferences getJobPreferences() { return jobPreferences; }
    public void setJobPreferences(JobPreferences jobPreferences) { this.jobPreferences = jobPreferences; }
    public NotificationPreferences getNotificationPreferences() { return notificationPreferences; }
    public void setNotificationPreferences(NotificationPreferences notificationPreferences) { this.notificationPreferences = notificationPreferences; }
    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
    public String getProfilePicturePath() { return profilePicturePath; }
    public void setProfilePicturePath(String profilePicturePath) { this.profilePicturePath = profilePicturePath; }
    public Double getProfileCompleteness() { return profileCompleteness; }
    public void setProfileCompleteness(Double profileCompleteness) { this.profileCompleteness = profileCompleteness; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
