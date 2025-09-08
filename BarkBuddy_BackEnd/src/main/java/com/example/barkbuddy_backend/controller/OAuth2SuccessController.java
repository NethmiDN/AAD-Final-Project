package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.entity.Role;
import com.example.barkbuddy_backend.entity.User;
import com.example.barkbuddy_backend.repo.UserRepository;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OAuth2SuccessController {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @GetMapping("/oauth2/success")
    public ResponseEntity<?> oauth2LoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = (UserDetails) User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .role(Role.valueOf(user.getRole().name()))
                .build();

        String token = jwtUtil.generateToken(userDetails, user.getId(), List.of(user.getRole().name()));

        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "email", user.getEmail(),
                "username", user.getUsername()
        ));
    }

}
