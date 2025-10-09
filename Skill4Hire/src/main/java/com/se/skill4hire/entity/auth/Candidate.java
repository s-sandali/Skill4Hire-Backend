package com.se.skill4hire.entity.auth;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidates")
@AttributeOverride(name = "id", column = @Column(name = "id"))

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

    public Long getId() { return super.getId(); }
}
