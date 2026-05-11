package com.sourav.category_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "categories",
        indexes = {

                @Index(
                        name = "idx_user_id",
                        columnList = "userId"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ============================
    // CATEGORY NAME
    // ============================

    @Column(nullable = false)
    private String name;

    // ============================
    // USER ID (NO ENTITY RELATION)
    // ============================

    private Long userId;

    // ============================
    // AUDIT FIELD
    // ============================

    private LocalDateTime createdAt;
}