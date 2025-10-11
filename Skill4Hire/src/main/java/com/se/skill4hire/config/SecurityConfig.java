package com.se.skill4hire.config;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5714"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - authentication (MUST come first)
                        .requestMatchers(
                                // Standard API endpoints
                                "/api/candidates/auth/register",
                                "/api/candidates/auth/login",
                                "/api/candidates/auth/test-register", // Temporary test endpoint
                                "/api/companies/auth/register",
                                "/api/companies/auth/login",
                                "/api/employees/auth/register",
                                "/api/employees/auth/login",
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/jobposts/**",
                                "/api/jobs/**",
                                // Without /api prefix (in case it's being stripped somewhere)
                                "/candidates/auth/register",
                                "/candidates/auth/login",
                                "/companies/auth/register",
                                "/companies/auth/login",
                                "/employees/auth/register",
                                "/employees/auth/login",
                                "/auth/login",
                                "/auth/logout",
                                "/api/jobposts/search",  // Add this
                                "/api/jobposts",         // Already exists but ensure it's public
                                 "/api/jobposts/**",
                                "/api/uploads/**",  // Add this line for file access
                                "/uploads/**",
                                // This allows all jobpost endpoints publicly
                                "/api/jobs/**",
                                "/error/**",
                                "/error"
                        ).permitAll()

                        // Authenticated endpoints (specific patterns before broad ones)
                        .requestMatchers(
                                "/api/candidates/auth/logout",
                                "/api/candidates/auth/me",
                                "/api/companies/auth/logout",
                                "/api/companies/auth/me",
                                "/api/employees/auth/logout",
                                "/api/employees/auth/me"
                        ).authenticated()

                        // Role-specific endpoints (broader patterns come last)
                        .requestMatchers("/api/candidates/**").hasAuthority("CANDIDATE")
                        .requestMatchers("/api/companies/**").hasAuthority("COMPANY")
                        .requestMatchers("/api/employees/**").hasAuthority("EMPLOYEE")

                        .anyRequest().authenticated()
                )
                // Temporarily disable custom filter to test
                // ENABLE the session authentication filter (remove the comment)
                .addFilterBefore(sessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                );
                    


        return http.build();
    }
}