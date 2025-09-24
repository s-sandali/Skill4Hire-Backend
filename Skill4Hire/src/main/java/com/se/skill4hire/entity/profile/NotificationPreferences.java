// NotificationPreferences.java
package com.se.skill4hire.entity.profile;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferences {
    private Boolean emailAlerts = true;
    private Boolean inAppNotifications = true;
}