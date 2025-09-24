package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.UnifiedLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.service.auth.UnifiedAuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UnifiedAuthController {

    private final UnifiedAuthService unifiedAuthService;

    public UnifiedAuthController(UnifiedAuthService unifiedAuthService) {
        this.unifiedAuthService = unifiedAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> unifiedLogin(@RequestBody UnifiedLoginRequest request, HttpSession session) {
        AuthResponse response = unifiedAuthService.unifiedLogin(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> unifiedLogout(HttpSession session) {
        AuthResponse response = unifiedAuthService.unifiedLogout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpSession session) {
        AuthResponse response = unifiedAuthService.getCurrentUser(session);
        return ResponseEntity.ok(response);
    }
}