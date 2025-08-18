package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.AuthDTO;
import com.example.barkbuddy_backend.dto.AuthResponseDTO;
import com.example.barkbuddy_backend.dto.UserDTO;
import com.example.barkbuddy_backend.entity.Role;
import com.example.barkbuddy_backend.entity.User;
import com.example.barkbuddy_backend.repo.UserRepository;
import com.example.barkbuddy_backend.service.UserService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        User user=
                userRepository.findByEmail(authDTO.getEmail())
                        .orElseThrow(
                                ()->new UsernameNotFoundException
                                        ("Email not found"));
        if (!passwordEncoder.matches(
                authDTO.getPassword(),
                user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        String token=jwtUtil.generateToken(authDTO.getEmail());
        return  new AuthResponseDTO(token);
    }
    public String register(UserDTO registerDTO) {
        if(userRepository.findByEmail(
                registerDTO.getUsername()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        User user=User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(
                        registerDTO.getPassword()))
                .role(Role.valueOf(registerDTO.getRole().toUpperCase()))
                .build();
        userRepository.save(user);
        return  "User Registration Success";
    }
}
