package com.sourav.analytics_service.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySummaryDTO
        implements Serializable {

    private String categoryName;

    private Double totalAmount;
}