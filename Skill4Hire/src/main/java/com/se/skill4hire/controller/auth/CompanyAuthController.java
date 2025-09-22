package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.CompanyLoginRequest;
import com.se.skill4hire.dto.auth.CompanyRegRequest;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.service.auth.CompanyAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies/auth")
@RequiredArgsConstructor
public class CompanyAuthController {

    private final CompanyAuthService companyAuthService;

    // Register
    @PostMapping("/register")
    public ResponseEntity<Company> register(@RequestBody CompanyRegRequest request) {
        Company company = companyAuthService.register(request);
        return ResponseEntity.ok(company);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<Company> login(@RequestBody CompanyLoginRequest request, HttpSession session) {
        Company company = companyAuthService.login(request, session);
        return ResponseEntity.ok(company);
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        companyAuthService.logout(session);
        return ResponseEntity.ok("Logged out successfully");
    }
}
