package com.se.skill4hire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - authentication
                        .requestMatchers(
                                "/api/candidates/auth/register",
                                "/api/candidates/auth/login",
                                "/api/companies/auth/register",
                                "/api/companies/auth/login",
                                "/api/employees/auth/register",
                                "/api/employees/auth/login",
                                "/api/admin/auth/register",
                                "/api/admin/auth/login",
                                "/h2-console/**",
                                "/h2-console",
                                "/error"
                        ).permitAll()

                        // Role-specific endpoints
                        .requestMatchers("/api/candidates/**").hasAnyRole("CANDIDATE", "ADMIN")
                        .requestMatchers("/api/companies/**").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/employees/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Common auth endpoints require authentication
                        .requestMatchers("/api/**/auth/logout").authenticated()
                        .requestMatchers("/api/**/auth/me").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(sessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                );

        return http.build();
    }
}