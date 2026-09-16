package com.issuehub.repository;

import com.issuehub.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Comment Repository
 * MongoDB repository for Comment operations
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    /**
     * Find all comments for an issue
     */
    List<Comment> findByIssueIdOrderByCreatedAtAsc(String issueId);

    /**
     * Find all comments by a user
     */
    List<Comment> findByUserId(String userId);

    /**
     * Count comments for an issue
     */
    long countByIssueId(String issueId);

    /**
     * Delete all comments for an issue
     */
    void deleteByIssueId(String issueId);
}
