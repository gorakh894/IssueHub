package com.issuehub.controller;

import com.issuehub.dto.NotificationResponse;
import com.issuehub.service.NotificationService;
import com.issuehub.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Notification Controller
 * REST API for notification management
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get all notifications for current user
     * GET /api/notifications
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<NotificationResponse> notifications = notificationService.getUserNotifications(userEmail, pageable);
        
        return ResponseEntity.ok(
                ApiResponse.success("Notifications retrieved successfully", notifications)
        );
    }

    /**
     * Get unread notifications
     * GET /api/notifications/unread
     */
    @GetMapping("/unread")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Unread notifications retrieved", notifications)
        );
    }

    /**
     * Get unread notification count
     * GET /api/notifications/unread/count
     */
    @GetMapping("/unread/count")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        long count = notificationService.getUnreadCount(userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Unread count retrieved", Map.of("count", count))
        );
    }

    /**
     * Mark notification as read
     * PATCH /api/notifications/{id}/read
     */
    @PatchMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable String id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        NotificationResponse notification = notificationService.markAsRead(id, userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Notification marked as read", notification)
        );
    }

    /**
     * Mark all notifications as read
     * PATCH /api/notifications/read-all
     */
    @PatchMapping("/read-all")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Object>> markAllAsRead(Authentication authentication) {
        String userEmail = authentication.getName();
        notificationService.markAllAsRead(userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("All notifications marked as read")
        );
    }

    /**
     * Delete notification
     * DELETE /api/notifications/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteNotification(
            @PathVariable String id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        notificationService.deleteNotification(id, userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Notification deleted successfully")
        );
    }
}
