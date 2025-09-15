package com.se.skill4hire.security;

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
                // Disable CSRF for H2 console and selected public endpoints
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/admins/register", "/api/admins/login", "/api/candidates/**/cv"))

                // Allow H2 console in frames
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/h2-console/**",
                                "/api/candidate/skills",
                                "/api/candidate/cv",
                                "/api/candidate/company",
                                "/api/candidates/**/cv"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
