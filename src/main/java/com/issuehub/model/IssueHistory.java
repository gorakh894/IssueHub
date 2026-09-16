package com.issuehub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Issue History Document - Tracks all changes to issues
 */
@Document(collection = "issue_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueHistory {

    @Id
    private String id;

    @Indexed
    private String issueId;

    @Indexed
    private String changedBy; // User ID

    private String changedByName; // Denormalized

    private String action; // e.g., "STATUS_CHANGE", "PRIORITY_CHANGE", "ASSIGNMENT"

    private String oldValue;

    private String newValue;

    private String comment;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
