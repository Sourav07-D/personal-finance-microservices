package com.sourav.analytics_service.service;

import com.sourav.analytics_service.client.TransactionClient;
import com.sourav.analytics_service.dto.CategorySummaryDTO;
import com.sourav.analytics_service.dto.TopCategoryDTO;
import com.sourav.analytics_service.dto.TransactionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionClient transactionClient;

    private static final Logger log =
            LoggerFactory.getLogger(AnalyticsService.class);

    // ============================
    // TOTAL EXPENSE
    // ============================

    @Cacheable(
            value = "totalExpense",
            key = "'user:' + #userId"
    )
    public Double getTotalExpense(Long userId) {

        log.info("Fetching total expense for userId={}", userId);

        List<TransactionResponseDTO> transactions =
                transactionClient.getAllTransactions(userId);

        return transactions.stream()
                .mapToDouble(TransactionResponseDTO::getAmount)
                .sum();
    }

    // ============================
    // CATEGORY SUMMARY
    // ============================

    @Cacheable(
            value = "categorySummary",
            key = "'user:' + #userId"
    )
    public List<CategorySummaryDTO>
    getCategorySummary(Long userId) {

        log.info("Fetching category summary for userId={}", userId);

        List<TransactionResponseDTO> transactions =
                transactionClient.getAllTransactions(userId);

        Map<Long, Double> grouped =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                TransactionResponseDTO::getCategoryId,
                                Collectors.summingDouble(
                                        TransactionResponseDTO::getAmount
                                )
                        ));

        return grouped.entrySet()
                .stream()
                .map(entry ->
                        CategorySummaryDTO.builder()
                                .categoryName(
                                        "Category-" + entry.getKey()
                                )
                                .totalAmount(entry.getValue())
                                .build()
                )
                .toList();
    }

    // ============================
    // TOP CATEGORY
    // ============================

    @Cacheable(
            value = "topCategory",
            key = "'user:' + #userId"
    )
    public TopCategoryDTO getTopCategory(Long userId) {

        log.info("Fetching top category for userId={}", userId);

        List<TransactionResponseDTO> transactions =
                transactionClient.getAllTransactions(userId);

        Map<Long, Double> grouped =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                TransactionResponseDTO::getCategoryId,
                                Collectors.summingDouble(
                                        TransactionResponseDTO::getAmount
                                )
                        ));

        return grouped.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry ->
                        TopCategoryDTO.builder()
                                .categoryId(entry.getKey())
                                .totalAmount(entry.getValue())
                                .build()
                )
                .orElse(null);
    }
}