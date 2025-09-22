package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.AdminRegRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.AdminLoginRequest;
import com.se.skill4hire.service.auth.AdminAuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AdminRegRequest request) {
        AuthResponse response = adminAuthService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AdminLoginRequest request, HttpSession session) {
        AuthResponse response = adminAuthService.login(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = adminAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }

        // Verify the user has ADMIN role
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(new AuthResponse("Access denied - not an admin", false));
        }

        return ResponseEntity.ok(new AuthResponse("You are logged in as admin ID: " + userId, true, userId, role));
    }
}