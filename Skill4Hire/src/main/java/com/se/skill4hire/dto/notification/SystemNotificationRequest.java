package com.se.skill4hire.dto.notification;

import com.se.skill4hire.entity.SystemNotification;

public class SystemNotificationRequest {
    private String title;
    private String message;
    private String type; // Expect values like "SYSTEM" or "USER"
    private Long userId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public SystemNotification.Type parseTypeOrDefault() {
        try {
            return SystemNotification.Type.valueOf(type == null ? "SYSTEM" : type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return SystemNotification.Type.SYSTEM;
        }
    }
}

