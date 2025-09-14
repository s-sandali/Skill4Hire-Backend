package com.se.skill4hire.dto.auth;

public class AuthResponse {
    private String message;
    private boolean success;
    private Long id;
    private String role;  

    public AuthResponse() {}

    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public AuthResponse(String message, boolean success, Long id, String role) {
        this.message = message;
        this.success = success;
        this.id = id;
        this.role = role;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
