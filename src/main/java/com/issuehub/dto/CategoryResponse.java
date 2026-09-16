package com.issuehub.dto;

import com.issuehub.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Category Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private String id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;

    /**
     * Convert Category entity to CategoryResponse DTO
     */
    public static CategoryResponse fromCategory(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
