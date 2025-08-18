package com.example.barkbuddy_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/admin")
    public String sayHelloAdmin() {
        return "Hello, Admin!";
    }

    @GetMapping("/user")
    public String sayHelloUser() {
        return "Hello, User!";
    }
}
