package com.se.skill4hire.dto.profile;

public class JobPreferencesDTO {
    private String jobType;
    private String expectedSalary;
    private Boolean willingToRelocate;

    public JobPreferencesDTO() {
        // Default constructor
    }

    public JobPreferencesDTO(String jobType, String expectedSalary, Boolean willingToRelocate) {
        this.jobType = jobType;
        this.expectedSalary = expectedSalary;
        this.willingToRelocate = willingToRelocate;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public Boolean getWillingToRelocate() {
        return willingToRelocate;
    }

    public void setWillingToRelocate(Boolean willingToRelocate) {
        this.willingToRelocate = willingToRelocate;
    }
}
