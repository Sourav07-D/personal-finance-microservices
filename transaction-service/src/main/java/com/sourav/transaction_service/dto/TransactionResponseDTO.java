package com.sourav.transaction_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionResponseDTO {

    private Long id;
    private Double amount;
    private String description;
    private LocalDate date;
    private Long userId;
    private Long categoryId;
}