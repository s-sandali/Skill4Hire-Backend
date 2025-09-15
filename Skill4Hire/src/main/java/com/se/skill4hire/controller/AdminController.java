package com.se.skill4hire.controller;


import com.se.skill4hire.dto.profile.AdminProfileDTO;
import com.se.skill4hire.entity.AdminProfile;
import com.se.skill4hire.service.profile.AdminProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminProfileService adminProfileService;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AdminProfileDTO dto) {
        try {
            AdminProfile savedAdmin = adminProfileService.registerAdmin(dto);
            return ResponseEntity.ok("Admin registered successfully with ID: " + savedAdmin.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminProfileDTO dto) {
        boolean success = adminProfileService.loginAdmin(dto);
        if (success) {
            return ResponseEntity.ok("Login successful! Welcome to the admin dashboard.");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
