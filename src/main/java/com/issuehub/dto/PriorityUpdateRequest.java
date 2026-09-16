package com.issuehub.dto;

import com.issuehub.model.Issue.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Priority Update Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriorityUpdateRequest {

    @NotNull(message = "Priority is required")
    private Priority priority;

    private String comment;
}
