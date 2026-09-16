package com.issuehub.controller;

import com.issuehub.dto.FileUploadResponse;
import com.issuehub.service.FileUploadService;
import com.issuehub.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * File Upload Controller
 * REST API for file upload operations
 */
@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * Upload single file
     * POST /api/uploads/single
     */
    @PostMapping("/single")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadSingleFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {
        
        FileUploadResponse response = fileUploadService.uploadFile(file, folder);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("File uploaded successfully", response));
    }

    /**
     * Upload multiple files
     * POST /api/uploads/multiple
     */
    @PostMapping("/multiple")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {
        
        List<FileUploadResponse> responses = fileUploadService.uploadMultipleFiles(files, folder);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        String.format("%d file(s) uploaded successfully", responses.size()), 
                        responses
                ));
    }

    /**
     * Upload issue attachment
     * POST /api/uploads/issue-attachment
     */
    @PostMapping("/issue-attachment")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadIssueAttachment(
            @RequestParam("file") MultipartFile file) {
        
        FileUploadResponse response = fileUploadService.uploadFile(file, "issues");
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Issue attachment uploaded successfully", response));
    }

    /**
     * Upload profile image
     * POST /api/uploads/profile-image
     */
    @PostMapping("/profile-image")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {
        
        FileUploadResponse response = fileUploadService.uploadFile(file, "profiles");
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Profile image uploaded successfully", response));
    }

    /**
     * Delete file
     * DELETE /api/uploads
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteFile(
            @RequestParam("publicId") String publicId) {
        
        fileUploadService.deleteFile(publicId);
        
        return ResponseEntity.ok(
                ApiResponse.success("File deleted successfully")
        );
    }
}
