package com.se.skill4hire.entity.profile;

import jakarta.validation.constraints.Size;

public class JobPreferences {
    @Size(max = 100, message = "Preferred role must be less than 100 characters")
    private String preferredRole;

    @Size(max = 100, message = "Preferred location must be less than 100 characters")
    private String preferredLocation;

    private Double expectedSalary;

    private String jobType; // FULL_TIME, PART_TIME, CONTRACT

    private Boolean willingToRelocate = false;

    public JobPreferences() {
        // Default constructor
    }

    public JobPreferences(String preferredRole, String preferredLocation, Double expectedSalary, String jobType, Boolean willingToRelocate) {
        this.preferredRole = preferredRole;
        this.preferredLocation = preferredLocation;
        this.expectedSalary = expectedSalary;
        this.jobType = jobType;
        this.willingToRelocate = willingToRelocate;
    }

    public String getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public Double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Boolean getWillingToRelocate() {
        return willingToRelocate;
    }

    public void setWillingToRelocate(Boolean willingToRelocate) {
        this.willingToRelocate = willingToRelocate;
    }
}
