package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.*;
import com.se.skill4hire.entity.auth.Admin;
import com.se.skill4hire.repository.auth.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService implements BaseAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthService(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!(request instanceof AdminRegRequest)) {
            return new AuthResponse("Invalid registration request", false);
        }

        AdminRegRequest regRequest = (AdminRegRequest) request;

        // Check if email exists
        if (adminRepository.findByEmail(regRequest.getEmail()) != null) {
            return new AuthResponse("Email already registered", false);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(regRequest.getPassword());

        // Create admin - AUTO-SET ROLE TO "ADMIN"
        Admin admin = new Admin(
                regRequest.getEmail(),
                hashedPassword,
                regRequest.getAdminName()
        );
        admin.setRole("ADMIN"); // Force set to ADMIN

        adminRepository.save(admin);

        return new AuthResponse(
                "Admin registered successfully",
                true,
                admin.getId(),
                admin.getRole()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        if (!(request instanceof AdminLoginRequest)) {
            return new AuthResponse("Invalid login request", false);
        }

        AdminLoginRequest loginRequest = (AdminLoginRequest) request;

        Admin admin = adminRepository.findByEmail(loginRequest.getEmail());
        if (admin == null || !passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store userId and role in session
        session.setAttribute("userId", admin.getId());
        session.setAttribute("role", admin.getRole());

        return new AuthResponse(
                "Login successful",
                true,
                admin.getId(),
                admin.getRole()
        );
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }
}