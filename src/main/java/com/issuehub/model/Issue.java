package com.issuehub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Issue Document - MongoDB entity for issue tracking
 */
@Document(collection = "issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {

    @Id
    private String id;

    @Indexed(unique = true)
    private String issueId; // ISS-2026-0001

    private String title;

    private String description;

    @Indexed
    private String categoryId;

    private String categoryName; // Denormalized for quick access

    @Indexed
    private String reportedBy; // User ID

    private String reportedByName; // Denormalized

    @Indexed
    private String assignedTo; // Technician User ID

    private String assignedToName; // Denormalized

    private Location location;

    @Indexed
    private Priority priority;

    @Indexed
    private Status status;

    @Builder.Default
    private List<String> attachments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

    private LocalDateTime closedAt;

    // SLA fields
    private LocalDateTime slaDeadline;
    private Boolean slaBreached;

    /**
     * Issue Status Enum
     */
    public enum Status {
        REPORTED,
        UNDER_REVIEW,
        ASSIGNED,
        IN_PROGRESS,
        RESOLVED,
        CLOSED,
        REOPENED,
        CANCELLED,
        ON_HOLD
    }

    /**
     * Priority Enum
     */
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    /**
     * Location embedded document
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private String building;
        private String floor;
        private String room;
    }
}
