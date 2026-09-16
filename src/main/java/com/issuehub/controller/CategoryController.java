package com.issuehub.controller;

import com.issuehub.dto.CategoryRequest;
import com.issuehub.dto.CategoryResponse;
import com.issuehub.service.CategoryService;
import com.issuehub.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Controller
 * REST API for category management
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create new category
     * POST /api/categories
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        
        CategoryResponse category = categoryService.createCategory(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", category));
    }

    /**
     * Get all categories
     * GET /api/categories
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        
        List<CategoryResponse> categories = activeOnly 
                ? categoryService.getActiveCategories() 
                : categoryService.getAllCategories();
        
        return ResponseEntity.ok(
                ApiResponse.success("Categories retrieved successfully", categories)
        );
    }

    /**
     * Get category by ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable String id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Category retrieved successfully", category)
        );
    }

    /**
     * Update category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest request) {
        
        CategoryResponse category = categoryService.updateCategory(id, request);
        
        return ResponseEntity.ok(
                ApiResponse.success("Category updated successfully", category)
        );
    }

    /**
     * Toggle category status (activate/deactivate)
     * PATCH /api/categories/{id}/toggle-status
     */
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> toggleStatus(@PathVariable String id) {
        CategoryResponse category = categoryService.toggleCategoryStatus(id);
        
        String statusMessage = category.getIsActive() ? "activated" : "deactivated";
        return ResponseEntity.ok(
                ApiResponse.success("Category " + statusMessage + " successfully", category)
        );
    }

    /**
     * Delete category
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.success("Category deleted successfully")
        );
    }
}
