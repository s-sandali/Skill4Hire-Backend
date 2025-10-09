package com.se.skill4hire.entity.auth;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
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
}