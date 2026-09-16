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
 * Comment Document - MongoDB entity for issue comments
 */
@Document(collection = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    private String id;

    @Indexed
    private String issueId;

    @Indexed
    private String userId;

    private String userName; // Denormalized

    private String userRole; // Denormalized

    private String message;

    @CreatedDate
    private LocalDateTime createdAt;
}
