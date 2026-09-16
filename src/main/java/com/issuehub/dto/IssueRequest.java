package com.issuehub.dto;

import com.issuehub.model.Issue.Priority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Issue Request DTO - For creating and updating issues
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @Valid
    @NotNull(message = "Location is required")
    private LocationRequest location;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private List<String> attachments;

    /**
     * Location Request DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LocationRequest {
        
        @NotBlank(message = "Building is required")
        private String building;

        @NotBlank(message = "Floor is required")
        private String floor;

        @NotBlank(message = "Room is required")
        private String room;
    }
}
