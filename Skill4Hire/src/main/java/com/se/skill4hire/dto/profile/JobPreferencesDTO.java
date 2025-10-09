package com.se.skill4hire.dto.profile;

public class JobPreferencesDTO {
    private String preferredRole;
    private String preferredLocation;
    private Double expectedSalary;
    private String jobType;
    private Boolean willingToRelocate;

    public JobPreferencesDTO() {
        // Default constructor
    }

    public JobPreferencesDTO(String preferredRole, String preferredLocation, Double expectedSalary, String jobType, Boolean willingToRelocate) {
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
