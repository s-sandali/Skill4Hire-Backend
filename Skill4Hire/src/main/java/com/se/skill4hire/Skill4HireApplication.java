package com.se.skill4hire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories("com.se.skill4hire.repository")
@EntityScan("com.se.skill4hire.entity")
public class Skill4HireApplication {
    public static void main(String[] args) {
        SpringApplication.run(Skill4HireApplication.class, args);
    }
}
