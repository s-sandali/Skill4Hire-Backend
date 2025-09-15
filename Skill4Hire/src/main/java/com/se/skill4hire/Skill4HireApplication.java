package com.se.skill4hire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.se.skill4hire") // Make sure this covers your controllers

public class Skill4HireApplication {
    public static void main(String[] args) {
        SpringApplication.run(Skill4HireApplication.class, args);
    }
}
