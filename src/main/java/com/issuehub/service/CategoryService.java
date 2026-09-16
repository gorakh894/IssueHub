package com.issuehub.service;

import com.issuehub.dto.CategoryRequest;
import com.issuehub.dto.CategoryResponse;
import com.issuehub.exception.DuplicateResourceException;
import com.issuehub.exception.ResourceNotFoundException;
import com.issuehub.model.Category;
import com.issuehub.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Category Service
 * Business logic for category operations
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Create new category
     */
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // Check if category name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category", "name", request.getName());
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromCategory(savedCategory);
    }

    /**
     * Get all categories
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    /**
     * Get active categories only
     */
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findByIsActive(true)
                .stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());
    }

    /**
     * Get category by ID
     */
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return CategoryResponse.fromCategory(category);
    }

    /**
     * Update category
     */
    @Transactional
    public CategoryResponse updateCategory(String id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if new name already exists (excluding current category)
        categoryRepository.findByName(request.getName()).ifPresent(existingCategory -> {
            if (!existingCategory.getId().equals(id)) {
                throw new DuplicateResourceException("Category", "name", request.getName());
            }
        });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromCategory(updatedCategory);
    }

    /**
     * Toggle category active status
     */
    @Transactional
    public CategoryResponse toggleCategoryStatus(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setIsActive(!category.getIsActive());
        Category updatedCategory = categoryRepository.save(category);
        
        return CategoryResponse.fromCategory(updatedCategory);
    }

    /**
     * Delete category
     */
    @Transactional
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);
    }
}
