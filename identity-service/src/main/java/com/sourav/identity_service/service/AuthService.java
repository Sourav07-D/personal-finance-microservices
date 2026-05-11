package com.sourav.identity_service.service;

import com.sourav.identity_service.dto.AuthRequestDTO;
import com.sourav.identity_service.dto.AuthResponseDTO;
import com.sourav.identity_service.entity.User;
import com.sourav.identity_service.exception.AccountBlockedException;
import com.sourav.identity_service.exception.InvalidCredentialsException;
import com.sourav.identity_service.exception.ResourceNotFoundException;
import com.sourav.identity_service.repository.UserRepository;
import com.sourav.identity_service.security.JwtService;
import com.sourav.identity_service.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final  JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    public AuthResponseDTO login(AuthRequestDTO request) {

        String email = request.getEmail();

        // 🔥 STEP 3.1 → Check if blocked
        if (loginAttemptService.isBlocked(email)) {
            throw new AccountBlockedException("Account blocked due to multiple failed attempts");
        }

        // 🔥 STEP 3.2 → Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🔥 STEP 5 → Safe logging (NO password/token)
        System.out.println("Login attempt for user: " + email);

        // 🔥 STEP 3.3 → Password validation
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            // 🔥 STEP 3.4 → Increment failed attempts
            loginAttemptService.loginFailed(email);

            throw new InvalidCredentialsException("Invalid credentials");
        }

        // 🔥 STEP 3.5 → Reset attempts on success
        loginAttemptService.loginSucceeded(email);

        // 🔥 STEP 4 → Generate tokens (already correct)
        String accessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return AuthResponseDTO.builder()
                .message("Login successful")
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}