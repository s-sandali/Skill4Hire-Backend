package com.se.skill4hire.dto.auth;

public class EmployeeRegisterRequest extends RegisterRequest {
    private String name;
    private String department;

    public EmployeeRegisterRequest() {}

    public EmployeeRegisterRequest(String email, String password, String role, String name, String department) {
        super(email, password, role);
        this.name = name;
        this.department = department;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}