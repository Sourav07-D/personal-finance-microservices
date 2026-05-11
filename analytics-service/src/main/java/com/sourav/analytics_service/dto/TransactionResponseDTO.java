package com.sourav.analytics_service.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;

    private Double amount;

    private String description;

    private LocalDate date;

    private Long userId;

    private Long categoryId;
}