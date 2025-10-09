package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.CompanyRegRequest;
import com.se.skill4hire.dto.auth.CompanyLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.service.auth.CompanyAuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies/auth")
public class CompanyAuthController {

    private final CompanyAuthService companyAuthService;

    public CompanyAuthController(CompanyAuthService companyAuthService) {
        this.companyAuthService = companyAuthService;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CompanyRegRequest request) {
        AuthResponse response = companyAuthService.register(request);
        return ResponseEntity.ok(response);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody CompanyLoginRequest request, HttpSession session) {
        AuthResponse response = companyAuthService.login(request, session);
        return ResponseEntity.ok(response);
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = companyAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    // Get current logged-in company (protected endpoint)
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentCompany(HttpSession session) {

        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }

        // Verify the user has COMPANY role
        if (!"COMPANY".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(new AuthResponse("Access denied - not a company", false));
        }

        return ResponseEntity.ok(new AuthResponse("You are logged in as company ID: " + userId, true, userId, role));
    }
}
