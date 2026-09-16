# Phase 8 Testing Guide - Categories & Notifications

Complete testing guide for Categories and Notifications features.

## 🎯 Sample Credentials (Auto-seeded on first run)

The application automatically seeds sample data on first startup:

| Role | Email | Password | Employee ID |
|------|-------|----------|-------------|
| **Admin** | admin@issuehub.com | admin123 | ADMIN001 |
| **Manager** | manager@issuehub.com | manager123 | MGR001 |
| **Technician 1** | tech1@issuehub.com | tech123 | TECH001 |
| **Technician 2** | tech2@issuehub.com | tech123 | TECH002 |
| **Employee 1** | employee1@issuehub.com | emp123 | EMP001 |
| **Employee 2** | employee2@issuehub.com | emp123 | EMP002 |

### Pre-seeded Categories:
1. Electrical
2. Plumbing
3. IT Support
4. Housekeeping
5. Furniture
6. Security
7. Safety
8. Internet/Network
9. HVAC
10. Other

---

## 1. Category Management APIs

### 1.1 Get All Categories

**Endpoint:** `GET /api/categories`

**Query Parameters:**
- `activeOnly` (optional, default: false) - Get only active categories

**Example:**
```bash
# Get all categories
curl -X GET "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer YOUR_TOKEN"

# Get only active categories
curl -X GET "http://localhost:8080/api/categories?activeOnly=true" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Categories retrieved successfully",
  "data": [
    {
      "id": "6501234abcdef",
      "name": "Electrical",
      "description": "Electrical-related problems",
      "isActive": true,
      "createdAt": "2026-09-16T10:00:00"
    }
  ]
}
```

### 1.2 Get Category by ID

**Endpoint:** `GET /api/categories/{id}`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/categories/CATEGORY_ID" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 1.3 Create Category

**Endpoint:** `POST /api/categories`

**Roles:** MANAGER, ADMIN

**Request Body:**
```json
{
  "name": "Building Maintenance",
  "description": "General building maintenance issues"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Building Maintenance\",\"description\":\"General building maintenance issues\"}"
```

### 1.4 Update Category

**Endpoint:** `PUT /api/categories/{id}`

**Roles:** MANAGER, ADMIN

**Request Body:**
```json
{
  "name": "Electrical & Power",
  "description": "Updated description for electrical issues"
}
```

**Example:**
```bash
curl -X PUT "http://localhost:8080/api/categories/CATEGORY_ID" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Electrical & Power\",\"description\":\"Updated description\"}"
```

### 1.5 Toggle Category Status

**Endpoint:** `PATCH /api/categories/{id}/toggle-status`

**Roles:** MANAGER, ADMIN

**Description:** Activates or deactivates a category

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/categories/CATEGORY_ID/toggle-status" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### 1.6 Delete Category

**Endpoint:** `DELETE /api/categories/{id}`

**Roles:** ADMIN only

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/categories/CATEGORY_ID" ^
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

## 2. Notification APIs

### 2.1 Get All Notifications

**Endpoint:** `GET /api/notifications`

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)

**Example:**
```bash
curl -X GET "http://localhost:8080/api/notifications?page=0&size=20" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Notifications retrieved successfully",
  "data": {
    "content": [
      {
        "id": "notif123",
        "userId": "user123",
        "issueId": "issue123",
        "title": "Issue Assigned",
        "message": "Issue 'AC not working' has been assigned to you.",
        "type": "ISSUE_ASSIGNED",
        "isRead": false,
        "createdAt": "2026-09-16T10:30:00"
      }
    ],
    "totalPages": 1,
    "totalElements": 5,
    "number": 0,
    "size": 20
  }
}
```

### 2.2 Get Unread Notifications

**Endpoint:** `GET /api/notifications/unread`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/notifications/unread" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.3 Get Unread Notification Count

**Endpoint:** `GET /api/notifications/unread/count`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/notifications/unread/count" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Unread count retrieved",
  "data": {
    "count": 5
  }
}
```

### 2.4 Mark Notification as Read

**Endpoint:** `PATCH /api/notifications/{id}/read`

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/notifications/NOTIF_ID/read" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.5 Mark All Notifications as Read

**Endpoint:** `PATCH /api/notifications/read-all`

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/notifications/read-all" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.6 Delete Notification

**Endpoint:** `DELETE /api/notifications/{id}`

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/notifications/NOTIF_ID" ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 3. Notification Types

The system automatically generates notifications for:

| Type | When Triggered | Notified User |
|------|---------------|---------------|
| `ISSUE_CREATED` | New issue is reported | Managers |
| `ISSUE_ASSIGNED` | Issue assigned to technician | Assigned technician |
| `ISSUE_REASSIGNED` | Issue reassigned to another technician | New technician |
| `STATUS_CHANGED` | Issue status changes | Issue reporter |
| `PRIORITY_CHANGED` | Issue priority changes | Issue reporter |
| `COMMENT_ADDED` | Comment added to issue | Issue reporter & technician |
| `ISSUE_RESOLVED` | Issue marked as resolved | Issue reporter |
| `ISSUE_REOPENED` | Issue reopened | Assigned technician |
| `ISSUE_CLOSED` | Issue closed | Issue reporter |
| `SLA_BREACH_WARNING` | SLA deadline approaching | Manager & technician |
| `SLA_BREACHED` | SLA deadline passed | Manager & technician |

---

## 4. Complete End-to-End Testing Workflow

### Step 1: Login with Pre-seeded Accounts

```bash
# Login as Employee
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"employee1@issuehub.com\",\"password\":\"emp123\"}"
# Save token as EMPLOYEE_TOKEN

