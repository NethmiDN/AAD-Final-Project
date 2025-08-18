package com.example.barkbuddy_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class APIResponse {
    private int code;
    private String status;
    private Object data;
}
