package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.APIResponse;
import com.example.barkbuddy_backend.dto.AuthDTO;
import com.example.barkbuddy_backend.dto.UserDTO;
import com.example.barkbuddy_backend.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/barkbuddy")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse> registerUser(
            @RequestBody UserDTO registerDTO) {
        return ResponseEntity.ok(new APIResponse(
                200,
                "OK",
                userService.register(registerDTO)));
    }
    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(
            @RequestBody AuthDTO authDTO) {
        return ResponseEntity.ok(new APIResponse(
                200,
                "OK",
                userService.authenticate(authDTO)));
    }
}
