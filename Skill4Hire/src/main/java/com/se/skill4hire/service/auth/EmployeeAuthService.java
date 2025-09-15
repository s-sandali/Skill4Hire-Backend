package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.*;
import com.se.skill4hire.entity.Employee;
import com.se.skill4hire.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAuthService implements BaseAuthService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeAuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!(request instanceof EmployeeRegisterRequest)) {
            return new AuthResponse("Invalid registration request", false);
        }

        EmployeeRegisterRequest regRequest = (EmployeeRegisterRequest) request;

        // Check if email exists
        Employee existing = employeeRepository.findByEmail(regRequest.getEmail());
        if (existing != null) {
            return new AuthResponse("Email already registered", false);
        }

        // Create new employee
        Employee employee = new Employee();
        employee.setEmail(regRequest.getEmail());
        employee.setPassword(regRequest.getPassword()); // You may hash password if needed
        employee.setCompanyName(regRequest.getCompanyName());
        employee.setRole("EMPLOYEE"); // default role

        employeeRepository.save(employee);

        return new AuthResponse(
                "Employee registered successfully",
                true,
                employee.getId(),
                employee.getRole()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session) {
        if (!(request instanceof EmployeeLoginRequest)) {
            return new AuthResponse("Invalid login request", false);
        }

        EmployeeLoginRequest loginRequest = (EmployeeLoginRequest) request;

        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail());
        if (employee == null || !employee.getPassword().equals(loginRequest.getPassword())) {
            return new AuthResponse("Invalid email or password", false);
        }

        // Store userId and role in session
        session.setAttribute("userId", employee.getId());
        session.setAttribute("role", employee.getRole());

        return new AuthResponse(
                "Login successful",
                true,
                employee.getId(),
                employee.getRole()
        );
    }

    @Override
    public AuthResponse logout(HttpSession session) {
        session.invalidate();
        return new AuthResponse("Logged out successfully", true);
    }
}
