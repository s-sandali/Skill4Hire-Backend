package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.*;
import com.se.skill4hire.entity.auth.Candidate;
import com.se.skill4hire.repository.auth.CandidateAuthRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CandidateAuthService implements BaseAuthService {

    private final CandidateAuthRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    public CandidateAuthService(CandidateAuthRepository candidateRepository,
                                PasswordEncoder passwordEncoder) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!(request instanceof CandidateRegRequest)) {
            return new AuthResponse("Invalid registration request", false);
        }

        CandidateRegRequest regRequest = (CandidateRegRequest) request;

        // Check if email exists
        if (candidateRepository.findByEmail(regRequest.getEmail()) != null) {
            return new AuthResponse("Email already registered", false);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(regRequest.getPassword());

        // Full name
        String fullName = regRequest.getFirstName() + " " + regRequest.getLastName();

        // Create candidate - AUTO-SET ROLE TO "CANDIDATE"
        Candidate candidate = new Candidate(
                regRequest.getEmail(),
                hashedPassword,
                fullName
        );
        candidate.setRole("CANDIDATE"); // Force set to CANDIDATE

        candidateRepository.save(candidate);

        return new AuthResponse(
                "Candidate registered successfully",
                true,
                candidate.getId(),
                candidate.getRole()
        );
    }

    // Remove or modify the role validation
    private boolean isValidCandidateRole(String role) {
        // Allow null/empty roles since we auto-set them
        return role == null || role.isEmpty() ||
                "CANDIDATE".equalsIgnoreCase(role) ||
                "ADMIN".equalsIgnoreCase(role);
    }
    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        if (!(request instanceof CandidateLoginRequest)) {
            return new AuthResponse("Invalid login request", false);
        }

        CandidateLoginRequest loginRequest = (CandidateLoginRequest) request;

        Candidate candidate = candidateRepository.findByEmail(loginRequest.getEmail());
        if (candidate == null || !passwordEncoder.matches(loginRequest.getPassword(), candidate.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store userId and role in session
        session.setAttribute("userId", candidate.getId());
        session.setAttribute("role", candidate.getRole());

        return new AuthResponse(
                "Login successful",
                true,
                candidate.getId(),
                candidate.getRole()
        );
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }


}
