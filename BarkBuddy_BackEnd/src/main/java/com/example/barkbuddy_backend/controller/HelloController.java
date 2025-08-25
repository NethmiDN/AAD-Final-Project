package com.example.barkbuddy_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hello")
@CrossOrigin(origins = "*")
public class HelloController {

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Hello! This is a public endpoint - no authentication required.");
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Hello World! API is working correctly.");
    }

    @PostMapping("/ping")
    public ResponseEntity<String> pingEndpoint(@RequestBody(required = false) String message) {
        return ResponseEntity.ok("Pong! Received: " + (message != null ? message : "no message"));
    }
}
