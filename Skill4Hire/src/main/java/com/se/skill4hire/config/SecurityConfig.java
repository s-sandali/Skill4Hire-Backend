package com.se.skill4hire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for H2 console (optional: disable for API if using stateless REST)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/admins/register", "/api/admins/login"))
            
            // Allow H2 console in frames
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/h2-console/**", 
                        "/api/admins/register", 
                        "/api/admins/login"
                ).permitAll() // public endpoints
                .anyRequest().authenticated() // all other endpoints require authentication
            )
            
            // Use default Spring login page for form login
            .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
