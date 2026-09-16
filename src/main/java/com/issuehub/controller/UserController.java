package com.issuehub.controller;

import com.issuehub.dto.UserResponse;
import com.issuehub.model.User;
import com.issuehub.service.UserService;
import com.issuehub.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * User Controller
 * REST API for user management
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get all users
     * GET /api/users
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully", users)
        );
    }

    /**
     * Get users by role
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(
            @PathVariable User.UserRole role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully", users)
        );
    }

    /**
     * Get all technicians
     * GET /api/users/technicians
     */
    @GetMapping("/technicians")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllTechnicians(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        
        List<UserResponse> technicians = activeOnly 
                ? userService.getActiveTechnicians() 
                : userService.getAllTechnicians();
        
        return ResponseEntity.ok(
                ApiResponse.success("Technicians retrieved successfully", technicians)
        );
    }

    /**
     * Update profile image
     * POST /api/users/profile-image
     */
    @PostMapping("/profile-image")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfileImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        UserResponse user = userService.updateProfileImage(userEmail, file);
        
        return ResponseEntity.ok(
                ApiResponse.success("Profile image updated successfully", user)
        );
    }

    /**
     * Delete profile image
     * DELETE /api/users/profile-image
     */
    @DeleteMapping("/profile-image")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> deleteProfileImage(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        UserResponse user = userService.deleteProfileImage(userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Profile image deleted successfully", user)
        );
    }

    /**
     * Toggle user active status
     * PATCH /api/users/{id}/toggle-status
     */
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> toggleUserStatus(@PathVariable String id) {
        UserResponse user = userService.toggleUserStatus(id);
        
        String statusMessage = user.getIsActive() ? "activated" : "deactivated";
        return ResponseEntity.ok(
                ApiResponse.success("User " + statusMessage + " successfully", user)
        );
    }
}
