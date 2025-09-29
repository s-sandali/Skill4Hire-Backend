package com.se.skill4hire.dto.profile;

public class NotificationPreferencesDTO {
    private Boolean emailAlerts;
    private Boolean inAppNotifications;

    public NotificationPreferencesDTO() {
        // Default constructor
    }

    public NotificationPreferencesDTO(Boolean emailAlerts, Boolean inAppNotifications) {
        this.emailAlerts = emailAlerts;
        this.inAppNotifications = inAppNotifications;
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
