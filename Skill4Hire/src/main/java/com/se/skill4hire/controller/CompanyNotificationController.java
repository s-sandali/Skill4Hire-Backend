package com.se.skill4hire.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.se.skill4hire.entity.CompanyNotification;
import com.se.skill4hire.service.notification.CompanyNotificationService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/companies/notifications")
public class CompanyNotificationController {

    private final CompanyNotificationService companyNotificationService;

    public CompanyNotificationController(CompanyNotificationService companyNotificationService) {
        this.companyNotificationService = companyNotificationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<List<CompanyNotification>> listMyNotifications(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(companyNotificationService.listForCompany(companyId));
    }

    @GetMapping("/unread")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<List<CompanyNotification>> getUnreadNotifications(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(companyNotificationService.getUnread(companyId));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(401).build();
        }
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", companyNotificationService.getUnreadCount(companyId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId, HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(401).build();
        }
        companyNotificationService.markAsRead(notificationId, companyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> markAllAsRead(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(401).build();
        }
        companyNotificationService.markAllAsRead(companyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Void> deleteNotification(@PathVariable String notificationId, HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        if (companyId == null) {
            return ResponseEntity.status(401).build();
        }
        companyNotificationService.delete(notificationId, companyId);
        return ResponseEntity.ok().build();
    }
}
