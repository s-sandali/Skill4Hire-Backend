package com.se.skill4hire.dto.auth;

public class CompanyLoginRequest extends LoginRequest {

    public CompanyLoginRequest() {}

    public CompanyLoginRequest(String email, String password) {
        super(email, password);
    }
}
