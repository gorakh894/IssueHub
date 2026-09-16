package com.issuehub.service;

import com.issuehub.dto.DashboardStatsResponse;
import com.issuehub.dto.DashboardStatsResponse.*;
import com.issuehub.exception.ResourceNotFoundException;
import com.issuehub.model.Issue;
import com.issuehub.model.User;
import com.issuehub.repository.IssueRepository;
import com.issuehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Dashboard Service
 * Provides analytics and statistics using MongoDB aggregation
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * Get employee dashboard statistics
     */
    public DashboardStatsResponse getEmployeeDashboard(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long myTotalIssues = issueRepository.countByReportedBy(user.getId());
        long myOpenIssues = countIssuesByReporterAndStatus(user.getId(), 
                Arrays.asList(Issue.Status.REPORTED, Issue.Status.UNDER_REVIEW, 
                             Issue.Status.ASSIGNED, Issue.Status.IN_PROGRESS, Issue.Status.REOPENED));
        long myInProgress = countIssuesByReporterAndStatus(user.getId(), 
                Collections.singletonList(Issue.Status.IN_PROGRESS));
        long myResolved = countIssuesByReporterAndStatus(user.getId(), 
                Collections.singletonList(Issue.Status.RESOLVED));
        long myClosed = countIssuesByReporterAndStatus(user.getId(), 
                Collections.singletonList(Issue.Status.CLOSED));

        // Get issues by status for the employee
        List<StatusStats> statusStats = getIssuesByStatusForUser(user.getId());

        return DashboardStatsResponse.builder()
                .totalIssues(myTotalIssues)
                .myIssues(myTotalIssues)
                .openIssues(myOpenIssues)
                .inProgressIssues(myInProgress)
                .resolvedIssues(myResolved)
                .closedIssues(myClosed)
                .issuesByStatus(statusStats)
                .build();
    }

    /**
     * Get manager dashboard statistics
     */
    public DashboardStatsResponse getManagerDashboard() {
        long totalIssues = issueRepository.count();
        long openIssues = countIssuesByStatuses(Arrays.asList(
                Issue.Status.REPORTED, Issue.Status.UNDER_REVIEW, Issue.Status.ASSIGNED));
        long inProgress = issueRepository.countByStatus(Issue.Status.IN_PROGRESS);
        long resolved = issueRepository.countByStatus(Issue.Status.RESOLVED);
        long closed = issueRepository.countByStatus(Issue.Status.CLOSED);
        long highPriority = issueRepository.countByPriority(Issue.Priority.HIGH);
        long criticalPriority = issueRepository.countByPriority(Issue.Priority.CRITICAL);
        long pendingAssignment = issueRepository.countByStatus(Issue.Status.REPORTED);
        
        // SLA breached issues
        List<Issue> allIssues = issueRepository.findAll();
        long slaBreached = allIssues.stream()
                .filter(issue -> Boolean.TRUE.equals(issue.getSlaBreached()))
                .count();

        // Calculate average resolution time
        Double avgResolutionTime = calculateAverageResolutionTime();
        Double slaCompliance = calculateSlaCompliance();

        // Get statistics for charts
        List<CategoryStats> categoryStats = getIssuesByCategory();
        List<StatusStats> statusStats = getIssuesByStatus();
        List<PriorityStats> priorityStats = getIssuesByPriority();
        List<TrendStats> trendStats = getIssuesTrend(7); // Last 7 days
        List<TechnicianPerformance> techPerformance = getTechnicianPerformance();

        return DashboardStatsResponse.builder()
                .totalIssues(totalIssues)
                .openIssues(openIssues)
                .inProgressIssues(inProgress)
                .resolvedIssues(resolved)
                .closedIssues(closed)
                .highPriorityIssues(highPriority)
                .criticalPriorityIssues(criticalPriority)
                .pendingAssignment(pendingAssignment)
                .slaBreachedIssues(slaBreached)
                .averageResolutionTimeHours(avgResolutionTime)
                .slaCompliancePercentage(slaCompliance)
                .issuesByCategory(categoryStats)
                .issuesByStatus(statusStats)
                .issuesByPriority(priorityStats)
                .issuesTrend(trendStats)
                .technicianPerformance(techPerformance)
                .build();
    }

    /**
     * Get technician dashboard statistics
     */
    public DashboardStatsResponse getTechnicianDashboard(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long assignedIssues = issueRepository.countByAssignedTo(user.getId());
        long pending = countIssuesByTechnicianAndStatus(user.getId(), 
                Collections.singletonList(Issue.Status.ASSIGNED));
        long inProgress = countIssuesByTechnicianAndStatus(user.getId(), 
                Collections.singletonList(Issue.Status.IN_PROGRESS));
        long resolved = countIssuesByTechnicianAndStatus(user.getId(), 
                Collections.singletonList(Issue.Status.RESOLVED));
        long completed = countIssuesByTechnicianAndStatus(user.getId(), 
                Arrays.asList(Issue.Status.RESOLVED, Issue.Status.CLOSED));

        // Calculate technician-specific metrics
        Double avgResolutionTime = calculateAverageResolutionTimeForTechnician(user.getId());
        Double slaCompliance = calculateSlaComplianceForTechnician(user.getId());

        // Get issues by priority for the technician
        List<PriorityStats> priorityStats = getIssuesByPriorityForTechnician(user.getId());

        return DashboardStatsResponse.builder()
                .totalIssues(assignedIssues)
                .assignedToMe(assignedIssues)
                .openIssues(pending)
                .inProgressIssues(inProgress)
                .resolvedIssues(resolved)
                .closedIssues(completed)
                .averageResolutionTimeHours(avgResolutionTime)
                .slaCompliancePercentage(slaCompliance)
                .issuesByPriority(priorityStats)
                .build();
    }

    /**
     * Get issues grouped by category
     */
    private List<CategoryStats> getIssuesByCategory() {
        GroupOperation groupByCategory = group("categoryName").count().as("count");
        ProjectionOperation project = project("count")
                .and("_id").as("categoryName");
        SortOperation sort = sort(Sort.Direction.DESC, "count");

        Aggregation aggregation = newAggregation(groupByCategory, project, sort);
        
        List<Map> results = mongoTemplate.aggregate(aggregation, "issues", Map.class)
                .getMappedResults();

        long total = issueRepository.count();

        return results.stream()
                .map(map -> CategoryStats.builder()
                        .categoryName((String) map.get("categoryName"))
                        .count(((Number) map.get("count")).longValue())
                        .percentage(total > 0 ? (((Number) map.get("count")).doubleValue() / total * 100) : 0.0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get issues grouped by status
     */
    private List<StatusStats> getIssuesByStatus() {
        GroupOperation groupByStatus = group("status").count().as("count");
        ProjectionOperation project = project("count")
                .and("_id").as("status");
        SortOperation sort = sort(Sort.Direction.DESC, "count");

        Aggregation aggregation = newAggregation(groupByStatus, project, sort);
        
        List<Map> results = mongoTemplate.aggregate(aggregation, "issues", Map.class)
                .getMappedResults();

        long total = issueRepository.count();

        return results.stream()
                .map(map -> StatusStats.builder()
                        .status((String) map.get("status"))
                        .count(((Number) map.get("count")).longValue())
                        .percentage(total > 0 ? (((Number) map.get("count")).doubleValue() / total * 100) : 0.0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get issues grouped by priority
     */
    private List<PriorityStats> getIssuesByPriority() {
        GroupOperation groupByPriority = group("priority").count().as("count");
        ProjectionOperation project = project("count")
                .and("_id").as("priority");
        SortOperation sort = sort(Sort.Direction.DESC, "count");

        Aggregation aggregation = newAggregation(groupByPriority, project, sort);
        
        List<Map> results = mongoTemplate.aggregate(aggregation, "issues", Map.class)
                .getMappedResults();

        long total = issueRepository.count();

        return results.stream()
                .map(map -> PriorityStats.builder()
                        .priority((String) map.get("priority"))
                        .count(((Number) map.get("count")).longValue())
                        .percentage(total > 0 ? (((Number) map.get("count")).doubleValue() / total * 100) : 0.0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Get issues trend over time
     */
    private List<TrendStats> getIssuesTrend(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        
        List<TrendStats> trendStats = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

            long created = issueRepository.findByCreatedAtBetween(dayStart, dayEnd).size();
            
            // Count resolved issues
            long resolved = issueRepository.findAll().stream()
                    .filter(issue -> issue.getResolvedAt() != null &&
                            !issue.getResolvedAt().isBefore(dayStart) &&
                            issue.getResolvedAt().isBefore(dayEnd))
                    .count();

            // Count closed issues
            long closed = issueRepository.findAll().stream()
                    .filter(issue -> issue.getClosedAt() != null &&
                            !issue.getClosedAt().isBefore(dayStart) &&
                            issue.getClosedAt().isBefore(dayEnd))
                    .count();

            trendStats.add(TrendStats.builder()
                    .date(date.format(formatter))
                    .created(created)
                    .resolved(resolved)
                    .closed(closed)
                    .build());
        }

        return trendStats;
    }

    /**
     * Get technician performance statistics
     */
    private List<TechnicianPerformance> getTechnicianPerformance() {
        List<User> technicians = userRepository.findByRole(User.UserRole.TECHNICIAN);
        
        return technicians.stream()
                .map(tech -> {
                    long assigned = issueRepository.countByAssignedTo(tech.getId());
                    long resolved = countIssuesByTechnicianAndStatus(tech.getId(), 
                            Collections.singletonList(Issue.Status.RESOLVED));
                    long inProgress = countIssuesByTechnicianAndStatus(tech.getId(), 
                            Collections.singletonList(Issue.Status.IN_PROGRESS));
                    
                    Double avgResolution = calculateAverageResolutionTimeForTechnician(tech.getId());
                    Double slaCompliance = calculateSlaComplianceForTechnician(tech.getId());

                    return TechnicianPerformance.builder()
                            .technicianId(tech.getId())
                            .technicianName(tech.getName())
                            .assignedIssues(assigned)
                            .resolvedIssues(resolved)
                            .inProgressIssues(inProgress)
                            .averageResolutionTimeHours(avgResolution)
                            .slaCompliancePercentage(slaCompliance)
                            .build();
                })
                .sorted(Comparator.comparing(TechnicianPerformance::getResolvedIssues).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Calculate average resolution time (in hours)
     */
    private Double calculateAverageResolutionTime() {
        List<Issue> resolvedIssues = issueRepository.findByStatus(Issue.Status.RESOLVED);
        
        if (resolvedIssues.isEmpty()) {
            return 0.0;
        }

        double totalHours = resolvedIssues.stream()
                .filter(issue -> issue.getResolvedAt() != null)
                .mapToDouble(issue -> {
                    Duration duration = Duration.between(issue.getCreatedAt(), issue.getResolvedAt());
                    return duration.toHours();
                })
                .average()
                .orElse(0.0);

        return Math.round(totalHours * 100.0) / 100.0;
    }

    /**
     * Calculate average resolution time for a specific technician
     */
    private Double calculateAverageResolutionTimeForTechnician(String technicianId) {
        List<Issue> resolvedIssues = issueRepository.findByStatusAndAssignedTo(
                Issue.Status.RESOLVED, technicianId);
        
        if (resolvedIssues.isEmpty()) {
            return 0.0;
        }

        double totalHours = resolvedIssues.stream()
                .filter(issue -> issue.getResolvedAt() != null)
                .mapToDouble(issue -> {
                    Duration duration = Duration.between(issue.getCreatedAt(), issue.getResolvedAt());
                    return duration.toHours();
                })
                .average()
                .orElse(0.0);

        return Math.round(totalHours * 100.0) / 100.0;
    }

    /**
     * Calculate SLA compliance percentage
     */
    private Double calculateSlaCompliance() {
        List<Issue> allIssues = issueRepository.findAll();
        
        if (allIssues.isEmpty()) {
            return 100.0;
        }

        long compliantIssues = allIssues.stream()
                .filter(issue -> !Boolean.TRUE.equals(issue.getSlaBreached()))
                .count();

        double percentage = (double) compliantIssues / allIssues.size() * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }

    /**
     * Calculate SLA compliance for a specific technician
     */
    private Double calculateSlaComplianceForTechnician(String technicianId) {
        List<Issue> techIssues = issueRepository.findByAssignedTo(technicianId);
        
        if (techIssues.isEmpty()) {
            return 100.0;
        }

        long compliantIssues = techIssues.stream()
                .filter(issue -> !Boolean.TRUE.equals(issue.getSlaBreached()))
                .count();

        double percentage = (double) compliantIssues / techIssues.size() * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }

    // Helper methods
    
    private long countIssuesByStatuses(List<Issue.Status> statuses) {
        return statuses.stream()
                .mapToLong(issueRepository::countByStatus)
                .sum();
    }

    private long countIssuesByReporterAndStatus(String userId, List<Issue.Status> statuses) {
        return issueRepository.findByReportedBy(userId).stream()
                .filter(issue -> statuses.contains(issue.getStatus()))
                .count();
    }

    private long countIssuesByTechnicianAndStatus(String technicianId, List<Issue.Status> statuses) {
        return issueRepository.findByAssignedTo(technicianId).stream()
                .filter(issue -> statuses.contains(issue.getStatus()))
                .count();
    }

    private List<StatusStats> getIssuesByStatusForUser(String userId) {
        List<Issue> userIssues = issueRepository.findByReportedBy(userId);
        long total = userIssues.size();

        Map<Issue.Status, Long> statusCounts = userIssues.stream()
                .collect(Collectors.groupingBy(Issue::getStatus, Collectors.counting()));

        return statusCounts.entrySet().stream()
                .map(entry -> StatusStats.builder()
                        .status(entry.getKey().name())
                        .count(entry.getValue())
                        .percentage(total > 0 ? (entry.getValue().doubleValue() / total * 100) : 0.0)
                        .build())
                .sorted(Comparator.comparing(StatusStats::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<PriorityStats> getIssuesByPriorityForTechnician(String technicianId) {
        List<Issue> techIssues = issueRepository.findByAssignedTo(technicianId);
        long total = techIssues.size();

        Map<Issue.Priority, Long> priorityCounts = techIssues.stream()
                .collect(Collectors.groupingBy(Issue::getPriority, Collectors.counting()));

        return priorityCounts.entrySet().stream()
                .map(entry -> PriorityStats.builder()
                        .priority(entry.getKey().name())
                        .count(entry.getValue())
                        .percentage(total > 0 ? (entry.getValue().doubleValue() / total * 100) : 0.0)
                        .build())
                .sorted(Comparator.comparing(PriorityStats::getCount).reversed())
                .collect(Collectors.toList());
    }
}
