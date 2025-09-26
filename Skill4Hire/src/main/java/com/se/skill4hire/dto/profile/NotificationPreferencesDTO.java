package com.se.skill4hire.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferencesDTO {
    private Boolean emailAlerts = true;
    private Boolean inAppNotifications = true;
}