package com.se.skill4hire.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/register-test")
    public String registerTest(@RequestBody String body) {
        return "Received: " + body;
    }
}