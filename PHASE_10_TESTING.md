# Phase 10 Testing Guide - Dashboard & Analytics

Complete testing guide for dashboard statistics and analytics features.

## 📊 Dashboard Overview

Three role-specific dashboards with real-time statistics:
1. **Employee Dashboard** - Personal issue tracking
2. **Manager Dashboard** - Complete analytics and team overview
3. **Technician Dashboard** - Workload and performance metrics

---

## 1. Employee Dashboard

### Endpoint
```
GET /api/dashboard/employee
```

### Authentication
**Role:** EMPLOYEE

### What You Get
- Total issues reported by you
- Open issues count
- In progress issues count
- Resolved issues count
- Closed issues count
- Issues breakdown by status (chart data)

### Example Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/employee" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

### Example Response
```json
{
  "success": true,
  "message": "Employee dashboard retrieved successfully",
  "data": {
    "totalIssues": 15,
    "myIssues": 15,
    "openIssues": 8,
    "inProgressIssues": 3,
    "resolvedIssues": 2,
    "closedIssues": 2,
    "issuesByStatus": [
      {
        "status": "REPORTED",
        "count": 5,
        "percentage": 33.33
      },
      {
        "status": "IN_PROGRESS",
        "count": 3,
        "percentage": 20.0
      },
      {
        "status": "RESOLVED",
        "count": 2,
        "percentage": 13.33
      }
    ]
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

---

## 2. Manager Dashboard

### Endpoint
```
GET /api/dashboard/manager
```

### Authentication
**Roles:** MANAGER, ADMIN

### What You Get
- **Overall Statistics:**
  - Total issues in system
  - Open issues
  - In progress issues
  - Resolved issues
  - Closed issues
  - High priority issues
  - Critical priority issues
  - Pending assignment
  - SLA breached issues

- **Analytics:**
  - Average resolution time (hours)
  - SLA compliance percentage
  
- **Chart Data:**
  - Issues by category
  - Issues by status
  - Issues by priority
  - Issues trend (last 7 days)
  - Technician performance metrics

### Example Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/manager" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### Example Response
```json
{
  "success": true,
  "message": "Manager dashboard retrieved successfully",
  "data": {
    "totalIssues": 150,
    "openIssues": 45,
    "inProgressIssues": 30,
    "resolvedIssues": 50,
    "closedIssues": 25,
    "highPriorityIssues": 20,
    "criticalPriorityIssues": 8,
    "pendingAssignment": 15,
    "slaBreachedIssues": 5,
    "averageResolutionTimeHours": 18.5,
    "slaCompliancePercentage": 96.67,
    "issuesByCategory": [
      {
        "categoryName": "IT Support",
        "count": 45,
        "percentage": 30.0
      },
      {
        "categoryName": "Electrical",
        "count": 30,
        "percentage": 20.0
      },
      {
        "categoryName": "Plumbing",
        "count": 25,
        "percentage": 16.67
      }
    ],
    "issuesByStatus": [
      {
        "status": "IN_PROGRESS",
        "count": 30,
        "percentage": 20.0
      },
      {
        "status": "RESOLVED",
        "count": 50,
        "percentage": 33.33
      }
    ],
    "issuesByPriority": [
      {
        "priority": "MEDIUM",
        "count": 80,
        "percentage": 53.33
      },
      {
        "priority": "HIGH",
        "count": 40,
        "percentage": 26.67
      }
    ],
    "issuesTrend": [
      {
        "date": "2026-09-10",
        "created": 8,
        "resolved": 5,
        "closed": 3
      },
      {
        "date": "2026-09-11",
        "created": 12,
        "resolved": 7,
        "closed": 4
      }
    ],
    "technicianPerformance": [
      {
        "technicianId": "tech1",
        "technicianName": "John Technician",
        "assignedIssues": 45,
        "resolvedIssues": 30,
        "inProgressIssues": 10,
        "averageResolutionTimeHours": 15.5,
        "slaCompliancePercentage": 98.0
      },
      {
        "technicianId": "tech2",
        "technicianName": "Sarah Technician",
        "assignedIssues": 40,
        "resolvedIssues": 28,
        "inProgressIssues": 8,
        "averageResolutionTimeHours": 17.2,
        "slaCompliancePercentage": 95.5
      }
    ]
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

---

## 3. Technician Dashboard

### Endpoint
```
GET /api/dashboard/technician
```

### Authentication
**Role:** TECHNICIAN

### What You Get
- Total assigned issues
- Issues assigned to me
- Open (pending) issues
- In progress issues
- Resolved issues
- Completed issues (resolved + closed)
- Average resolution time
- SLA compliance percentage
- Issues breakdown by priority

### Example Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/technician" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN"
```

### Example Response
```json
{
  "success": true,
  "message": "Technician dashboard retrieved successfully",
  "data": {
    "totalIssues": 45,
    "assignedToMe": 45,
    "openIssues": 5,
    "inProgressIssues": 10,
    "resolvedIssues": 25,
    "closedIssues": 30,
    "averageResolutionTimeHours": 16.8,
    "slaCompliancePercentage": 97.5,
    "issuesByPriority": [
      {
        "priority": "MEDIUM",
        "count": 20,
        "percentage": 44.44
      },
      {
        "priority": "HIGH",
        "count": 15,
        "percentage": 33.33
      },
      {
        "priority": "CRITICAL",
        "count": 8,
        "percentage": 17.78
      },
      {
        "priority": "LOW",
        "count": 2,
        "percentage": 4.44
      }
    ]
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

---

## 4. Complete Testing Workflow

### Step 1: Create Sample Data

First, create some issues to populate the dashboard:

```bash
# Login as employee
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"employee1@issuehub.com\",\"password\":\"emp123\"}"

# Create multiple issues with different priorities
curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Issue 1\",\"description\":\"Test issue\",\"categoryId\":\"CATEGORY_ID\",\"location\":{\"building\":\"Main\",\"floor\":\"1\",\"room\":\"101\"},\"priority\":\"HIGH\"}"

curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Issue 2\",\"description\":\"Test issue\",\"categoryId\":\"CATEGORY_ID\",\"location\":{\"building\":\"Main\",\"floor\":\"2\",\"room\":\"201\"},\"priority\":\"MEDIUM\"}"

curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Issue 3\",\"description\":\"Test issue\",\"categoryId\":\"CATEGORY_ID\",\"location\":{\"building\":\"Main\",\"floor\":\"3\",\"room\":\"301\"},\"priority\":\"CRITICAL\"}"
```

### Step 2: Manager Assigns Issues

```bash
# Login as manager
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"manager@issuehub.com\",\"password\":\"manager123\"}"

# Assign issues to technician
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/assign" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"technicianId\":\"TECH_USER_ID\",\"comment\":\"Assigned for resolution\"}"
```

### Step 3: Technician Updates Issues

```bash
# Login as technician
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"tech1@issuehub.com\",\"password\":\"tech123\"}"

# Update to IN_PROGRESS
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"IN_PROGRESS\",\"comment\":\"Working on it\"}"

# Mark as RESOLVED
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"RESOLVED\",\"comment\":\"Fixed the issue\"}"
```

### Step 4: View Dashboards

```bash
# Employee Dashboard
curl -X GET "http://localhost:8080/api/dashboard/employee" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"

# Manager Dashboard
curl -X GET "http://localhost:8080/api/dashboard/manager" ^
  -H "Authorization: Bearer MANAGER_TOKEN"

# Technician Dashboard
curl -X GET "http://localhost:8080/api/dashboard/technician" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN"
```

---

## 5. Dashboard Metrics Explained

### Average Resolution Time
- **Definition:** Average time (in hours) from issue creation to resolution
- **Calculation:** Sum of (resolvedAt - createdAt) / Number of resolved issues
- **Good Target:** < 24 hours for most issues

### SLA Compliance Percentage
- **Definition:** Percentage of issues resolved within SLA deadline
- **Calculation:** (Non-breached issues / Total issues) × 100
- **Good Target:** > 95%

### Issues Trend
- **Definition:** Daily count of created, resolved, and closed issues
- **Time Range:** Last 7 days
- **Use Case:** Identify patterns and workload trends

### Technician Performance
- **Metrics per Technician:**
  - Total assigned issues
  - Resolved issues count
  - In progress issues count
  - Average resolution time
  - SLA compliance percentage
- **Sorting:** By resolved issues (descending)

---

## 6. Dashboard Use Cases

### For Employees
✅ Track your reported issues
✅ Monitor issue status
✅ See resolution progress
✅ Identify patterns in your reports

### For Managers
✅ Overall system health monitoring
✅ Resource allocation (assign to least busy technician)
✅ Identify bottlenecks (pending assignments)
✅ SLA compliance tracking
✅ Category analysis (which categories need more resources)
✅ Technician performance comparison
✅ Trend analysis for capacity planning

### For Technicians
✅ Current workload overview
✅ Prioritize high/critical issues
✅ Track personal performance
✅ Monitor SLA compliance
✅ Identify workload distribution

---

## 7. MongoDB Aggregation Pipelines Used

The dashboard uses MongoDB aggregation pipelines for efficient data processing:

### Issues by Category
```javascript
db.issues.aggregate([
  { $group: { _id: "$categoryName", count: { $sum: 1 } } },
  { $project: { categoryName: "$_id", count: 1, _id: 0 } },
  { $sort: { count: -1 } }
])
```

### Issues by Status
```javascript
db.issues.aggregate([
  { $group: { _id: "$status", count: { $sum: 1 } } },
  { $project: { status: "$_id", count: 1, _id: 0 } },
  { $sort: { count: -1 } }
])
```

### Issues by Priority
```javascript
db.issues.aggregate([
  { $group: { _id: "$priority", count: { $sum: 1 } } },
  { $project: { priority: "$_id", count: 1, _id: 0 } },
  { $sort: { count: -1 } }
])
```

---

## 8. Performance Tips

### For Fast Dashboard Loading:
1. **Indexes:** MongoDB automatically creates indexes on frequently queried fields
2. **Caching:** Consider caching dashboard data for 1-5 minutes
3. **Pagination:** Use pagination for large datasets
4. **Lazy Loading:** Load heavy charts data on demand

### Optimized Fields:
- `status` - Indexed
- `priority` - Indexed
- `categoryId` - Indexed
- `assignedTo` - Indexed
- `reportedBy` - Indexed
- `createdAt` - Indexed

---

## 9. Testing Checklist

### Employee Dashboard:
- [ ] Shows correct total issues count
- [ ] Shows only my reported issues
- [ ] Status breakdown is accurate
- [ ] Percentages add up to 100%

### Manager Dashboard:
- [ ] Shows all system issues
- [ ] Statistics are accurate
- [ ] Category chart has all categories
- [ ] Status chart has all statuses
- [ ] Priority chart has all priorities
- [ ] Trend shows last 7 days
- [ ] Technician performance list is complete
- [ ] SLA metrics are calculated correctly

### Technician Dashboard:
- [ ] Shows only assigned issues
- [ ] Workload metrics are accurate
- [ ] Priority breakdown is correct
- [ ] Personal SLA compliance is shown
- [ ] Resolution time is calculated

---

## 10. Common Scenarios

### Scenario 1: New System (No Data)
**Expected:** All counts will be 0, percentages will be 0.0, empty arrays for charts

### Scenario 2: Only Created Issues (Not Assigned)
**Expected:**
- Employee: Shows created issues
- Manager: High "pendingAssignment" count
- Technician: No data (nothing assigned yet)

### Scenario 3: All Issues Resolved
**Expected:**
- High "resolvedIssues" and "closedIssues" counts
- Low "openIssues" and "inProgressIssues" counts
- Good SLA compliance if resolved on time
- Positive trend in "resolved" and "closed" metrics

### Scenario 4: SLA Breaches
**Expected:**
- "slaBreachedIssues" count increases
- "slaCompliancePercentage" decreases
- Manager can identify which technician has breaches

---

## 11. Real-Time Updates

Dashboards reflect real-time data:
- ✅ Issue creation immediately updates counts
- ✅ Status changes update statistics
- ✅ Assignment updates technician workload
- ✅ Resolution updates completion metrics

**Note:** Frontend should refresh dashboard periodically (every 30-60 seconds) or use WebSocket for real-time updates.

---

Happy Analyzing! 📊🚀
