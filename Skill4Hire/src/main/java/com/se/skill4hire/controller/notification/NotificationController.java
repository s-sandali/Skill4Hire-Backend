package com.se.skill4hire.controller.notification;

import com.se.skill4hire.entity.AppNotification;
import com.se.skill4hire.service.notification.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<List<AppNotification>> listMyNotifications(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        if (candidateId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(notificationService.listForUser(candidateId));
    }
}

