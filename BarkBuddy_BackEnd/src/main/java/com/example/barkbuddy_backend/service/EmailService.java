package com.example.barkbuddy_backend.service;

public interface EmailService {
    void sendAdoptionRequestEmail(String ownerEmail, Long petId, String message, String adopterEmail, String dogName);
}
