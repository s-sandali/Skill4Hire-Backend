package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.AdminLoginRequest;
import com.se.skill4hire.dto.auth.AdminRegRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.service.auth.AdminAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    @Autowired
    private AdminAuthService adminAuthService;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AdminRegRequest request) {
        AuthResponse response = adminAuthService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AdminLoginRequest request, HttpSession session) {
        AuthResponse response = adminAuthService.login(request, session);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = adminAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    // Get current user endpoint
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId != null && role != null) {
            AuthResponse response = new AuthResponse("User is logged in", true, userId, role);
            return ResponseEntity.ok(response);
        } else {
            AuthResponse response = new AuthResponse("No user logged in", false);
            return ResponseEntity.status(401).body(response);
        }
    }
}