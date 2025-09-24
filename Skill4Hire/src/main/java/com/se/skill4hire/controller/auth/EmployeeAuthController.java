package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.EmployeeRegisterRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.EmployeeLoginRequest;
import com.se.skill4hire.service.auth.EmployeeAuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees/auth")
public class EmployeeAuthController {

    private final EmployeeAuthService employeeAuthService;

    public EmployeeAuthController(EmployeeAuthService employeeAuthService) {
        this.employeeAuthService = employeeAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody EmployeeRegisterRequest request) {
        AuthResponse response = employeeAuthService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody EmployeeLoginRequest request, HttpSession session) {
        AuthResponse response = employeeAuthService.login(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = employeeAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentEmployee(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }
        return ResponseEntity.ok(new AuthResponse("You are logged in as employee ID: " + userId, true));
    }
}