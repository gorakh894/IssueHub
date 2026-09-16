package com.issuehub.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.issuehub.dto.FileUploadResponse;
import com.issuehub.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * File Upload Service
 * Handles file uploads to Cloudinary
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final Cloudinary cloudinary;

    // Allowed file types for issue attachments
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );

    // Maximum file size: 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * Upload single file to Cloudinary
     */
    public FileUploadResponse uploadFile(MultipartFile file, String folder) {
        validateFile(file);

        try {
            // Upload to Cloudinary
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "issuehub/" + folder,
                    "resource_type", "auto",
                    "transformation", new com.cloudinary.Transformation()
                            .quality("auto")
                            .fetchFormat("auto")
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            // Extract response data
            String url = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            Long size = ((Number) uploadResult.get("bytes")).longValue();

            log.info("File uploaded successfully: {}", url);

            return FileUploadResponse.builder()
                    .url(url)
                    .publicId(publicId)
                    .format(format)
                    .size(size)
                    .originalFilename(file.getOriginalFilename())
                    .uploadedAt(new Date())
                    .build();

        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary: {}", e.getMessage());
            throw new BadRequestException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Upload multiple files to Cloudinary
     */
    public List<FileUploadResponse> uploadMultipleFiles(List<MultipartFile> files, String folder) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("No files provided");
        }

        if (files.size() > 5) {
            throw new BadRequestException("Maximum 5 files can be uploaded at once");
        }

        List<FileUploadResponse> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                FileUploadResponse response = uploadFile(file, folder);
                uploadedFiles.add(response);
            } catch (Exception e) {
                log.error("Error uploading file {}: {}", file.getOriginalFilename(), e.getMessage());
                // Continue with other files
            }
        }

        if (uploadedFiles.isEmpty()) {
            throw new BadRequestException("Failed to upload any files");
        }

        return uploadedFiles;
    }

    /**
     * Delete file from Cloudinary
     */
    public void deleteFile(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");

            if ("ok".equals(resultStatus)) {
                log.info("File deleted successfully: {}", publicId);
            } else {
                log.warn("File deletion returned status: {}", resultStatus);
            }
        } catch (IOException e) {
            log.error("Error deleting file from Cloudinary: {}", e.getMessage());
            throw new BadRequestException("Failed to delete file: " + e.getMessage());
        }
    }

    /**
     * Delete multiple files from Cloudinary
     */
    public void deleteMultipleFiles(List<String> publicIds) {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }

        for (String publicId : publicIds) {
            try {
                deleteFile(publicId);
            } catch (Exception e) {
                log.error("Error deleting file {}: {}", publicId, e.getMessage());
                // Continue with other files
            }
        }
    }

    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException(
                    String.format("File size exceeds maximum limit of %d MB", MAX_FILE_SIZE / (1024 * 1024))
            );
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException(
                    "Invalid file type. Allowed types: JPEG, JPG, PNG, WEBP, GIF"
            );
        }

        // Check filename
        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new BadRequestException("Invalid filename");
        }
    }

    /**
     * Extract public ID from Cloudinary URL
     */
    public String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            return null;
        }

        try {
            // Extract public ID from URL
            // Format: https://res.cloudinary.com/cloud_name/image/upload/v12345/folder/filename.ext
            String[] parts = cloudinaryUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            String pathAfterUpload = parts[1];
            // Remove version number if present (v12345/)
            String publicIdWithExt = pathAfterUpload.replaceFirst("v\\d+/", "");
            // Remove file extension
            int lastDotIndex = publicIdWithExt.lastIndexOf('.');
            if (lastDotIndex > 0) {
                return publicIdWithExt.substring(0, lastDotIndex);
            }

            return publicIdWithExt;
        } catch (Exception e) {
            log.error("Error extracting public ID from URL: {}", e.getMessage());
            return null;
        }
    }
}
