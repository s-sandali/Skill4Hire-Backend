package com.se.skill4hire.service.auth;


import com.se.skill4hire.dto.auth.CandidateRegisterRequest;
import com.se.skill4hire.dto.auth.CandidateLoginRequest;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.RegisterRequest;
import com.se.skill4hire.dto.auth.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class CandidateAuthService implements BaseAuthService {

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Cast to CandidateRegisterRequest
        CandidateRegisterRequest candidateRequest = (CandidateRegisterRequest) request;

        // TODO: Shadurshan: validate input, hash password
        // TODO: Himasha: save to DB

        return new AuthResponse(
                "mock-jwt-token-123",          // token
                1L,                             // id
                candidateRequest.getEmail(),    // email
                candidateRequest.getFirstName(),// first name
                candidateRequest.getLastName()  // last name
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Cast to CandidateLoginRequest
        CandidateLoginRequest candidateRequest = (CandidateLoginRequest) request;

        // TODO: Shadurshan: verify credentials, generate JWT

        return new AuthResponse(
                "mock-jwt-token-123",      // token
                1L,                        // id
                candidateRequest.getEmail(),// email
                "John",                    // first name placeholder
                "Doe"                      // last name placeholder
        );
    }
}
