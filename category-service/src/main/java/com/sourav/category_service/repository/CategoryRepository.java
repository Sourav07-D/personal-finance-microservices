package com.sourav.category_service.repository;

import com.sourav.category_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);
}