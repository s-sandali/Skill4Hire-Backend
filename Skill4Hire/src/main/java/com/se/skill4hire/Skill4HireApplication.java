package com.se.skill4hire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.se.skill4hire.entity") // explicitly scan entities
public class Skill4HireApplication {
    public static void main(String[] args) {
        SpringApplication.run(Skill4HireApplication.class, args);
    }
}
