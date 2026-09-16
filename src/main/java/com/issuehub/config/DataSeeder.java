package com.issuehub.config;

import com.issuehub.model.Category;
import com.issuehub.model.User;
import com.issuehub.repository.CategoryRepository;
import com.issuehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Data Seeder Configuration
 * Seeds initial data for development and testing
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            seedCategories();
            seedUsers();
        };
    }

    /**
     * Seed predefined categories
     */
    private void seedCategories() {
        if (categoryRepository.count() > 0) {
            log.info("Categories already exist. Skipping seeding.");
            return;
        }

        List<Category> categories = Arrays.asList(
                Category.builder()
                        .name("Electrical")
                        .description("Electrical-related problems including power outages, wiring issues, and lighting")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Plumbing")
                        .description("Plumbing issues including leaks, drainage, and water supply problems")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("IT Support")
                        .description("IT-related issues including hardware, software, and network problems")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Housekeeping")
                        .description("Cleaning and maintenance of facilities")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Furniture")
                        .description("Furniture-related issues including repairs and replacements")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Security")
                        .description("Security concerns including access control and surveillance")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Safety")
                        .description("Safety-related issues including fire hazards and emergency equipment")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Internet/Network")
                        .description("Internet connectivity and network infrastructure issues")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("HVAC")
                        .description("Heating, Ventilation, and Air Conditioning issues")
                        .isActive(true)
                        .build(),
                
                Category.builder()
                        .name("Other")
                        .description("Other miscellaneous issues")
                        .isActive(true)
                        .build()
        );

        categoryRepository.saveAll(categories);
        log.info("✓ Seeded {} categories", categories.size());
    }

    /**
     * Seed sample users for testing (optional - can be commented out for production)
     */
    private void seedUsers() {
        if (userRepository.count() > 0) {
            log.info("Users already exist. Skipping user seeding.");
            return;
        }

        List<User> users = Arrays.asList(
                // Admin
                User.builder()
                        .name("Admin User")
                        .email("admin@issuehub.com")
                        .password(passwordEncoder.encode("admin123"))
                        .phone("9999999999")
                        .role(User.UserRole.ADMIN)
                        .department("Administration")
                        .employeeId("ADMIN001")
                        .isActive(true)
                        .build(),
                
                // Manager
                User.builder()
                        .name("Manager User")
                        .email("manager@issuehub.com")
                        .password(passwordEncoder.encode("manager123"))
                        .phone("9999999998")
                        .role(User.UserRole.MANAGER)
                        .department("Management")
                        .employeeId("MGR001")
                        .isActive(true)
                        .build(),
                
                // Technicians
                User.builder()
                        .name("John Technician")
                        .email("tech1@issuehub.com")
                        .password(passwordEncoder.encode("tech123"))
                        .phone("9999999997")
                        .role(User.UserRole.TECHNICIAN)
                        .department("Maintenance")
                        .employeeId("TECH001")
                        .isActive(true)
                        .build(),
                
                User.builder()
                        .name("Sarah Technician")
                        .email("tech2@issuehub.com")
                        .password(passwordEncoder.encode("tech123"))
                        .phone("9999999996")
                        .role(User.UserRole.TECHNICIAN)
                        .department("IT Support")
                        .employeeId("TECH002")
                        .isActive(true)
                        .build(),
                
                // Employees
                User.builder()
                        .name("Alice Employee")
                        .email("employee1@issuehub.com")
                        .password(passwordEncoder.encode("emp123"))
                        .phone("9999999995")
                        .role(User.UserRole.EMPLOYEE)
                        .department("Operations")
                        .employeeId("EMP001")
                        .isActive(true)
                        .build(),
                
                User.builder()
                        .name("Bob Employee")
                        .email("employee2@issuehub.com")
                        .password(passwordEncoder.encode("emp123"))
                        .phone("9999999994")
                        .role(User.UserRole.EMPLOYEE)
                        .department("Operations")
                        .employeeId("EMP002")
                        .isActive(true)
                        .build()
        );

        userRepository.saveAll(users);
        log.info("✓ Seeded {} sample users", users.size());
        log.info("===========================================");
        log.info("Sample Login Credentials:");
        log.info("Admin    : admin@issuehub.com / admin123");
        log.info("Manager  : manager@issuehub.com / manager123");
        log.info("Tech 1   : tech1@issuehub.com / tech123");
        log.info("Tech 2   : tech2@issuehub.com / tech123");
        log.info("Employee1: employee1@issuehub.com / emp123");
        log.info("Employee2: employee2@issuehub.com / emp123");
        log.info("===========================================");
    }
}
