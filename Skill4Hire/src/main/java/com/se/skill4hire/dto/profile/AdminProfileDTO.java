package com.se.skill4hire.dto.profile;

public class AdminProfileDTO {

    private String username;
    private String email;
    private String password;

    public AdminProfileDTO() {}

    public AdminProfileDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
