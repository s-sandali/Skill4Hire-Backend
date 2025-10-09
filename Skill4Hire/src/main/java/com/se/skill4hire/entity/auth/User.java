package com.se.skill4hire.entity.auth;

import org.springframework.data.annotation.Id;

public abstract class User {

    @Id
    private String id;

    private String email;
    private String password;

    private String role; // CANDIDATE, COMPANY, EMPLOYEE

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) {
        // Validate role
        if (isValidRole(role)) {
            this.role = role.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    private boolean isValidRole(String role) {
        return role != null &&
                (role.equalsIgnoreCase("CANDIDATE") ||
                        role.equalsIgnoreCase("COMPANY") ||
                        role.equalsIgnoreCase("EMPLOYEE"));
    }
}
