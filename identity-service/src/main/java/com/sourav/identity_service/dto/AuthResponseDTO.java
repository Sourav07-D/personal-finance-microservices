package com.sourav.identity_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
    private String message;
    private  String token;
    private String refreshToken;
}