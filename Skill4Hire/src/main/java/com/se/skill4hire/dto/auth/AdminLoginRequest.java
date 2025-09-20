package com.se.skill4hire.dto.auth;

public class AdminLoginRequest extends LoginRequest {

    public AdminLoginRequest() {}

    public AdminLoginRequest(String email, String password) {
        super(email, password);
    }
}