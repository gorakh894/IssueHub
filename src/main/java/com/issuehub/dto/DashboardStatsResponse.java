package com.issuehub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Dashboard Statistics Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {

    // Overall statistics
    private Long totalIssues;
    private Long openIssues;
    private Long inProgressIssues;
    private Long resolvedIssues;
    private Long closedIssues;
    private Long highPriorityIssues;
    private Long criticalPriorityIssues;
    private Long slaBreachedIssues;
    
    // User-specific stats
    private Long myIssues;
    private Long assignedToMe;
    private Long pendingAssignment;
    
    // Charts data
    private List<CategoryStats> issuesByCategory;
    private List<StatusStats> issuesByStatus;
    private List<PriorityStats> issuesByPriority;
    private List<TrendStats> issuesTrend;
    private List<TechnicianPerformance> technicianPerformance;
    
    // Average metrics
    private Double averageResolutionTimeHours;
    private Double averageResponseTimeHours;
    private Double slaCompliancePercentage;

    /**
     * Category Statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryStats {
        private String categoryName;
        private Long count;
        private Double percentage;
    }

    /**
     * Status Statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusStats {
        private String status;
        private Long count;
        private Double percentage;
    }

    /**
     * Priority Statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriorityStats {
        private String priority;
        private Long count;
        private Double percentage;
    }

    /**
     * Trend Statistics (time-based)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TrendStats {
        private String date;
        private Long created;
        private Long resolved;
        private Long closed;
    }

    /**
     * Technician Performance Statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TechnicianPerformance {
        private String technicianId;
        private String technicianName;
        private Long assignedIssues;
        private Long resolvedIssues;
        private Long inProgressIssues;
        private Double averageResolutionTimeHours;
        private Double slaCompliancePercentage;
    }
}
