package com.se.skill4hire.employee.service;

import com.se.skill4hire.dto.AuthResponse;
import com.se.skill4hire.dto.EmployeeLoginRequest;
import com.se.skill4hire.dto.EmployeeRegisterRequest;
import com.se.skill4hire.entity.Employee;
import com.se.skill4hire.exception.ApiException;
import com.se.skill4hire.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeAuthService {

    private final EmployeeRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public EmployeeAuthService(EmployeeRepository repo, PasswordEncoder encoder, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public AuthResponse register(EmployeeRegisterRequest req) {
        if (repo.existsByEmail(req.getEmail())) {
            throw new ApiException("Email already registered", HttpStatus.CONFLICT);
        }
        Employee e = new Employee();
        e.setFirstName(req.getFirstName());
        e.setLastName(req.getLastName());
        e.setEmail(req.getEmail());
        e.setPasswordHash(encoder.encode(req.getPassword()));
        e.setPhone(req.getPhone());
        e.setDepartment(req.getDepartment());
        e.setRoleTitle(req.getRoleTitle());
        Employee saved = repo.save(e);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", saved.getId());
        claims.put("type", "EMPLOYEE");
        String token = jwt.generate(saved.getEmail(), claims);
        return new AuthResponse(token, saved.getId(), saved.getEmail(),
                saved.getFirstName(), saved.getLastName());
    }

    public AuthResponse login(EmployeeLoginRequest req) {
        Employee e = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED));
        if (!encoder.matches(req.getPassword(), e.getPasswordHash())) {
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", e.getId());
        claims.put("type", "EMPLOYEE");
        String token = jwt.generate(e.getEmail(), claims);
        return new AuthResponse(token, e.getId(), e.getEmail(),
                e.getFirstName(), e.getLastName());
    }
}
