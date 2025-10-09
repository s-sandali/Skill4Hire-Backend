package com.se.skill4hire.entity.auth;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "candidates")
public class Candidate extends User {

    private String name;

    public Candidate() {}

    public Candidate(String email, String password, String name) {
        super(email, password);
        this.name = name;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public void setRole(String role) {
        super.setRole(role); // store role in User
    }
}
