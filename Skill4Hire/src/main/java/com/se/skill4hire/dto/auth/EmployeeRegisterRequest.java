package com.se.skill4hire.dto.auth;

public class EmployeeRegisterRequest extends RegisterRequest {
    private String companyName;

    public EmployeeRegisterRequest() {
        super();
    }

    public EmployeeRegisterRequest(String email, String password, String role, String companyName) {
        super(email, password, role);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
