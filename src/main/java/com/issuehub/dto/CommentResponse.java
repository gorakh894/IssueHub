package com.issuehub.dto;

import com.issuehub.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Comment Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private String id;
    private String issueId;
    private String userId;
    private String userName;
    private String userRole;
    private String message;
    private LocalDateTime createdAt;

    /**
     * Convert Comment entity to CommentResponse DTO
     */
    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .issueId(comment.getIssueId())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .userRole(comment.getUserRole())
                .message(comment.getMessage())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
