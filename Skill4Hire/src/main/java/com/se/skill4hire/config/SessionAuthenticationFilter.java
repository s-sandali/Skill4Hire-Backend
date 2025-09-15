package com.se.skill4hire.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    // List of public endpoints that don't require authentication
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/candidates/auth/register",
            "/api/candidates/auth/login",
            "/api/companies/auth/register",
            "/api/companies/auth/login",
            "/api/employees/auth/register",
            "/api/employees/auth/login",
            "/api/admin/auth/register",
            "/api/admin/auth/login",
            "/api/auth/login",
            "/api/auth/logout",
            "/h2-console",
            "/h2-console/",
            "/h2-console/**",
            "/error"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("SessionAuthenticationFilter processing: " + requestURI);

        // Skip authentication for public endpoints
        if (isPublicEndpoint(requestURI)) {
            System.out.println("Public endpoint, skipping authentication: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            String role = (String) session.getAttribute("role");

            if (userId != null && role != null) {
                // Create authentication token with proper role authority
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId.toString(),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authenticated user: " + userId + " with role: " + role);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestURI) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(endpoint -> {
            if (endpoint.endsWith("/**")) {
                String basePattern = endpoint.substring(0, endpoint.length() - 3);
                return requestURI.startsWith(basePattern);
            }
            return requestURI.equals(endpoint);
        });
    }
}