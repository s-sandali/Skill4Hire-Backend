package com.se.skill4hire.dto.candidate;

import java.util.ArrayList;
import java.util.List;

public class CandidateBasicView {
    private String candidateId;
    private String name;
    private String title;
    private String location;
    private List<String> skills = new ArrayList<>();
    private String profilePictureUrl; // e.g. /uploads/profile-pictures/<filename>
    private boolean hasCv;
    private String cvDownloadUrl; // e.g. /api/employees/candidates/{candidateId}/cv

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public boolean isHasCv() { return hasCv; }
    public void setHasCv(boolean hasCv) { this.hasCv = hasCv; }
    public String getCvDownloadUrl() { return cvDownloadUrl; }
    public void setCvDownloadUrl(String cvDownloadUrl) { this.cvDownloadUrl = cvDownloadUrl; }
}
