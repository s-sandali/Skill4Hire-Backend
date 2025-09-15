package com.se.skill4hire.dto.auth;

public class AdminRegRequest extends RegisterRequest {
    private String fullName;

    public AdminRegRequest() {}

    public AdminRegRequest(String email, String password, String role, String fullName) {
        super(email, password, role);
        this.fullName = fullName;
    }

    // Getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}