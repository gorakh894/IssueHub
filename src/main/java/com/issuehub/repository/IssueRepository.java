package com.issuehub.repository;

import com.issuehub.model.Issue;
import com.issuehub.model.Issue.Priority;
import com.issuehub.model.Issue.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Issue Repository
 * MongoDB repository for Issue operations
 */
@Repository
public interface IssueRepository extends MongoRepository<Issue, String> {

    /**
     * Find issue by issueId (ISS-2026-0001)
     */
    Optional<Issue> findByIssueId(String issueId);

    /**
     * Find all issues by status
     */
    List<Issue> findByStatus(Status status);
    Page<Issue> findByStatus(Status status, Pageable pageable);

    /**
     * Find all issues by priority
     */
    List<Issue> findByPriority(Priority priority);
    Page<Issue> findByPriority(Priority priority, Pageable pageable);

    /**
     * Find all issues by category
     */
    List<Issue> findByCategoryId(String categoryId);
    Page<Issue> findByCategoryId(String categoryId, Pageable pageable);

    /**
     * Find all issues reported by a user
     */
    List<Issue> findByReportedBy(String userId);
    Page<Issue> findByReportedBy(String userId, Pageable pageable);

    /**
     * Find all issues assigned to a technician
     */
    List<Issue> findByAssignedTo(String technicianId);
    Page<Issue> findByAssignedTo(String technicianId, Pageable pageable);

    /**
     * Find issues by status and assigned technician
     */
    List<Issue> findByStatusAndAssignedTo(Status status, String technicianId);

    /**
     * Find issues by status and priority
     */
    List<Issue> findByStatusAndPriority(Status status, Priority priority);
    Page<Issue> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);

    /**
     * Find issues created between dates
     */
    List<Issue> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Count issues by status
     */
    long countByStatus(Status status);

    /**
     * Count issues by priority
     */
    long countByPriority(Priority priority);

    /**
     * Count issues by assigned technician
     */
    long countByAssignedTo(String technicianId);

    /**
     * Count issues by reported user
     */
    long countByReportedBy(String userId);

    /**
     * Find issues by SLA breach status
     */
    List<Issue> findBySlaBreached(Boolean breached);

    /**
     * Search issues by title or description
     */
    @Query("{'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}")
    Page<Issue> searchByTitleOrDescription(String keyword, Pageable pageable);

    /**
     * Get latest issue number for ID generation
     */
    Optional<Issue> findTopByOrderByCreatedAtDesc();
}
