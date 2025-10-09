package com.se.skill4hire.dto.auth;

public class AuthResponse {
    private String message;
    private boolean success;
    private String id;
    private String role;
    private String email;

    public AuthResponse() {}

    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public AuthResponse(String message, boolean success, String id, String role) {
        this.message = message;
        this.success = success;
        this.id = id;
        this.role = role;
    }

    public AuthResponse(String message, boolean success, String id, String role, String email) {
        this.message = message;
        this.success = success;
        this.id = id;
        this.role = role;
        this.email = email;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}