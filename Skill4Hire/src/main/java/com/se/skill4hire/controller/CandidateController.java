package com.se.skill4hire.controller;

import com.se.skill4hire.dto.auth.CandidateRegisterRequest;
import com.se.skill4hire.dto.auth.CandidateLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.service.auth.CandidateAuthService;
import com.se.skill4hire.service.auth.CandidateAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateAuthService candidateService;

    public CandidateController(CandidateAuthService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerCandidate(@RequestBody CandidateRegisterRequest request) {
        AuthResponse response = candidateService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginCandidate(@RequestBody CandidateLoginRequest request) {
        AuthResponse response = candidateService.login(request);
        return ResponseEntity.ok(response);
    }
}
