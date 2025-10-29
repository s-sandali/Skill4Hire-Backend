package com.se.skill4hire.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    /**
     * ✅ CORS Configuration
     * Allows both localhost (for dev) and deployed frontend (Vercel)
     * and ensures credentials (cookies) are included.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = new ArrayList<>();
        origins.add("https://skill4-hire-frontend-ez8f.vercel.app");
        origins.add("http://localhost:5175");
        origins.add("http://localhost:5174");
        origins.add("http://localhost:3000");
        origins.add("http://127.0.0.1:5173");
        origins.add("http://127.0.0.1:3000");
        configuration.setAllowedOrigins(origins);

        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ Enable CORS + Disable CSRF for API requests
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                // ✅ Authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints (login/register, static, error)
                        .requestMatchers(
                                "/api/candidates/auth/register",
                                "/api/candidates/auth/login",
                                "/api/candidates/auth/test-register",
                                "/api/companies/auth/register",
                                "/api/companies/auth/login",
                                "/api/employees/auth/register",
                                "/api/employees/auth/login",
                                "/api/admin/auth/login",
                                "/api/admin/auth/register",
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/uploads/**",
                                "/uploads/**",

                                "/error/**",
                                "/error"
                        ).permitAll()

                        // Public GET endpoints (job listings)
                        .requestMatchers(HttpMethod.GET,
                                "/api/jobposts",
                                "/api/jobposts/**",
                                "/api/jobs/**"
                        ).permitAll()

                        // Authenticated endpoints (user info, logout)
                        .requestMatchers(
                                "/api/candidates/auth/logout",
                                "/api/candidates/auth/me",
                                "/api/companies/auth/logout",
                                "/api/companies/auth/me",
                                "/api/employees/auth/logout",
                                "/api/employees/auth/me"
                        ).authenticated()

                        // Role-specific endpoints
                        .requestMatchers("/api/candidates/**").hasAuthority("CANDIDATE")
                        .requestMatchers("/api/companies/**").hasAuthority("COMPANY")
                        .requestMatchers("/api/employees/**").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // ✅ Add your session filter
                .addFilterBefore(sessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // ✅ Allow same-origin frames (e.g. for H2 console if used)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // ✅ Session management setup
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                );

        return http.build();
    }
}
