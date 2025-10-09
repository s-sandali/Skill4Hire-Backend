package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.UnifiedLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.entity.auth.Candidate;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.entity.auth.Employee;
import com.se.skill4hire.repository.auth.CandidateAuthRepository;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import com.se.skill4hire.repository.auth.EmployeeRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UnifiedAuthService {

    private final CandidateAuthRepository candidateAuthRepository;
    private final CompanyAuthRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UnifiedAuthService(CandidateAuthRepository candidateAuthRepository,
                              CompanyAuthRepository companyRepository,
                              EmployeeRepository employeeRepository,
                              PasswordEncoder passwordEncoder) {
        this.candidateAuthRepository = candidateAuthRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse unifiedLogin(UnifiedLoginRequest request, HttpSession session) {
        // Check candidates
        Candidate candidate = candidateAuthRepository.findByEmail(request.getEmail());
        if (candidate != null && passwordEncoder.matches(request.getPassword(), candidate.getPassword())) {
            session.setAttribute("userId", candidate.getId());
            session.setAttribute("role", candidate.getRole());
            return new AuthResponse("Login successful", true, candidate.getId(), candidate.getRole());
        }

        // Check companies
        Company company = companyRepository.findByEmail(request.getEmail()).orElse(null);
        if (company != null && passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            session.setAttribute("userId", company.getId());
            session.setAttribute("role", company.getRole());
            return new AuthResponse("Login successful", true, company.getId(), company.getRole());
        }

        // Check employees
        Employee employee = employeeRepository.findByEmail(request.getEmail());
        if (employee != null && passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            session.setAttribute("userId", employee.getId());
            session.setAttribute("role", employee.getRole());
            return new AuthResponse("Login successful", true, employee.getId(), employee.getRole());
        }



        return new AuthResponse("Invalid credentials", false);
    }

    public AuthResponse unifiedLogout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logout successful", true);
    }

    public AuthResponse getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId != null && role != null) {
            return new AuthResponse("User is logged in", true, userId, role);
        }

        return new AuthResponse("No user logged in", false);
    }
}