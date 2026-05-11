package com.sourav.transaction_service.mapper;

import com.sourav.transaction_service.dto.TransactionResponseDTO;
import com.sourav.transaction_service.entity.Transaction;

public class TransactionMapper {

    public static TransactionResponseDTO toDTO(Transaction t) {
        return TransactionResponseDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .date(t.getDate())
                .userId(t.getUserId())
                .categoryId(t.getCategoryId())
                .build();
    }
}