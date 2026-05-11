package com.sourav.identity_service.mapper;

import com.sourav.identity_service.dto.UserResponseDTO;
import com.sourav.identity_service.entity.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}