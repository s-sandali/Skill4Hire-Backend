package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.*;
import com.se.skill4hire.entity.Employee;
import com.se.skill4hire.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAuthService implements BaseAuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeAuthService(EmployeeRepository employeeRepository,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!(request instanceof EmployeeRegisterRequest)) {
            return new AuthResponse("Invalid registration request", false);
        }

        EmployeeRegisterRequest regRequest = (EmployeeRegisterRequest) request;

        // Check if email exists
        if (employeeRepository.findByEmail(regRequest.getEmail()) != null) {
            return new AuthResponse("Email already registered", false);
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(regRequest.getPassword());

        // Create employee - AUTO-SET ROLE TO "EMPLOYEE"
        Employee employee = new Employee(
                regRequest.getEmail(),
                hashedPassword,
                regRequest.getName(),
                regRequest.getDepartment()
        );
        employee.setRole("EMPLOYEE"); // Force set to EMPLOYEE

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
        if (employee == null || !passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
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