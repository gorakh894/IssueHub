package com.issuehub.controller;

import com.issuehub.dto.*;
import com.issuehub.model.Issue.Priority;
import com.issuehub.model.Issue.Status;
import com.issuehub.service.IssueService;
import com.issuehub.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Issue Controller
 * REST API for issue management
 */
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    /**
     * Create new issue
     * POST /api/issues
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<IssueResponse>> createIssue(
            @Valid @RequestBody IssueRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        IssueResponse issue = issueService.createIssue(request, userEmail);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Issue created successfully", issue));
    }

    /**
     * Get all issues with pagination and filters
     * GET /api/issues
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<IssueResponse>>> getAllIssues(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String reportedBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<IssueResponse> issues = issueService.getIssuesByFilters(
                status, priority, categoryId, assignedTo, reportedBy, pageable);
        
        return ResponseEntity.ok(
                ApiResponse.success("Issues retrieved successfully", issues)
        );
    }

    /**
     * Search issues
     * GET /api/issues/search
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<IssueResponse>>> searchIssues(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<IssueResponse> issues = issueService.searchIssues(keyword, pageable);
        
        return ResponseEntity.ok(
                ApiResponse.success("Search results retrieved", issues)
        );
    }

    /**
     * Get issue by ID
     * GET /api/issues/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<IssueResponse>> getIssueById(@PathVariable String id) {
        IssueResponse issue = issueService.getIssueById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Issue retrieved successfully", issue)
        );
    }

    /**
     * Update issue
     * PUT /api/issues/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<IssueResponse>> updateIssue(
            @PathVariable String id,
            @Valid @RequestBody IssueRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        IssueResponse issue = issueService.updateIssue(id, request, userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Issue updated successfully", issue)
        );
    }

    /**
     * Delete issue
     * DELETE /api/issues/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteIssue(@PathVariable String id) {
        issueService.deleteIssue(id);
        return ResponseEntity.ok(
                ApiResponse.success("Issue deleted successfully")
        );
    }

    /**
     * Update issue status
     * PATCH /api/issues/{id}/status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<IssueResponse>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody StatusUpdateRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        IssueResponse issue = issueService.updateStatus(id, request, userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Status updated successfully", issue)
        );
    }

    /**
     * Update issue priority
     * PATCH /api/issues/{id}/priority
     */
    @PatchMapping("/{id}/priority")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<IssueResponse>> updatePriority(
            @PathVariable String id,
            @Valid @RequestBody PriorityUpdateRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        IssueResponse issue = issueService.updatePriority(id, request, userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Priority updated successfully", issue)
        );
    }

    /**
     * Assign issue to technician
     * PATCH /api/issues/{id}/assign
     */
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ApiResponse<IssueResponse>> assignIssue(
            @PathVariable String id,
            @Valid @RequestBody AssignmentRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        IssueResponse issue = issueService.assignIssue(id, request, userEmail);
        
        return ResponseEntity.ok(
                ApiResponse.success("Issue assigned successfully", issue)
        );
    }

    /**
     * Get issue comments
     * GET /api/issues/{id}/comments
     */
    @GetMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(@PathVariable String id) {
        List<CommentResponse> comments = issueService.getIssueComments(id);
        return ResponseEntity.ok(
                ApiResponse.success("Comments retrieved successfully", comments)
        );
    }

    /**
     * Add comment to issue
     * POST /api/issues/{id}/comments
     */
    @PostMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable String id,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        CommentResponse comment = issueService.addComment(id, request, userEmail);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully", comment));
    }

    /**
     * Get issue history
     * GET /api/issues/{id}/history
     */
    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<IssueHistoryResponse>>> getHistory(@PathVariable String id) {
        List<IssueHistoryResponse> history = issueService.getIssueHistory(id);
        return ResponseEntity.ok(
                ApiResponse.success("History retrieved successfully", history)
        );
    }
}
