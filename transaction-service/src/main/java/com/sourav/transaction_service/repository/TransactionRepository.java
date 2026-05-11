package com.sourav.transaction_service.repository;

import com.sourav.transaction_service.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends
        JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    // ============================================
    // BASIC QUERIES
    // ============================================

    List<Transaction> findByCategoryId(Long categoryId);

    List<Transaction> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId
    );

    List<Transaction> findByUserIdAndDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    Page<Transaction> findByUserId(
            Long userId,
            Pageable pageable
    );
    List<Transaction> findByUserId(
            Long userId
    );

    // ============================================
    // PAGINATION OPTIMIZATION
    // ============================================

    /*
     WHY THIS EXISTS?

     Instead of fetching complete entities first,
     we first fetch IDs only.

     This improves pagination performance
     in large datasets.
    */

    @Query("""
            SELECT t.id
            FROM Transaction t
            WHERE t.userId = :userId
            """)
    Page<Long> findIdsByUserId(
            Long userId,
            Pageable pageable
    );

    /*
     WHY THIS EXISTS?

     Second optimized fetch step.

     We fetch complete rows only for paginated IDs.
    */

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.id IN :ids
            """)
    List<Transaction> findByIdsWithRelations(
            List<Long> ids
    );

    // ============================================
    // ANALYTICS QUERIES
    // ============================================

    /*
     IMPORTANT:

     In microservices architecture,
     we no longer use:

     t.user.id

     because User entity relation was removed.
    */

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.userId = :userId
            """)
    double getTotalExpenseByUserId(Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.userId = :userId
            AND t.date BETWEEN :start AND :end
            """)
    Double getTotalExpenseByUserIdAndDateRange(
            Long userId,
            LocalDate start,
            LocalDate end
    );
}
