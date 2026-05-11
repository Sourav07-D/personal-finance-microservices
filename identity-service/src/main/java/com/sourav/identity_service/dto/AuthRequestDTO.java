package com.sourav.identity_service.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}