package com.se.skill4hire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
public class Skill4HireApplication {

    public static void main(String[] args) {
        SpringApplication.run(Skill4HireApplication.class, args);
    }

}
