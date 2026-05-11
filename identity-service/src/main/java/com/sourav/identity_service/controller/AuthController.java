package com.sourav.identity_service.controller;

import com.sourav.identity_service.dto.ApiResponse;
import com.sourav.identity_service.dto.AuthRequestDTO;
import com.sourav.identity_service.dto.RefreshTokenRequestDTO;
import com.sourav.identity_service.dto.UserRequestDTO;
import com.sourav.identity_service.security.JwtService;

import com.sourav.identity_service.service.AuthService;
import com.sourav.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody UserRequestDTO dto) {

        return ApiResponse.builder()
                .success(true)
                .message("User created successfully")
                .data(userService.createUser(dto))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AuthRequestDTO request) {

        return ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {

        String email = jwtService.extractEmail(request.getRefreshToken());

        if (!jwtService.isTokenValid(request.getRefreshToken(), email)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(email, "USER"); // role fix later

        return ApiResponse.builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(newAccessToken)
                .build();
    }
}