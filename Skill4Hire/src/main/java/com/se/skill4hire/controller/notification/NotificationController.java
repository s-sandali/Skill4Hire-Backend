package com.se.skill4hire.controller.notification;

import com.se.skill4hire.entity.AppNotification;
import com.se.skill4hire.service.notification.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        if (candidateId == null) {
            return ResponseEntity.status(401).build();
        }
        int count = notificationService.getUnreadCount(candidateId);
        Map<String, Integer> resp = new HashMap<>();
        resp.put("unreadCount", count);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Void> markAsRead(@PathVariable("id") String id, HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        if (candidateId == null) {
            return ResponseEntity.status(401).build();
        }
        notificationService.markAsRead(candidateId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/read-all")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public ResponseEntity<Void> markAllAsRead(HttpSession session) {
        String candidateId = (String) session.getAttribute("userId");
        if (candidateId == null) {
            return ResponseEntity.status(401).build();
        }
        notificationService.markAllAsRead(candidateId);
        return ResponseEntity.noContent().build();
    }
}
