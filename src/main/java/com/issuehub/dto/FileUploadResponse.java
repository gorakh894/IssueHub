package com.issuehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * File Upload Response DTO
 * Contains information about uploaded file
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponse {

    private String url;
    private String publicId;
    private String format;
    private Long size;
    private String originalFilename;
    private Date uploadedAt;
}
