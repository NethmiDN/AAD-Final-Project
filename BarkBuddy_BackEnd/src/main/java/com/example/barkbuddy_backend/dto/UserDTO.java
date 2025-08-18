package com.example.barkbuddy_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String role;
}
