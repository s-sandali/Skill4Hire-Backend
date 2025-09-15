package com.se.skill4hire.controller;

import com.se.skill4hire.dto.AuthResponse;
import com.se.skill4hire.dto.EmployeeLoginRequest;
import com.se.skill4hire.dto.EmployeeRegisterRequest;
import com.se.skill4hire.service.EmployeeAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees/auth")
@CrossOrigin
public class EmployeeAuthController {

    private final EmployeeAuthService service;

    public EmployeeAuthController(EmployeeAuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody EmployeeRegisterRequest req){
        return ResponseEntity.ok(service.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody EmployeeLoginRequest req){
        return ResponseEntity.ok(service.login(req));
    }
}
