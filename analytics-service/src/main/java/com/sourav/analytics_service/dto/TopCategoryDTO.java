package com.sourav.analytics_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopCategoryDTO {

    private Long categoryId;

    private Double totalAmount;
}