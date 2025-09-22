package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.CompanyLoginRequest;
import com.se.skill4hire.dto.auth.CompanyRegRequest;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyAuthService {

    private final CompanyAuthRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new company
    public Company register(CompanyRegRequest request) {
        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Company company = new Company(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getDescription(),
                request.getPhone(),
                request.getWebsite(),
                request.getAddress(),
                request.getFacebook(),
                request.getLinkedin(),
                request.getTwitter()
        );
        company.setRole("COMPANY");

        return companyRepository.save(company);
    }

    // Login and set session
    public Company login(CompanyLoginRequest request, HttpSession session) {
        Company company = companyRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Store session info
        session.setAttribute("userId", company.getId());
        session.setAttribute("role", company.getRole());

        return company;
    }

    // Logout
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
