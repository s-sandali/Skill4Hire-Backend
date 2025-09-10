package com.se.skill4hire.service.auth;

import com.se.skill4hire.dto.auth.AuthResponse;
import com.se.skill4hire.dto.auth.RegisterRequest;
import com.se.skill4hire.dto.auth.LoginRequest;

public interface BaseAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
