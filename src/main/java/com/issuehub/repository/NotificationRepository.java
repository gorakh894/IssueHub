package com.issuehub.repository;

import com.issuehub.model.Notification;
import com.issuehub.model.Notification.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Notification Repository
 * MongoDB repository for Notification operations
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Find all notifications for a user
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * Find unread notifications for a user
     */
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(String userId);

    /**
     * Count unread notifications for a user
     */
    long countByUserIdAndIsReadFalse(String userId);

    /**
     * Find notifications by issue
     */
    List<Notification> findByIssueId(String issueId);

    /**
     * Find notifications by type
     */
    List<Notification> findByType(NotificationType type);

    /**
     * Delete all notifications for a user
     */
    void deleteByUserId(String userId);

    /**
     * Delete all notifications for an issue
     */
    void deleteByIssueId(String issueId);
}
