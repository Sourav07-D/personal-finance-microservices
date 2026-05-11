package com.sourav.identity_service.service;

import com.sourav.identity_service.dto.UserRequestDTO;
import com.sourav.identity_service.dto.UserResponseDTO;
import com.sourav.identity_service.entity.Role;
import com.sourav.identity_service.entity.User;
import com.sourav.identity_service.mapper.UserMapper;
import com.sourav.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO dto) {

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();


   User savedUser = userRepository.save(user);

        return UserMapper.toDTO(savedUser);
    }
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}