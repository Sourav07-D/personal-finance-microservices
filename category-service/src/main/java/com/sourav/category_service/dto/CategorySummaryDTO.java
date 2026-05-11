package com.sourav.category_service.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor   // ✅ this already creates constructor
@NoArgsConstructor
@Builder
public class CategorySummaryDTO implements Serializable {

    private String categoryName;
    private Double totalAmount;
}
