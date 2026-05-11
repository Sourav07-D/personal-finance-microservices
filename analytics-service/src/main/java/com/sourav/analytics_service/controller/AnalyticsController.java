package com.sourav.analytics_service.controller;

import com.sourav.analytics_service.dto.CategorySummaryDTO;
import com.sourav.analytics_service.dto.TopCategoryDTO;
import com.sourav.analytics_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // ============================
    // TOTAL EXPENSE
    // ============================

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/user/{userId}/total-expense")
    public Double getTotalExpense(
            @PathVariable Long userId) {

        return analyticsService
                .getTotalExpense(userId);
    }

    // ============================
    // CATEGORY SUMMARY
    // ============================

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/user/{userId}/category-summary")
    public List<CategorySummaryDTO>
    getCategorySummary(
            @PathVariable Long userId) {

        return analyticsService
                .getCategorySummary(userId);
    }

    // ============================
    // TOP CATEGORY
    // ============================

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/user/{userId}/top-category")
    public TopCategoryDTO getTopCategory(
            @PathVariable Long userId) {

        return analyticsService
                .getTopCategory(userId);
    }
}