package com.issuehub.repository;

import com.issuehub.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Category Repository
 * MongoDB repository for Category operations
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    /**
     * Find category by name
     */
    Optional<Category> findByName(String name);

    /**
     * Check if category name exists
     */
    boolean existsByName(String name);

    /**
     * Find all active categories
     */
    List<Category> findByIsActive(Boolean isActive);

    /**
     * Find category by name (case insensitive)
     */
    Optional<Category> findByNameIgnoreCase(String name);
}
