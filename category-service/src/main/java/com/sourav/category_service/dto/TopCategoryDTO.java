package com.sourav.category_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopCategoryDTO {
    private Long categoryId;
    private Double totalAmount;
}
