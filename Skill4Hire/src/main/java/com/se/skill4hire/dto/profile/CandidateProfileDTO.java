package com.se.skill4hire.dto.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CandidateProfileDTO {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String location;
    private LocalDate dateOfBirth;
    private String title;
    private String headline;
    private List<String> skills;
    private EducationDTO education;
    private ExperienceDTO experience;
    private JobPreferencesDTO jobPreferences;
    private NotificationPreferencesDTO notificationPreferences;
    private String resumePath;
    private String profilePicturePath;
    private Double profileCompleteness;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CandidateProfileDTO() {
        // Default constructor
    }

    public CandidateProfileDTO(String id, String name, String email, String phoneNumber, String location,
                               LocalDate dateOfBirth, String title, String headline, List<String> skills,
                               EducationDTO education, ExperienceDTO experience, JobPreferencesDTO jobPreferences,
                               NotificationPreferencesDTO notificationPreferences, String resumePath,
                               String profilePicturePath, Double profileCompleteness, LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
        this.id = id;
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
        this.jobPreferences = jobPreferences;
        this.notificationPreferences = notificationPreferences;
        this.resumePath = resumePath;
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

    public EducationDTO getEducation() {
        return education;
    }

    public void setEducation(EducationDTO education) {
        this.education = education;
    }

    public ExperienceDTO getExperience() {
        return experience;
    }

    public void setExperience(ExperienceDTO experience) {
        this.experience = experience;
    }

    public JobPreferencesDTO getJobPreferences() {
        return jobPreferences;
    }

    public void setJobPreferences(JobPreferencesDTO jobPreferences) {
        this.jobPreferences = jobPreferences;
    }

    public NotificationPreferencesDTO getNotificationPreferences() {
        return notificationPreferences;
    }

    public void setNotificationPreferences(NotificationPreferencesDTO notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
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
