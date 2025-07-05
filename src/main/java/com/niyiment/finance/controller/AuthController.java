package com.niyiment.finance.controller;


import com.niyiment.finance.dto.LoginRequest;
import com.niyiment.finance.dto.LoginResponse;
import com.niyiment.finance.dto.RegisterRequest;
import com.niyiment.finance.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);

            return ResponseEntity.ok(response);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.authentication(request);

            return ResponseEntity.ok(response);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
