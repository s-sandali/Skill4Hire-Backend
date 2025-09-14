package com.se.skill4hire.dto.auth;

import com.se.skill4hire.dto.auth.RegisterRequest;

public class CandidateRegRequest extends RegisterRequest {
    private String name;

    public CandidateRegRequest() {}

    public CandidateRegRequest(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    // Getter and setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
