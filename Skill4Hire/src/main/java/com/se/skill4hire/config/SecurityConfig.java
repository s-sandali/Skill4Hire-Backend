package com.se.skill4hire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

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

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5175"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
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
                        // Allow CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
                                "/api/admin/auth/register",
                                "/api/admin/auth/login",
                                "/api/auth/login",
                                "/api/auth/logout",
<<<<<<< HEAD
                                "/api/jobposts/**",
                                "/api/jobs/**",
                                // Without /api prefix (in case it's being stripped somewhere)
=======

                                // File upload endpoints
                                "/uploads/**",

                                // Without /api prefix
>>>>>>> origin/main
                                "/candidates/auth/register",
                                "/candidates/auth/login",
                                "/companies/auth/register",
                                "/companies/auth/login",
                                "/employees/auth/register",
                                "/employees/auth/login",
                                "/admin/auth/register",
                                "/admin/auth/login",
                                "/auth/login",
                                "/auth/logout",

                                // Other public endpoints
                                "/h2-console/**",
                                "/h2-console",
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
                                "/api/employees/auth/me",
                                "/api/admin/auth/logout",
                                "/api/admin/auth/me"
                        ).authenticated()

                        // Role-specific endpoints
                        .requestMatchers("/api/candidates/**").hasAnyAuthority("CANDIDATE", "ADMIN")
                        .requestMatchers("/api/companies/**").hasAnyAuthority("COMPANY", "ADMIN")
                        .requestMatchers("/api/employees/**").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                // Add custom session authentication filter
               .addFilterBefore(sessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
.exceptionHandling(ex -> ex
        .authenticationEntryPoint((request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\"}");
        })
        .accessDeniedHandler((request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\"}");
        })
)
.headers(headers -> headers.frameOptions().sameOrigin())

                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                );

        return http.build();
    }
}
