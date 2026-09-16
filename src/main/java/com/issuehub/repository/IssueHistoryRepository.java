package com.issuehub.repository;

import com.issuehub.model.IssueHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Issue History Repository
 * MongoDB repository for IssueHistory operations
 */
@Repository
public interface IssueHistoryRepository extends MongoRepository<IssueHistory, String> {

    /**
     * Find all history entries for an issue
     */
    List<IssueHistory> findByIssueIdOrderByTimestampDesc(String issueId);

    /**
     * Find all history entries by user
     */
    List<IssueHistory> findByChangedBy(String userId);

    /**
     * Delete all history for an issue
     */
    void deleteByIssueId(String issueId);
}
