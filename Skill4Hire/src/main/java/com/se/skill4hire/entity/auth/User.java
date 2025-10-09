package com.se.skill4hire.entity.auth;

import jakarta.persistence.*;

import java.time.Instant;

@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Column(nullable = false)
    private String role; // CANDIDATE, COMPANY, EMPLOYEE

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getId() { return id; }

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
