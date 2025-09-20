package com.se.skill4hire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.se.skill4hire")
@EnableJpaRepositories(basePackages = "com.se.skill4hire.repository") // ← ADD THIS
@EntityScan(basePackages = "com.se.skill4hire.entity") // ← ADD THIS
public class Skill4HireApplication {
    public static void main(String[] args) {
        SpringApplication.run(Skill4HireApplication.class, args);
    }
}