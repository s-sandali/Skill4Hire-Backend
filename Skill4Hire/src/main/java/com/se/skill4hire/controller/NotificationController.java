package com.se.skill4hire.controller;

import com.se.skill4hire.dto.notification.SystemNotificationRequest;
import com.se.skill4hire.entity.SystemNotification;
import com.se.skill4hire.repository.SystemNotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SystemNotificationRepository repository;

    public NotificationController(SystemNotificationRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<SystemNotification> create(@RequestBody SystemNotificationRequest req) {
        SystemNotification n = new SystemNotification();
        n.setTitle(req.getTitle());
        n.setMessage(req.getMessage());
        n.setType(req.parseTypeOrDefault());
        n.setUserId(req.getUserId());
        SystemNotification saved = repository.save(n);
        return ResponseEntity.ok(saved);
    }
}

