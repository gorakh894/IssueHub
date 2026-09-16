package com.issuehub.dto;

import com.issuehub.model.User;
import com.issuehub.model.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Response DTO
 * Returns user information without password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private String department;
    private String employeeId;
    private String profileImage;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convert User entity to UserResponse DTO
     */
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .department(user.getDepartment())
                .employeeId(user.getEmployeeId())
                .profileImage(user.getProfileImage())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
