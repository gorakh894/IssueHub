package com.issuehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for IssueHub
 * Centralized Issue Tracking & Resolution Platform
 */
@SpringBootApplication
@EnableMongoAuditing
public class IssueHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssueHubApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("✓ IssueHub Application Started Successfully");
        System.out.println("===========================================\n");
    }
}
