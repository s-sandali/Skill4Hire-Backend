package com.se.skill4hire.entity;

import jakarta.persistence.Entity;

@Entity
public class Candidate extends User {

    private String name;

    public Candidate() {}

    public Candidate(String email, String password, String name) {
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
