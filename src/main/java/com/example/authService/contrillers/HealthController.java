package com.example.authService.contrillers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Cheap liveness check with no DB/auth dependency — intended target for an external
// keep-alive pinger (Render's free tier spins the container down after ~15 min idle).
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
