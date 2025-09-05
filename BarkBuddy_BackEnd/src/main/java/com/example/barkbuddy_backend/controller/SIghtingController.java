package com.example.barkbuddy_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sightings")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class SIghtingController {
}