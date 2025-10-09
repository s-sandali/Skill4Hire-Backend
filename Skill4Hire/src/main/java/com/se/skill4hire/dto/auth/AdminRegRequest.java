package com.se.skill4hire.dto.auth;

public class AdminRegRequest extends RegisterRequest {
    public AdminRegRequest() { super(); }
    public AdminRegRequest(String email, String password, String role) {
        super(email, password, role);
    }
}