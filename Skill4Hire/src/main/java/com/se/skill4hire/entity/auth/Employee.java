package com.se.skill4hire.entity.auth;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class Employee extends User {

    private String name;
    private String department;

    public Employee() {}

    public Employee(String email, String password, String name, String department) {
        super(email, password);
        this.name = name;
        this.department = department;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }

    public Long getId() { return super.getId(); }
}