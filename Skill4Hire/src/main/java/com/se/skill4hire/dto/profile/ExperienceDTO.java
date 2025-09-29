package com.se.skill4hire.dto.profile;

public class ExperienceDTO {
    private Boolean isExperienced;
    private String role;
    private String company;
    private Integer yearsOfExperience;

    public ExperienceDTO() {
        // Default constructor
    }

    public ExperienceDTO(Boolean isExperienced, String role, String company, Integer yearsOfExperience) {
        this.isExperienced = isExperienced;
        this.role = role;
        this.company = company;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Boolean getIsExperienced() {
        return isExperienced;
    }

    public void setIsExperienced(Boolean isExperienced) {
        this.isExperienced = isExperienced;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
}
