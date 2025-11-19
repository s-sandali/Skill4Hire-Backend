package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.*;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.service.notification.EmployeeNotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyAuthService {

    private final CompanyAuthRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeNotificationService employeeNotificationService;

    @Autowired
    public CompanyAuthService(CompanyAuthRepository companyRepository,
                              PasswordEncoder passwordEncoder,
                              EmployeeNotificationService employeeNotificationService) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeNotificationService = employeeNotificationService;
    }

    // Register
    public AuthResponse register(CompanyRegRequest request) {
        if (companyRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse("Email already registered", false);
        }

        Company company = new Company();
        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setPassword(passwordEncoder.encode(request.getPassword()));
        company.setRole("COMPANY");

        companyRepository.save(company);

        // Notify employees of new company registration
        try { employeeNotificationService.notifyCompanyRegistered(company.getId()); } catch (Exception ignored) {}

        return new AuthResponse(
                "Company registered successfully",
                true,
                company.getId(),
                company.getRole(),
                company.getEmail()  // Include email for frontend
        );
    }

    // Login
    public AuthResponse login(CompanyLoginRequest request, HttpSession session) {
        System.out.println("Login attempt email: " + request.getEmail());
        System.out.println("Login attempt password: " + request.getPassword());

        Company company = companyRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (company == null) {
            System.out.println("Company not found for email: " + request.getEmail());
            return new AuthResponse("Invalid email or password", false);
        }

        System.out.println("Password in DB (hashed): " + company.getPassword());

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), company.getPassword());
        System.out.println("Password matches? " + passwordMatches);

        if (!passwordMatches) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store user details in session
        session.setAttribute("userId", company.getId());
        session.setAttribute("role", company.getRole());

        return new AuthResponse(
                "Login successful",
                true,
                company.getId(),
                company.getRole(),
                company.getEmail()  // Include email for frontend
        );
    }

    // Logout
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }
}
