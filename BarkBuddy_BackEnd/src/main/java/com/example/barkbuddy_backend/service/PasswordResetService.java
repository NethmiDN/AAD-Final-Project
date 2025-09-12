package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.APIResponse;
import com.example.barkbuddy_backend.dto.ForgotPasswordRequestDTO;
import com.example.barkbuddy_backend.dto.VerifyOtpDTO;

public interface PasswordResetService {
    APIResponse sendOtpForPasswordReset(ForgotPasswordRequestDTO request);
    APIResponse verifyOtpAndResetPassword(VerifyOtpDTO request);
}