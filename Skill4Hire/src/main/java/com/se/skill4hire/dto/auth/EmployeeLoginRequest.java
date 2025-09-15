package com.se.skill4hire.dto.auth;

public class EmployeeLoginRequest extends LoginRequest {

    public EmployeeLoginRequest() {
        super();
    }

    public EmployeeLoginRequest(String email, String password) {
        super(email, password);
    }
}
