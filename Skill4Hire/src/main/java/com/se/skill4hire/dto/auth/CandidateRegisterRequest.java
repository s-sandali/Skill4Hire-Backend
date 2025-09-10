package com.se.skill4hire.dto.auth;

public class CandidateRegisterRequest extends RegisterRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public CandidateRegisterRequest() {}

    public CandidateRegisterRequest(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password); // initialize base class fields
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
