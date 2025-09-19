package com.se.skill4hire.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Employee extends User {

    private String companyName;

    public Employee() {}

    public Employee(String email, String password, String companyName) {
        super(email, password);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }
}
