package com.sourav.category_service.service;

import com.sourav.category_service.client.UserClient;
import com.sourav.category_service.dto.CategoryRequestDTO;
import com.sourav.category_service.dto.CategoryResponseDTO;
import com.sourav.category_service.entity.Category;
import com.sourav.category_service.exception.ResourceNotFoundException;
import com.sourav.category_service.mapper.CategoryMapper;
import com.sourav.category_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserClient userClient;

    private static final Logger log =
            LoggerFactory.getLogger(CategoryService.class);

    // ============================
    // CREATE CATEGORY
    // ============================

    public CategoryResponseDTO createCategory(
            Long userId,
            CategoryRequestDTO dto) {

        log.info("Creating category for userId={}", userId);

        // ✅ Validate user via identity-service
        userClient.getUser(userId);

        Category category = Category.builder()
                .name(dto.getName())
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        Category saved =
                categoryRepository.save(category);

        log.info("Category created id={}",
                saved.getId());

        return CategoryMapper.toDTO(saved);
    }

    // ============================
    // GET ALL
    // ============================

    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    // ============================
    // GET CATEGORY
    // ============================

    public CategoryResponseDTO getCategory(Long id) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                ));

        return CategoryMapper.toDTO(category);
    }
}