package com.se.skill4hire.dto.auth;

public class CandidateLoginRequest {
    private String email;
    private String password;

    public CandidateLoginRequest() {}

    public CandidateLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
