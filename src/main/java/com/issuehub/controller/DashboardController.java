package com.issuehub.controller;

import com.issuehub.dto.DashboardStatsResponse;
import com.issuehub.service.DashboardService;
import com.issuehub.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard Controller
 * REST API for dashboard statistics and analytics
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get employee dashboard statistics
     * GET /api/dashboard/employee
     */
    @GetMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getEmployeeDashboard(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        DashboardStatsResponse stats = dashboardService.getEmployeeDashboard(userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Employee dashboard retrieved successfully", stats)
        );
    }

    /**
     * Get manager dashboard statistics
     * GET /api/dashboard/manager
     */
    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getManagerDashboard() {
        DashboardStatsResponse stats = dashboardService.getManagerDashboard();
        
        return ResponseEntity.ok(
                ApiResponse.success("Manager dashboard retrieved successfully", stats)
        );
    }

    /**
     * Get technician dashboard statistics
     * GET /api/dashboard/technician
     */
    @GetMapping("/technician")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getTechnicianDashboard(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        DashboardStatsResponse stats = dashboardService.getTechnicianDashboard(userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Technician dashboard retrieved successfully", stats)
        );
    }
}
