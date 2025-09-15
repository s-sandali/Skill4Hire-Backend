package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.LoginRequest;

import com.se.skill4hire.dto.auth.RegisterRequest;
import jakarta.servlet.http.HttpSession;

public interface BaseAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request, HttpSession session);
    AuthResponse logout(HttpSession session);
}