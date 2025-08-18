package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.AuthDTO;
import com.example.barkbuddy_backend.dto.AuthResponseDTO;
import com.example.barkbuddy_backend.dto.UserDTO;

public interface UserService {
     AuthResponseDTO authenticate(AuthDTO authDTO);
     String register(UserDTO registerDTO);

}