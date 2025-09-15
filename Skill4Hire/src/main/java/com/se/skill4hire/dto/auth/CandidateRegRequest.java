package com.se.skill4hire.dto.auth;

public class CandidateRegRequest extends RegisterRequest {
    private String firstName;
    private String lastName;

    public CandidateRegRequest() {}

    public CandidateRegRequest(String email, String password, String role, String firstName, String lastName) {
        super(email, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
