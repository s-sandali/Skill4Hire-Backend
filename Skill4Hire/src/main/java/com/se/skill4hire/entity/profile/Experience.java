package com.se.skill4hire.entity.profile;

import jakarta.validation.constraints.Size;

public class Experience {
    private Boolean isExperienced = false;

    @Size(max = 100, message = "Role must be less than 100 characters")
    private String role;

    @Size(max = 100, message = "Company must be less than 100 characters")
    private String company;

    private Integer yearsOfExperience;

    public Experience() {
        // Default constructor
    }

    public Experience(Boolean isExperienced, String role, String company, Integer yearsOfExperience) {
        this.isExperienced = isExperienced;
        this.role = role;
        this.company = company;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Boolean getIsExperienced() {
        return isExperienced;
    }

    public void setIsExperienced(Boolean experienced) {
        isExperienced = experienced;
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
