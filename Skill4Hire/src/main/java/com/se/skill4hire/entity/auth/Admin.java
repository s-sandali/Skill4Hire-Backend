package com.se.skill4hire.entity.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {

    private String adminName;

    public Admin() {}

    public Admin(String email, String password, String adminName) {
        super(email, password);
        this.adminName = adminName;
    }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }
}