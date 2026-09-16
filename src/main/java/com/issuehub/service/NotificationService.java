package com.issuehub.service;

import com.issuehub.dto.NotificationResponse;
import com.issuehub.exception.ResourceNotFoundException;
import com.issuehub.model.Notification;
import com.issuehub.model.Notification.NotificationType;
import com.issuehub.repository.NotificationRepository;
import com.issuehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Notification Service
 * Business logic for notification operations
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * Create notification for a user
     */
    public Notification createNotification(String userId, String issueId, String title, 
                                         String message, NotificationType type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .issueId(issueId)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }

    /**
     * Get all notifications for a user
     */
    public Page<NotificationResponse> getUserNotifications(String userEmail, Pageable pageable) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(NotificationResponse::fromNotification);
    }

    /**
     * Get unread notifications for a user
     */
    public List<NotificationResponse> getUnreadNotifications(String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(NotificationResponse::fromNotification)
                .collect(Collectors.toList());
    }

    /**
     * Get unread notification count
     */
    public long getUnreadCount(String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return notificationRepository.countByUserIdAndIsReadFalse(user.getId());
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public NotificationResponse markAsRead(String notificationId, String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        // Verify notification belongs to user
        if (!notification.getUserId().equals(user.getId())) {
            throw new ResourceNotFoundException("Notification not found");
        }

        notification.setIsRead(true);
        Notification updated = notificationRepository.save(notification);

        return NotificationResponse.fromNotification(updated);
    }

    /**
     * Mark all notifications as read for a user
     */
    @Transactional
    public void markAllAsRead(String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Notification> unreadNotifications = 
                notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId());

        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Delete notification
     */
    @Transactional
    public void deleteNotification(String notificationId, String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        // Verify notification belongs to user
        if (!notification.getUserId().equals(user.getId())) {
            throw new ResourceNotFoundException("Notification not found");
        }

        notificationRepository.delete(notification);
    }

    /**
     * Helper methods for creating specific notification types
     */

    public void notifyIssueCreated(String managerId, String issueId, String issueTitle) {
        createNotification(
                managerId,
                issueId,
                "New Issue Reported",
                String.format("A new issue '%s' has been reported.", issueTitle),
                NotificationType.ISSUE_CREATED
        );
    }

    public void notifyIssueAssigned(String technicianId, String issueId, String issueTitle) {
        createNotification(
                technicianId,
                issueId,
                "Issue Assigned",
                String.format("Issue '%s' has been assigned to you.", issueTitle),
                NotificationType.ISSUE_ASSIGNED
        );
    }

    public void notifyIssueReassigned(String technicianId, String issueId, String issueTitle) {
        createNotification(
                technicianId,
                issueId,
                "Issue Reassigned",
                String.format("Issue '%s' has been reassigned to you.", issueTitle),
                NotificationType.ISSUE_REASSIGNED
        );
    }

    public void notifyStatusChanged(String userId, String issueId, String issueTitle, String newStatus) {
        createNotification(
                userId,
                issueId,
                "Status Updated",
                String.format("Issue '%s' status changed to %s.", issueTitle, newStatus),
                NotificationType.STATUS_CHANGED
        );
    }

    public void notifyPriorityChanged(String userId, String issueId, String issueTitle, String newPriority) {
        createNotification(
                userId,
                issueId,
                "Priority Updated",
                String.format("Issue '%s' priority changed to %s.", issueTitle, newPriority),
                NotificationType.PRIORITY_CHANGED
        );
    }

    public void notifyCommentAdded(String userId, String issueId, String issueTitle, String commenterName) {
        createNotification(
                userId,
                issueId,
                "New Comment",
                String.format("%s commented on issue '%s'.", commenterName, issueTitle),
                NotificationType.COMMENT_ADDED
        );
    }

    public void notifyIssueResolved(String userId, String issueId, String issueTitle) {
        createNotification(
                userId,
                issueId,
                "Issue Resolved",
                String.format("Issue '%s' has been resolved.", issueTitle),
                NotificationType.ISSUE_RESOLVED
        );
    }

    public void notifyIssueReopened(String technicianId, String issueId, String issueTitle) {
        createNotification(
                technicianId,
                issueId,
                "Issue Reopened",
                String.format("Issue '%s' has been reopened.", issueTitle),
                NotificationType.ISSUE_REOPENED
        );
    }

    public void notifyIssueClosed(String userId, String issueId, String issueTitle) {
        createNotification(
                userId,
                issueId,
                "Issue Closed",
                String.format("Issue '%s' has been closed.", issueTitle),
                NotificationType.ISSUE_CLOSED
        );
    }
}
