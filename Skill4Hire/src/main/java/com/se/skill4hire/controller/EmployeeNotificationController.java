package com.se.skill4hire.controller;

import com.se.skill4hire.entity.EmployeeNotification;
import com.se.skill4hire.service.notification.EmployeeNotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/employees/notifications", "/api/employee/notifications"})
public class EmployeeNotificationController {

    private final EmployeeNotificationService employeeNotificationService;

    public EmployeeNotificationController(EmployeeNotificationService employeeNotificationService) {
        this.employeeNotificationService = employeeNotificationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> listMyNotifications(@RequestParam(value = "page", required = false) Integer page,
                                                 @RequestParam(value = "limit", required = false) Integer limit,
                                                 HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }
        if (page != null && limit != null && page >= 0 && limit > 0) {
            Page<EmployeeNotification> paged = employeeNotificationService.listForEmployeePaged(employeeId, page, limit);
            Map<String, Object> body = new HashMap<>();
            body.put("content", paged.getContent());
            body.put("page", paged.getNumber());
            body.put("limit", paged.getSize());
            body.put("totalElements", paged.getTotalElements());
            body.put("totalPages", paged.getTotalPages());
            return ResponseEntity.ok(body);
        } else {
            List<EmployeeNotification> list = employeeNotificationService.listForEmployee(employeeId);
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("/unread")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<EmployeeNotification>> getUnreadNotifications(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(employeeNotificationService.getUnreadNotifications(employeeId));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }

        long count = employeeNotificationService.getUnreadCount(employeeId);
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{notificationId}/read")
    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> markAsRead(@PathVariable String notificationId, HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }

        employeeNotificationService.markAsRead(notificationId, employeeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> markAllAsRead(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }

        employeeNotificationService.markAllAsRead(employeeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> deleteNotification(@PathVariable String notificationId, HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }

        employeeNotificationService.deleteNotification(notificationId, employeeId);
        return ResponseEntity.ok().build();
    }

    // Simple debug endpoint to seed a sample notification for the current employee
    @PostMapping("/seed")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<EmployeeNotification> seedOne(HttpSession session) {
        String employeeId = (String) session.getAttribute("userId");
        if (employeeId == null) {
            return ResponseEntity.status(401).build();
        }
        EmployeeNotification created = employeeNotificationService.createSampleForEmployee(employeeId);
        return ResponseEntity.ok(created);
    }
}