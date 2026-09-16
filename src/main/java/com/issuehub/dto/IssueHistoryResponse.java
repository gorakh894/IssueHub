package com.issuehub.dto;

import com.issuehub.model.IssueHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Issue History Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueHistoryResponse {

    private String id;
    private String issueId;
    private String changedBy;
    private String changedByName;
    private String action;
    private String oldValue;
    private String newValue;
    private String comment;
    private LocalDateTime timestamp;

    /**
     * Convert IssueHistory entity to IssueHistoryResponse DTO
     */
    public static IssueHistoryResponse fromHistory(IssueHistory history) {
        return IssueHistoryResponse.builder()
                .id(history.getId())
                .issueId(history.getIssueId())
                .changedBy(history.getChangedBy())
                .changedByName(history.getChangedByName())
                .action(history.getAction())
                .oldValue(history.getOldValue())
                .newValue(history.getNewValue())
                .comment(history.getComment())
                .timestamp(history.getTimestamp())
                .build();
    }
}
