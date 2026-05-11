package com.sourav.category_service.mapper;

import com.sourav.category_service.dto.CategoryResponseDTO;
import com.sourav.category_service.entity.Category;

public class CategoryMapper {

    public static CategoryResponseDTO toDTO(Category c) {

        return CategoryResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .userId(c.getUserId())
                .build();
    }
}