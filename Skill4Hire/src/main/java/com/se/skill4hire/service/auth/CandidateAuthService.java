package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.CandidateRegRequest;
import com.se.skill4hire.dto.auth.CandidateLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.RegisterRequest;
import com.se.skill4hire.dto.auth.LoginRequest;
import com.se.skill4hire.entity.Candidate;
import com.se.skill4hire.repository.CandidateRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateAuthService implements BaseAuthService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateAuthService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!(request instanceof CandidateRegRequest)) {
            return new AuthResponse("Invalid registration request", false);
        }

        CandidateRegRequest regRequest = (CandidateRegRequest) request;

        // Check if email exists
        Candidate existing = candidateRepository.findByEmail(regRequest.getEmail());
        if (existing != null) {
            return new AuthResponse("Email already registered", false);
        }
        String fullName = regRequest.getFirstName() + " " + regRequest.getLastName();


        // Create new candidate and set role dynamically
        Candidate candidate = new Candidate(
                regRequest.getEmail(),
                regRequest.getPassword(),
                fullName
        );
        candidate.setRole(regRequest.getRole());

        candidateRepository.save(candidate);

        return new AuthResponse(
                "Candidate registered successfully",
                true,
                candidate.getId(),
                candidate.getRole()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        if (!(request instanceof CandidateLoginRequest)) {
            return new AuthResponse("Invalid login request", false);
        }

        CandidateLoginRequest loginRequest = (CandidateLoginRequest) request;

        Candidate candidate = candidateRepository.findByEmail(loginRequest.getEmail());
        if (candidate == null || !candidate.getPassword().equals(loginRequest.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store userId and role in session for role-based access
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
