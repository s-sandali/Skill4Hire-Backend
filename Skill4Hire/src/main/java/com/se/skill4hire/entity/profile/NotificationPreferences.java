package com.se.skill4hire.entity.profile;

import jakarta.validation.constraints.Size;

public class NotificationPreferences {
    private Boolean emailNotifications = true;

    private Boolean smsNotifications = false;

    @Size(max = 500, message = "Notification preferences must be less than 500 characters")
    private String customPreferences;

    private Boolean emailAlerts = true;
    private Boolean inAppNotifications = true;

    public NotificationPreferences() {
        // Default constructor
    }

    public NotificationPreferences(Boolean emailNotifications, Boolean smsNotifications, String customPreferences, Boolean emailAlerts, Boolean inAppNotifications) {
        this.emailNotifications = emailNotifications;
        this.smsNotifications = smsNotifications;
        this.customPreferences = customPreferences;
        this.emailAlerts = emailAlerts;
        this.inAppNotifications = inAppNotifications;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public String getCustomPreferences() {
        return customPreferences;
    }

    public void setCustomPreferences(String customPreferences) {
        this.customPreferences = customPreferences;
    }

    public Boolean getEmailAlerts() {
        return emailAlerts;
    }

    public void setEmailAlerts(Boolean emailAlerts) {
        this.emailAlerts = emailAlerts;
    }

    public Boolean getInAppNotifications() {
        return inAppNotifications;
    }

    public void setInAppNotifications(Boolean inAppNotifications) {
        this.inAppNotifications = inAppNotifications;
    }
}
