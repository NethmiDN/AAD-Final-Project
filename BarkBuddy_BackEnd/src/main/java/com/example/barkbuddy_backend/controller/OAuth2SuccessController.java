package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.entity.Role;
import com.example.barkbuddy_backend.entity.User;
import com.example.barkbuddy_backend.repo.UserRepository;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OAuth2SuccessController {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @GetMapping("/oauth2/success")
    public RedirectView oauth2LoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = (UserDetails) User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .role(Role.valueOf(user.getRole().name()))
                .build();

        String token = jwtUtil.generateToken(userDetails, user.getId(), List.of(user.getRole().name()));

        // Redirect to frontend with token and user info as URL parameters
        // Using file:// protocol for local development
        String basePath = "file:///D:/Final_Project/BarkBuddy_FrontEnd/pages/";
        String redirectUrl = String.format(basePath + "oauth-callback.html?token=%s&userId=%d&email=%s&username=%s&role=%s",
                token, user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());

        return new RedirectView(redirectUrl);
    }
}
