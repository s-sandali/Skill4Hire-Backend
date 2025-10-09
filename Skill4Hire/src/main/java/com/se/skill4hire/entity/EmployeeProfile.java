package com.se.skill4hire.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "employee_profiles")
public class EmployeeProfile {
    @Id
    private String id;
    private String userId;
    private String name;
    private String email;
    @Size(max = 15)
    private String phoneNumber;
    @Size(max = 100)
    private String location;
    private LocalDate dateOfBirth;
    @Size(max = 100)
    private String title;
    private String headline;
    private List<String> skills;
    @Size(max = 1000)
    private String education;
    @Size(max = 2000)
    private String experience;
    @Size(max = 500)
    private String notificationPreferences;
    @Size(max = 255)
    private String profilePicturePath;
    private Double profileCompleteness;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EmployeeProfile() {
        // Default constructor
    }

    public EmployeeProfile(String id, String userId, String name, String email, String phoneNumber, String location,
                           LocalDate dateOfBirth, String title, String headline, List<String> skills, String education,
                           String experience, String notificationPreferences, String profilePicturePath,
                           Double profileCompleteness, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.dateOfBirth = dateOfBirth;
        this.title = title;
        this.headline = headline;
        this.skills = skills;
        this.education = education;
        this.experience = experience;
        this.notificationPreferences = notificationPreferences;
        this.profilePicturePath = profilePicturePath;
        this.profileCompleteness = profileCompleteness;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getNotificationPreferences() {
        return notificationPreferences;
    }

    public void setNotificationPreferences(String notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public Double getProfileCompleteness() {
        return profileCompleteness;
    }

    public void setProfileCompleteness(Double profileCompleteness) {
        this.profileCompleteness = profileCompleteness;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
