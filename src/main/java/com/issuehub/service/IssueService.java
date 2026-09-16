package com.issuehub.service;

import com.issuehub.dto.*;
import com.issuehub.exception.BadRequestException;
import com.issuehub.exception.ResourceNotFoundException;
import com.issuehub.model.*;
import com.issuehub.model.Issue.Priority;
import com.issuehub.model.Issue.Status;
import com.issuehub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Issue Service
 * Business logic for issue operations
 */
@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final IssueHistoryRepository historyRepository;

    /**
     * Create new issue
     */
    @Transactional
    public IssueResponse createIssue(IssueRequest request, String userEmail) {
        // Get user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        if (!category.getIsActive()) {
            throw new BadRequestException("Category is not active");
        }

        // Generate issue ID
        String issueId = generateIssueId();

        // Calculate SLA deadline
        LocalDateTime slaDeadline = calculateSlaDeadline(request.getPriority());

        // Create issue location
        Issue.Location location = Issue.Location.builder()
                .building(request.getLocation().getBuilding())
                .floor(request.getLocation().getFloor())
                .room(request.getLocation().getRoom())
                .build();

        // Create issue
        Issue issue = Issue.builder()
                .issueId(issueId)
                .title(request.getTitle())
                .description(request.getDescription())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .reportedBy(user.getId())
                .reportedByName(user.getName())
                .location(location)
                .priority(request.getPriority())
                .status(Status.REPORTED)
                .attachments(request.getAttachments() != null ? request.getAttachments() : List.of())
                .slaDeadline(slaDeadline)
                .slaBreached(false)
                .build();

        Issue savedIssue = issueRepository.save(issue);

        // Create history entry
        createHistoryEntry(savedIssue.getId(), user.getId(), user.getName(),
                "CREATED", null, "Issue created", "Issue reported");

        return IssueResponse.fromIssue(savedIssue);
    }

    /**
     * Get all issues with pagination
     */
    public Page<IssueResponse> getAllIssues(Pageable pageable) {
        return issueRepository.findAll(pageable)
                .map(IssueResponse::fromIssue);
    }

    /**
     * Get issue by ID
     */
    public IssueResponse getIssueById(String id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));
        return IssueResponse.fromIssue(issue);
    }

    /**
     * Get issue by issueId (ISS-2026-0001)
     */
    public IssueResponse getIssueByIssueId(String issueId) {
        Issue issue = issueRepository.findByIssueId(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "issueId", issueId));
        return IssueResponse.fromIssue(issue);
    }

    /**
     * Update issue
     */
    @Transactional
    public IssueResponse updateIssue(String id, IssueRequest request, String userEmail) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        // Update fields
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setCategoryId(category.getId());
        issue.setCategoryName(category.getName());

        Issue.Location location = Issue.Location.builder()
                .building(request.getLocation().getBuilding())
                .floor(request.getLocation().getFloor())
                .room(request.getLocation().getRoom())
                .build();
        issue.setLocation(location);

        if (request.getAttachments() != null) {
            issue.setAttachments(request.getAttachments());
        }

        Issue updatedIssue = issueRepository.save(issue);

        // Create history entry
        createHistoryEntry(issue.getId(), user.getId(), user.getName(),
                "UPDATED", null, "Issue updated", "Issue details updated");

        return IssueResponse.fromIssue(updatedIssue);
    }

    /**
     * Delete issue
     */
    @Transactional
    public void deleteIssue(String id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        // Delete related comments and history
        commentRepository.deleteByIssueId(id);
        historyRepository.deleteByIssueId(id);

        issueRepository.delete(issue);
    }

    /**
     * Update issue status
     */
    @Transactional
    public IssueResponse updateStatus(String id, StatusUpdateRequest request, String userEmail) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate status transition
        validateStatusTransition(issue.getStatus(), request.getStatus());

        Status oldStatus = issue.getStatus();
        issue.setStatus(request.getStatus());

        // Update timestamps based on status
        if (request.getStatus() == Status.RESOLVED) {
            issue.setResolvedAt(LocalDateTime.now());
        } else if (request.getStatus() == Status.CLOSED) {
            issue.setClosedAt(LocalDateTime.now());
        }

        // Check SLA breach
        if (issue.getSlaDeadline() != null && LocalDateTime.now().isAfter(issue.getSlaDeadline())) {
            issue.setSlaBreached(true);
        }

        Issue updatedIssue = issueRepository.save(issue);

        // Create history entry
        createHistoryEntry(issue.getId(), user.getId(), user.getName(),
                "STATUS_CHANGE", oldStatus.name(), request.getStatus().name(), request.getComment());

        return IssueResponse.fromIssue(updatedIssue);
    }

    /**
     * Update issue priority
     */
    @Transactional
    public IssueResponse updatePriority(String id, PriorityUpdateRequest request, String userEmail) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Priority oldPriority = issue.getPriority();
        issue.setPriority(request.getPriority());

        // Recalculate SLA deadline
        issue.setSlaDeadline(calculateSlaDeadline(request.getPriority()));

        Issue updatedIssue = issueRepository.save(issue);

        // Create history entry
        createHistoryEntry(issue.getId(), user.getId(), user.getName(),
                "PRIORITY_CHANGE", oldPriority.name(), request.getPriority().name(), request.getComment());

        return IssueResponse.fromIssue(updatedIssue);
    }

    /**
     * Assign issue to technician
     */
    @Transactional
    public IssueResponse assignIssue(String id, AssignmentRequest request, String userEmail) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", id));

        User manager = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User technician = userRepository.findById(request.getTechnicianId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getTechnicianId()));

        // Validate technician role
        if (technician.getRole() != User.UserRole.TECHNICIAN) {
            throw new BadRequestException("User is not a technician");
        }

        String oldTechnicianName = issue.getAssignedToName();
        issue.setAssignedTo(technician.getId());
        issue.setAssignedToName(technician.getName());
        issue.setStatus(Status.ASSIGNED);

        Issue updatedIssue = issueRepository.save(issue);

        // Create history entry
        String action = oldTechnicianName == null ? "ASSIGNMENT" : "REASSIGNMENT";
        createHistoryEntry(issue.getId(), manager.getId(), manager.getName(),
                action, oldTechnicianName, technician.getName(), request.getComment());

        return IssueResponse.fromIssue(updatedIssue);
    }

    /**
     * Get issues by filters
     */
    public Page<IssueResponse> getIssuesByFilters(
            Status status, Priority priority, String categoryId,
            String assignedTo, String reportedBy, Pageable pageable) {
        
        Page<Issue> issues;

        if (status != null && priority != null) {
            issues = issueRepository.findByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            issues = issueRepository.findByStatus(status, pageable);
        } else if (priority != null) {
            issues = issueRepository.findByPriority(priority, pageable);
        } else if (categoryId != null) {
            issues = issueRepository.findByCategoryId(categoryId, pageable);
        } else if (assignedTo != null) {
            issues = issueRepository.findByAssignedTo(assignedTo, pageable);
        } else if (reportedBy != null) {
            issues = issueRepository.findByReportedBy(reportedBy, pageable);
        } else {
            issues = issueRepository.findAll(pageable);
        }

        return issues.map(IssueResponse::fromIssue);
    }

    /**
     * Search issues
     */
    public Page<IssueResponse> searchIssues(String keyword, Pageable pageable) {
        return issueRepository.searchByTitleOrDescription(keyword, pageable)
                .map(IssueResponse::fromIssue);
    }

    /**
     * Get issue comments
     */
    public List<CommentResponse> getIssueComments(String issueId) {
        // Verify issue exists
        issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", issueId));

        return commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId)
                .stream()
                .map(CommentResponse::fromComment)
                .collect(Collectors.toList());
    }

    /**
     * Add comment to issue
     */
    @Transactional
    public CommentResponse addComment(String issueId, CommentRequest request, String userEmail) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", issueId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .issueId(issue.getId())
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getRole().name())
                .message(request.getMessage())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Create history entry
        createHistoryEntry(issue.getId(), user.getId(), user.getName(),
                "COMMENT_ADDED", null, "Comment added", request.getMessage());

        return CommentResponse.fromComment(savedComment);
    }

    /**
     * Get issue history
     */
    public List<IssueHistoryResponse> getIssueHistory(String issueId) {
        // Verify issue exists
        issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue", "id", issueId));

        return historyRepository.findByIssueIdOrderByTimestampDesc(issueId)
                .stream()
                .map(IssueHistoryResponse::fromHistory)
                .collect(Collectors.toList());
    }

    /**
     * Generate unique issue ID (ISS-2026-0001)
     */
    private String generateIssueId() {
        int year = Year.now().getValue();
        long count = issueRepository.count() + 1;
        return String.format("ISS-%d-%04d", year, count);
    }

    /**
     * Calculate SLA deadline based on priority
     */
    private LocalDateTime calculateSlaDeadline(Priority priority) {
        LocalDateTime now = LocalDateTime.now();
        return switch (priority) {
            case LOW -> now.plusHours(72);
            case MEDIUM -> now.plusHours(48);
            case HIGH -> now.plusHours(24);
            case CRITICAL -> now.plusHours(4);
        };
    }

    /**
     * Validate status transition
     */
    private void validateStatusTransition(Status currentStatus, Status newStatus) {
        // Add business rules for valid status transitions
        if (currentStatus == Status.CLOSED && newStatus != Status.REOPENED) {
            throw new BadRequestException("Closed issue can only be reopened");
        }
    }

    /**
     * Create history entry
     */
    private void createHistoryEntry(String issueId, String userId, String userName,
                                   String action, String oldValue, String newValue, String comment) {
        IssueHistory history = IssueHistory.builder()
                .issueId(issueId)
                .changedBy(userId)
                .changedByName(userName)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .comment(comment)
                .build();

        historyRepository.save(history);
    }
}
