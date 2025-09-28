package com.se.skill4hire.entity.profile;

import jakarta.persistence.Embeddable;

@Embeddable
public class NotificationPreferences {
    private Boolean emailAlerts = true;
    private Boolean inAppNotifications = true;

    public NotificationPreferences() {
        // Default constructor
    }

    public NotificationPreferences(Boolean emailAlerts, Boolean inAppNotifications) {
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
