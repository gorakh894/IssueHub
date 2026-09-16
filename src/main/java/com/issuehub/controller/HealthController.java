package com.issuehub.controller;

import com.issuehub.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Provides endpoints to test application and MongoDB connection
 */
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final MongoTemplate mongoTemplate;

    /**
     * Basic health check endpoint
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("application", "IssueHub");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("message", "Application is running successfully");

        return ResponseEntity.ok(
            ApiResponse.success("Health check successful", healthData)
        );
    }

    /**
     * MongoDB connection test endpoint
     */
    @GetMapping("/mongodb")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testMongoConnection() {
        try {
            Map<String, Object> mongoInfo = new HashMap<>();
            
            // Test MongoDB connection by pinging
            Document pingResult = mongoTemplate.getDb()
                .runCommand(new Document("ping", 1));
            
            // Get database name
            String databaseName = mongoTemplate.getDb().getName();
            
            // Get collection names
            var collectionNames = mongoTemplate.getDb()
                .listCollectionNames();
            
            mongoInfo.put("status", "CONNECTED");
            mongoInfo.put("database", databaseName);
            mongoInfo.put("pingResult", pingResult.toJson());
            mongoInfo.put("timestamp", LocalDateTime.now());
            mongoInfo.put("message", "MongoDB connection is active");

            return ResponseEntity.ok(
                ApiResponse.success("MongoDB connection test successful", mongoInfo)
            );
            
        } catch (Exception e) {
            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("status", "DISCONNECTED");
            errorInfo.put("error", e.getMessage());
            errorInfo.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(500).body(
                ApiResponse.error("MongoDB connection failed: " + e.getMessage(), errorInfo)
            );
        }
    }

    /**
     * Detailed application info endpoint
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> info() {
        Map<String, Object> appInfo = new HashMap<>();
        appInfo.put("application", "IssueHub");
        appInfo.put("version", "1.0.0");
        appInfo.put("description", "Centralized Issue Tracking & Resolution Platform");
        appInfo.put("timestamp", LocalDateTime.now());
        appInfo.put("javaVersion", System.getProperty("java.version"));
        appInfo.put("springBootVersion", "3.2.5");

        return ResponseEntity.ok(
            ApiResponse.success("Application info retrieved", appInfo)
        );
    }
}
