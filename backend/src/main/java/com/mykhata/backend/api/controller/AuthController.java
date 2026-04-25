package com.mykhata.backend.api.controller;

import com.mykhata.backend.api.dtos.AuthResponse;
import com.mykhata.backend.api.dtos.AuthTypeRequest;
import com.mykhata.backend.api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/authtype")
    public ResponseEntity<?> authenticateAndSetCookie(@RequestBody AuthTypeRequest request, HttpServletResponse response) {
        Optional<AuthResponse> authResponseOptional = authService.authenticate(request);

        if (authResponseOptional.isPresent()) {
            AuthResponse authResponse = authResponseOptional.get();

            Cookie cookie = authService.createJwtCookie(authResponse.getToken());
            response.addCookie(cookie);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Authenticated successfully");
            responseBody.put("role", authResponse.getRole());

            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.status(401).body("User not found");
        }
    }
}
