package com.issuehub.service;

import com.issuehub.dto.AuthResponse;
import com.issuehub.dto.LoginRequest;
import com.issuehub.dto.RegisterRequest;
import com.issuehub.dto.UserResponse;
import com.issuehub.exception.DuplicateResourceException;
import com.issuehub.model.User;
import com.issuehub.repository.UserRepository;
import com.issuehub.security.CustomUserDetailsService;
import com.issuehub.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication Service
 * Handles user registration and login
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Register new user
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Check if employee ID already exists (if provided)
        if (request.getEmployeeId() != null && !request.getEmployeeId().isEmpty()) {
            if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
                throw new DuplicateResourceException("User", "employeeId", request.getEmployeeId());
            }
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .department(request.getDepartment())
                .employeeId(request.getEmployeeId())
                .isActive(true)
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        // Return response
        return new AuthResponse(jwtToken, UserResponse.fromUser(savedUser));
    }

    /**
     * Authenticate user login
     */
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Load user details
        User user = userDetailsService.loadUserEntityByEmail(request.getEmail());

        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        // Return response
        return new AuthResponse(jwtToken, UserResponse.fromUser(user));
    }

    /**
     * Get current authenticated user
     */
    public UserResponse getCurrentUser(String email) {
        User user = userDetailsService.loadUserEntityByEmail(email);
        return UserResponse.fromUser(user);
    }
}
