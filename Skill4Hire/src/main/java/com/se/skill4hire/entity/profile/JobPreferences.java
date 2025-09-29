package com.se.skill4hire.entity.profile;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

@Embeddable
public class JobPreferences {
    @Size(max = 50, message = "Job type must be less than 50 characters")
    private String jobType;

    @Size(max = 100, message = "Expected salary must be less than 100 characters")
    private String expectedSalary;

    private Boolean willingToRelocate = false;

    public JobPreferences() {
        // Default constructor
    }

    public JobPreferences(String jobType, String expectedSalary, Boolean willingToRelocate) {
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
