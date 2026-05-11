package com.sourav.category_service.controller;

import com.sourav.category_service.dto.CategoryRequestDTO;
import com.sourav.category_service.dto.CategoryResponseDTO;
import com.sourav.category_service.entity.Category;
import com.sourav.category_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ============================
    // CREATE CATEGORY
    // ONLY ADMIN
    // ============================

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user/{userId}")
    public CategoryResponseDTO createCategory(
            @PathVariable Long userId,
            @Valid @RequestBody CategoryRequestDTO dto) {

        return categoryService.createCategory(
                userId,
                dto
        );
    }

    // ============================
    // GET CATEGORY
    // USER + ADMIN
    // ============================

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping("/{id}")
    public CategoryResponseDTO getCategory(
            @PathVariable Long id) {

        return categoryService.getCategory(id);
    }

    // ============================
    // GET ALL CATEGORIES
    // USER + ADMIN
    // ============================

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @GetMapping
    public List<Category> getAllCategories() {

        return categoryService.getAllCategories();
    }
}