package com.se.skill4hire.dto.auth;

public class AdminRegRequest extends RegisterRequest {
    private String adminName;

    public AdminRegRequest() {}

    public AdminRegRequest(String email, String password, String role, String adminName) {
        super(email, password, role);
        this.adminName = adminName;
    }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
}