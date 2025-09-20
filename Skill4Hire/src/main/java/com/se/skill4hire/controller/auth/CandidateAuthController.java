package com.se.skill4hire.controller.auth;

import com.se.skill4hire.dto.auth.CandidateRegRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.CandidateLoginRequest;
import com.se.skill4hire.service.auth.CandidateAuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates/auth")
public class CandidateAuthController {

    private final CandidateAuthService candidateAuthService;

    public CandidateAuthController(CandidateAuthService candidateAuthService) {
        this.candidateAuthService = candidateAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CandidateRegRequest request) {
        AuthResponse response = candidateAuthService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody CandidateLoginRequest request, HttpSession session) {
        AuthResponse response = candidateAuthService.login(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        AuthResponse response = candidateAuthService.logout(session);
        return ResponseEntity.ok(response);
    }

    // Example protected endpoint
    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentCandidate(HttpSession session) {
        Object candidateId = session.getAttribute("candidateId");
        if (candidateId == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Not logged in", false));
        }
        return ResponseEntity.ok(new AuthResponse("You are logged in as candidate ID: " + candidateId, true));
    }
}
