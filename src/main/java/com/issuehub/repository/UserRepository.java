package com.issuehub.repository;

import com.issuehub.model.User;
import com.issuehub.model.User.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository
 * MongoDB repository for User operations
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by employee ID
     */
    Optional<User> findByEmployeeId(String employeeId);

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if employee ID exists
     */
    boolean existsByEmployeeId(String employeeId);

    /**
     * Find all users by role
     */
    List<User> findByRole(UserRole role);

    /**
     * Find all active users
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Find users by role and active status
     */
    List<User> findByRoleAndIsActive(UserRole role, Boolean isActive);

    /**
     * Find users by department
     */
    List<User> findByDepartment(String department);
}
