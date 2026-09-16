package com.issuehub.service;

import com.issuehub.dto.UserResponse;
import com.issuehub.exception.ResourceNotFoundException;
import com.issuehub.model.User;
import com.issuehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service
 * Business logic for user operations
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    /**
     * Get all users
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * Get users by role
     */
    public List<UserResponse> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * Get all technicians
     */
    public List<UserResponse> getAllTechnicians() {
        return getUsersByRole(User.UserRole.TECHNICIAN);
    }

    /**
     * Get active technicians
     */
    public List<UserResponse> getActiveTechnicians() {
        return userRepository.findByRoleAndIsActive(User.UserRole.TECHNICIAN, true)
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    /**
     * Get user by ID
     */
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponse.fromUser(user);
    }

    /**
     * Update user profile image
     */
    @Transactional
    public UserResponse updateProfileImage(String userEmail, MultipartFile file) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Delete old profile image if exists
        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            String oldPublicId = fileUploadService.extractPublicId(user.getProfileImage());
            if (oldPublicId != null) {
                try {
                    fileUploadService.deleteFile(oldPublicId);
                } catch (Exception e) {
                    // Log but don't fail the update
                }
            }
        }

        // Upload new profile image
        var uploadResponse = fileUploadService.uploadFile(file, "profiles");
        user.setProfileImage(uploadResponse.getUrl());

        User updatedUser = userRepository.save(user);
        return UserResponse.fromUser(updatedUser);
    }

    /**
     * Delete profile image
     */
    @Transactional
    public UserResponse deleteProfileImage(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            String publicId = fileUploadService.extractPublicId(user.getProfileImage());
            if (publicId != null) {
                fileUploadService.deleteFile(publicId);
            }
            user.setProfileImage(null);
            User updatedUser = userRepository.save(user);
            return UserResponse.fromUser(updatedUser);
        }

        return UserResponse.fromUser(user);
    }

    /**
     * Toggle user active status
     */
    @Transactional
    public UserResponse toggleUserStatus(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setIsActive(!user.getIsActive());
        User updatedUser = userRepository.save(user);

        return UserResponse.fromUser(updatedUser);
    }
}
