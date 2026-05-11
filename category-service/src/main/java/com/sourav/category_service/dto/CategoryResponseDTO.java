package com.sourav.category_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private Long userId;
}