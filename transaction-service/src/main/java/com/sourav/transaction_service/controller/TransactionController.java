package com.sourav.transaction_service.controller;

import com.sourav.transaction_service.dto.*;
import com.sourav.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
  //  private final AnalyticsService analyticsService;

    @PostMapping("/user/{userId}/category/{categoryId}")
    public TransactionResponseDTO createTransaction(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @Valid @RequestBody TransactionRequestDTO dto) {

        return transactionService.createTransaction(userId, categoryId, dto);
    }
    @GetMapping
    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        return transactionService.getAllTransactions(pageable);
    }
    @GetMapping("/user/{userId}")
    public ApiResponse<?> getByUser(
            @PathVariable Long userId,
            Pageable pageable) {

        return ApiResponse.builder()
                .success(true)
                .message("Transactions fetched successfully")
                .data(transactionService.getTransactionsByUser(userId, pageable))
                .build();
    }
    @GetMapping("/category/{categoryId}")
    public List<TransactionResponseDTO> getByCategory(
            @PathVariable Long categoryId) {

        return transactionService.getByCategory(categoryId);
    }
    @GetMapping("/user/{userId}/category/{categoryId}")
    public List<TransactionResponseDTO> getByUserAndCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        return transactionService
                .getByUserAndCategory(userId, categoryId);
    }
    @GetMapping("/user/{userId}/date-range")
    public List<TransactionResponseDTO> getByDateRange(
            @PathVariable Long userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
            ){
        return transactionService.getByUserAndDataRange(userId,start,end);
    }

    @PutMapping("/{transactionId}/user/{userId}")
    public TransactionResponseDTO updateTransaction(
            @PathVariable Long transactionId,
            @PathVariable Long userId,
            @Valid @RequestBody TransactionRequestDTO dto) {

        return transactionService.updateTransaction(userId, transactionId, dto);
    }

    @DeleteMapping("/{transactionId}/user/{userId}")
    public ApiResponse<?> deleteTransaction(
            @PathVariable Long transactionId,
            @PathVariable Long userId) {

        transactionService.deleteTransaction(userId, transactionId);

        return ApiResponse.builder()
                .success(true)
                .message("Transaction deleted successfully")
                .build();
    }

    @PostMapping("/filter")
    public Page<TransactionResponseDTO> filterTransactions(
            @RequestBody TransactionFilterDTO filter,
            Pageable pageable) {

        return transactionService.filterTransactions(filter, pageable);
    }
    @GetMapping("/user/{userId}/all")
    public List<TransactionResponseDTO> getAllTransactionsForAnalytics(
            @PathVariable Long userId) {

        return transactionService
                .getAllTransactionsForAnalytics(userId);
    }

}