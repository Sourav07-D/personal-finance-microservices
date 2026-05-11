package com.sourav.identity_service.controller;

import com.sourav.identity_service.dto.ApiResponse;
import com.sourav.identity_service.dto.UserRequestDTO;
import com.sourav.identity_service.dto.UserResponseDTO;
import com.sourav.identity_service.entity.User;
import com.sourav.identity_service.exception.ResourceNotFoundException;
import com.sourav.identity_service.repository.UserRepository;
import com.sourav.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getAllUsers() {

        return ApiResponse.builder()
                .success(true)
                .message("Users fetched successfully")
                .data(userService.getAllUsers())
                .build();
    }
}