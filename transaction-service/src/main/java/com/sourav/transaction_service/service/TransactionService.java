package com.sourav.transaction_service.service;

import com.sourav.transaction_service.client.CategoryClient;
import com.sourav.transaction_service.client.UserClient;
import com.sourav.transaction_service.dto.*;
import com.sourav.transaction_service.entity.Transaction;
//import com.sourav.expense_tracker_api.entity.User;
import com.sourav.transaction_service.exception.ResourceNotFoundException;
import com.sourav.transaction_service.exception.UnauthorizedException;
import com.sourav.transaction_service.mapper.TransactionMapper;
//import com.sourav.expense_tracker_api.repository.CategoryRepository;
import com.sourav.transaction_service.repository.TransactionRepository;
//import com.sourav.transaction_service.repository.UserRepository;
//import com.sourav.expense_tracker_api.specification.TransactionSpecification;
import com.sourav.transaction_service.specification.TransactionSpecification;
import com.sourav.transaction_service.util.SortValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserClient userClient;
    private final CategoryClient categoryClient;

    private static final Logger log =
            LoggerFactory.getLogger(TransactionService.class);

    // ============================
    // CREATE TRANSACTION
    // ============================

    @Caching(evict = {
            // ✅ FIX: Proper cache key format (was inconsistent before)
            @CacheEvict(value = "totalExpense", key = "'user:' + #userId"),
            @CacheEvict(value = "categorySummary", key = "'user:' + #userId"),
            @CacheEvict(value = "totalExpenseByDate", allEntries = true) // multiple combinations
    })
    public TransactionResponseDTO createTransaction(
            Long userId,
            Long categoryId,
            TransactionRequestDTO dto) {

        log.info(
                "Creating transaction for userId={} categoryId={}",
                userId,
                categoryId
        );

        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .date(dto.getDate())

                // ✅ VERY IMPORTANT FIX
                .userId(userId)
                .categoryId(categoryId)

                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved =
                transactionRepository.save(transaction);

        log.info(
                "Transaction created successfully with id={}",
                saved.getId()
        );

        return TransactionMapper.toDTO(saved);
    }

    // ============================
    // READ OPERATIONS
    // ============================

    public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(TransactionMapper::toDTO);
    }

    public Page<TransactionResponseDTO> getTransactionsByUser(
            Long userId,
            Pageable pageable) {

        log.info("Fetching transactions for userId={} page={} size={}",
                userId,
                pageable.getPageNumber(),
                pageable.getPageSize());

        userClient.getUser(userId);

        Page<Transaction> transactions =
                transactionRepository.findByUserId(userId, pageable);

        return transactions.map(TransactionMapper::toDTO);
    }

    public List<TransactionResponseDTO> getByCategory(Long categoryId) {
      categoryClient.getCategory(categoryId);
        List<Transaction> transactions =
                transactionRepository.findByCategoryId(categoryId);

        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    public List<TransactionResponseDTO> getByUserAndCategory(
            Long userId,
            Long categoryId) {

       userClient.getUser(userId);
       categoryClient.getCategory(categoryId);

        List<Transaction> transactions =
                transactionRepository
                        .findByUserIdAndCategoryId(userId, categoryId);

        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    public List<TransactionResponseDTO> getByUserAndDataRange(
            Long userId,
            LocalDate start,
            LocalDate end) {

       userClient.getUser(userId);

        List<Transaction> transactions =
                transactionRepository.findByUserIdAndDateBetween(userId, start, end);

        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    // ============================
    // CACHED METHODS (FIXED)
    // ============================





    // ============================
    // UPDATE TRANSACTION
    // ============================

    @Caching(evict = {
            @CacheEvict(value = "totalExpense", key = "'user:' + #userId"),
            @CacheEvict(value = "categorySummary", key = "'user:' + #userId"),
            @CacheEvict(value = "totalExpenseByDate", allEntries = true)
    })
    public TransactionResponseDTO updateTransaction(
            Long userId,
            Long transactionId,
            TransactionRequestDTO dto) {

        log.info("Updating transactionId={} for userId={}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Transaction not found id={}", transactionId);
                    return new ResourceNotFoundException("Transaction not found");
                });

        // ✅ SECURITY CHECK
        if (!transaction.getUserId().equals(userId)) {
            log.error("Unauthorized update attempt userId={}", userId);
            throw new UnauthorizedException("Unauthorized access");
        }

        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());

        Transaction updated = transactionRepository.save(transaction);

        log.info("Transaction updated successfully id={}", updated.getId());

        return TransactionMapper.toDTO(updated);
    }

    // ============================
    // DELETE TRANSACTION
    // ============================

    @Caching(evict = {
            @CacheEvict(value = "totalExpense", key = "'user:' + #userId"),
            @CacheEvict(value = "categorySummary", key = "'user:' + #userId"),
            @CacheEvict(value = "totalExpenseByDate", allEntries = true)
    })
    public void deleteTransaction(Long userId, Long transactionId) {

        log.info("Deleting transactionId={} for userId={}", transactionId, userId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Transaction not found id={}", transactionId);
                    return new ResourceNotFoundException("Transaction not found");
                });

        // ✅ SECURITY CHECK
        if (!transaction.getUserId().equals(userId)) {
            log.error("Unauthorized delete attempt userId={}", userId);
            throw new UnauthorizedException("Unauthorized access");
        }

        transactionRepository.delete(transaction);

        log.info("Transaction deleted successfully id={}", transactionId);
    }

    public Page<TransactionResponseDTO> filterTransactions(
            TransactionFilterDTO filter,
            Pageable pageable) {

        log.info("Filtering transactions with pagination + sorting");

        // ✅ Validate sort fields
        Sort validatedSort = SortValidator.validate(pageable.getSort());

        Pageable newPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                validatedSort
        );

        Specification<Transaction> spec =
                TransactionSpecification.filter(filter);

        Page<Transaction> page =
                transactionRepository.findAll(spec, newPageable);

        return page.map(TransactionMapper::toDTO);
    }
    public List<TransactionResponseDTO>
    getAllTransactionsForAnalytics(Long userId) {

        return transactionRepository
                .findByUserId(userId)
                .stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }
}