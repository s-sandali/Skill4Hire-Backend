package com.se.skill4hire.dto.profile;

public class ProfileCompletenessDTO {
    private double completenessPercentage; // CHANGED FROM 'completeness'
    private String message;
    
    public double getCompletenessPercentage() {
        return completenessPercentage;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setCompletenessPercentage(double completenessPercentage) {
        this.completenessPercentage = completenessPercentage;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public ProfileCompletenessDTO() {}
    
    public ProfileCompletenessDTO(double completenessPercentage, String message) {
        this.completenessPercentage = completenessPercentage;
        this.message = message;
    }
}