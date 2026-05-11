package com.sourav.transaction_service.specification;

import com.sourav.transaction_service.dto.TransactionFilterDTO;
import com.sourav.transaction_service.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> filter(TransactionFilterDTO filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔥 USER FILTER
            if (filter.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), filter.getUserId()));
            }

            // 🔥 CATEGORY FILTER
            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            // 🔥 DATE RANGE
            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                predicates.add(cb.between(root.get("date"),
                        filter.getStartDate(),
                        filter.getEndDate()));
            }

            // 🔥 MIN AMOUNT
            if (filter.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
            }

            // 🔥 MAX AMOUNT
            if (filter.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}