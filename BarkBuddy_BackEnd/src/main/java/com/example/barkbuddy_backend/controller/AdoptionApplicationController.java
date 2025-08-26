package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.entity.AdoptionApplications;
import com.example.barkbuddy_backend.service.AdoptionApplicationService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/adoption")
@CrossOrigin(origins = "http://localhost:5500") // adjust to your frontend origin
@RequiredArgsConstructor
public class AdoptionApplicationController {

    private final AdoptionApplicationService adoptionService;
    private final JWTUtil jwtUtil;

    @PostMapping("/request/{dogId}")
    public ResponseEntity<?> requestAdoption(@PathVariable Long dogId,
                                             @RequestBody Map<String,String> body,
                                             @RequestHeader("Authorization") String token) {
        Long adopterId = getUserIdFromToken(token); // implement JWT extraction
//        String message = body.getOrDefault("message","");
        AdoptionApplications app = adoptionService.requestAdoption(dogId, adopterId);
        return ResponseEntity.ok(app);
    }

    // Dummy method to extract userId from JWT (replace with your JWTUtil)
    private Long getUserIdFromToken(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            throw new SecurityException("Invalid or expired token");
        }
        return userId;
    }
}
