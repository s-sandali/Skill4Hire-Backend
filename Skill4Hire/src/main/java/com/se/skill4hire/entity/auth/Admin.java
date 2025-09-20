package com.se.skill4hire.entity.auth;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Admin extends User {

    private String fullName;

    public Admin() {}

    public Admin(String email, String password, String fullName) {
        super(email, password);
        this.fullName = fullName;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }
}