# Login as Manager
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"manager@issuehub.com\",\"password\":\"manager123\"}"
# Save token as MANAGER_TOKEN

# Login as Technician
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"tech1@issuehub.com\",\"password\":\"tech123\"}"
# Save token as TECH_TOKEN
```

### Step 2: Employee Views Categories

```bash
curl -X GET "http://localhost:8080/api/categories?activeOnly=true" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

### Step 3: Employee Creates Issue

```bash
# Get a category ID from step 2, then create issue
curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"AC not working in Room 301\",\"description\":\"The air conditioning unit has completely stopped\",\"categoryId\":\"HVAC_CATEGORY_ID\",\"location\":{\"building\":\"Main Building\",\"floor\":\"3\",\"room\":\"301\"},\"priority\":\"HIGH\"}"
```

**Result:** Manager receives notification about new issue

### Step 4: Manager Views Notifications

```bash
# Check unread count
curl -X GET "http://localhost:8080/api/notifications/unread/count" ^
  -H "Authorization: Bearer MANAGER_TOKEN"

# View all unread notifications
curl -X GET "http://localhost:8080/api/notifications/unread" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### Step 5: Manager Reviews and Assigns Issue

```bash
# Get technician ID (login as tech or from user list)
# Then assign the issue
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/assign" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"technicianId\":\"TECH_USER_ID\",\"comment\":\"Assigned to John for immediate attention\"}"
```

**Result:** Technician receives notification about assignment

### Step 6: Technician Views Notifications

```bash
# Check notifications
curl -X GET "http://localhost:8080/api/notifications/unread" ^
  -H "Authorization: Bearer TECH_TOKEN"

# Mark notification as read
curl -X PATCH "http://localhost:8080/api/notifications/NOTIF_ID/read" ^
  -H "Authorization: Bearer TECH_TOKEN"
```

### Step 7: Technician Updates Issue

```bash
# Update status to IN_PROGRESS
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer TECH_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"IN_PROGRESS\",\"comment\":\"Started working on the AC repair\"}"

# Add a comment
curl -X POST "http://localhost:8080/api/issues/ISSUE_ID/comments" ^
  -H "Authorization: Bearer TECH_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"The compressor needs replacement. ETA: 2 hours\"}"
```

**Result:** Employee receives notifications about status change and comment

### Step 8: Employee Views Notifications

```bash
curl -X GET "http://localhost:8080/api/notifications/unread" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

### Step 9: Technician Resolves Issue

```bash
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer TECH_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"RESOLVED\",\"comment\":\"AC repaired. Compressor replaced and tested\"}"
```

**Result:** Employee receives notification about resolution

### Step 10: View Complete Notification History

```bash
curl -X GET "http://localhost:8080/api/notifications?page=0&size=50" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

---

## 5. Testing Category Management

### Create Custom Category (Manager/Admin)

```bash
curl -X POST "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Parking\",\"description\":\"Parking lot and garage issues\"}"
```

### Update Category

```bash
curl -X PUT "http://localhost:8080/api/categories/CATEGORY_ID" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Parking & Transportation\",\"description\":\"Parking and transportation related issues\"}"
```

### Deactivate Category

```bash
curl -X PATCH "http://localhost:8080/api/categories/CATEGORY_ID/toggle-status" ^
  -H "Authorization: Bearer MANAGER_TOKEN"
```

### Verify Category is Inactive

```bash
curl -X GET "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN"
```

---

## 6. Expected Behavior

### Automatic Notifications Are Generated For:

1. **Issue Created** → Managers get notified
2. **Issue Assigned** → Technician gets notified
3. **Status Changed** → Reporter gets notified
4. **Priority Changed** → Reporter gets notified
5. **Comment Added** → Reporter and Technician get notified
6. **Issue Resolved** → Reporter gets notified
7. **Issue Reopened** → Technician gets notified
8. **Issue Closed** → Reporter gets notified

### Category Features:

1. **Pre-seeded Categories** - 10 default categories available on first run
2. **CRUD Operations** - Full Create, Read, Update, Delete support
3. **Status Toggle** - Activate/Deactivate categories without deletion
4. **Duplicate Prevention** - Cannot create categories with duplicate names
5. **Role-Based Access** - Only Managers/Admins can modify categories

---

## 7. Validation Testing

### Test Duplicate Category Name

```bash
curl -X POST "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Electrical\",\"description\":\"Duplicate test\"}"
```

**Expected:** 409 CONFLICT error

### Test Invalid Category Data

```bash
curl -X POST "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"A\",\"description\":\"Too short name\"}"
```

**Expected:** 400 BAD REQUEST with validation errors

### Test Unauthorized Access

```bash
# Try to create category as Employee (should fail)
curl -X POST "http://localhost:8080/api/categories" ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Test Category\",\"description\":\"Should fail\"}"
```

**Expected:** 403 FORBIDDEN error

---

## 8. Data Seeding

On the first application startup, the system automatically:

1. ✅ Seeds 10 predefined categories
2. ✅ Seeds 6 sample users (1 Admin, 1 Manager, 2 Technicians, 2 Employees)
3. ✅ Displays login credentials in console

### To Re-seed Data:

1. Drop the MongoDB database
2. Restart the application
3. Check console for seeded credentials

---

## 9. Tips

1. **Save JWTtokens** from login responses for testing
2. **Check console logs** for seeded user credentials on first run
3. **Use Postman** or Thunder Client for easier testing
4. **Test notification flow** by creating and managing issues
5. **Verify role-based access** by trying operations with different user roles
6. **Monitor MongoDB** to see notifications being created in real-time

---

Happy Testing! 🎉
