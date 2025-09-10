package com.se.skill4hire.dto.auth;

public class CandidateLoginRequest extends LoginRequest {

    public CandidateLoginRequest() {}

    public CandidateLoginRequest(String email, String password) {
        super(email, password); // initialize base class fields
    }

}
