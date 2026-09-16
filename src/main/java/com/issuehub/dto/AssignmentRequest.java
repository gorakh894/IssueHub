package com.issuehub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Assignment Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentRequest {

    @NotBlank(message = "Technician ID is required")
    private String technicianId;

    private String comment;
}
