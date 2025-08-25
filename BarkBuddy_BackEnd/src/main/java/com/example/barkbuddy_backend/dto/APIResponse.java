package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    private int status;
    private String message;
    private Object data;
}
