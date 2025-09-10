package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.CandidateRegisterRequest;
import com.se.skill4hire.dto.auth.CandidateLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    public AuthResponse registerCandidate(CandidateRegisterRequest request) {
        // TODO: Shadurshan: Add validation, hash password
        //  TODO: Himasha: save to DB
        return new AuthResponse("mock-jwt-token-123", 1L, request.getEmail(), request.getFirstName(), request.getLastName());
    }

    public AuthResponse loginCandidate(CandidateLoginRequest request) {
        // TODO: Shadurshan: Verify credentials, generate JWT
        return new AuthResponse("mock-jwt-token-123", 1L, request.getEmail(), "John", "Doe");
    }
}
