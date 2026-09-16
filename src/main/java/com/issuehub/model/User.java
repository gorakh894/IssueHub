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

/**
 * User Document - MongoDB entity for users
 * Supports roles: EMPLOYEE, MANAGER, TECHNICIAN, ADMIN
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String phone;

    private UserRole role;

    private String department;

    @Indexed(unique = true, sparse = true)
    private String employeeId;

    private String profileImage;

    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * User roles enum
     */
    public enum UserRole {
        EMPLOYEE,
        MANAGER,
        TECHNICIAN,
        ADMIN
    }
}
