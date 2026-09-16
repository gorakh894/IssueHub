package com.issuehub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Notification Document - MongoDB entity for user notifications
 */
@Document(collection = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String issueId;

    private String title;

    private String message;

    private NotificationType type;

    @Builder.Default
    private Boolean isRead = false;

    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Notification types enum
     */
    public enum NotificationType {
        ISSUE_CREATED,
        ISSUE_ASSIGNED,
        ISSUE_REASSIGNED,
        STATUS_CHANGED,
        PRIORITY_CHANGED,
        COMMENT_ADDED,
        ISSUE_RESOLVED,
        ISSUE_REOPENED,
        ISSUE_CLOSED,
        SLA_BREACH_WARNING,
        SLA_BREACHED
    }
}
