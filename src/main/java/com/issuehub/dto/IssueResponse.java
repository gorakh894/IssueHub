package com.issuehub.dto;

import com.issuehub.model.Issue;
import com.issuehub.model.Issue.Priority;
import com.issuehub.model.Issue.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Issue Response DTO - Returns issue information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponse {

    private String id;
    private String issueId;
    private String title;
    private String description;
    private String categoryId;
    private String categoryName;
    private String reportedBy;
    private String reportedByName;
    private String assignedTo;
    private String assignedToName;
    private LocationResponse location;
    private Priority priority;
    private Status status;
    private List<String> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private LocalDateTime slaDeadline;
    private Boolean slaBreached;

    /**
     * Convert Issue entity to IssueResponse DTO
     */
    public static IssueResponse fromIssue(Issue issue) {
        LocationResponse locationResponse = null;
        if (issue.getLocation() != null) {
            locationResponse = LocationResponse.builder()
                    .building(issue.getLocation().getBuilding())
                    .floor(issue.getLocation().getFloor())
                    .room(issue.getLocation().getRoom())
                    .build();
        }

        return IssueResponse.builder()
                .id(issue.getId())
                .issueId(issue.getIssueId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .categoryId(issue.getCategoryId())
                .categoryName(issue.getCategoryName())
                .reportedBy(issue.getReportedBy())
                .reportedByName(issue.getReportedByName())
                .assignedTo(issue.getAssignedTo())
                .assignedToName(issue.getAssignedToName())
                .location(locationResponse)
                .priority(issue.getPriority())
                .status(issue.getStatus())
                .attachments(issue.getAttachments())
                .createdAt(issue.getCreatedAt())
                .updatedAt(issue.getUpdatedAt())
                .resolvedAt(issue.getResolvedAt())
                .closedAt(issue.getClosedAt())
                .slaDeadline(issue.getSlaDeadline())
                .slaBreached(issue.getSlaBreached())
                .build();
    }

    /**
     * Location Response DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LocationResponse {
        private String building;
        private String floor;
        private String room;
    }
}
