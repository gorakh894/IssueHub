# IssueHub API Testing Guide

Complete API testing guide for IssueHub application.

## Base URL
```
http://localhost:8080
```

## Authentication

All endpoints (except `/api/health/**` and `/api/auth/**`) require JWT authentication.

Include the JWT token in the `Authorization` header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 1. Authentication APIs

### 1.1 Register User

**Endpoint:** `POST /api/auth/register`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "9876543210",
  "role": "EMPLOYEE",
  "department": "IT",
  "employeeId": "EMP001"
}
```

**Available Roles:** `EMPLOYEE`, `MANAGER`, `TECHNICIAN`, `ADMIN`

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\",\"phone\":\"9876543210\",\"role\":\"EMPLOYEE\",\"department\":\"IT\",\"employeeId\":\"EMP001\"}"
```

### 1.2 Login

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"john.doe@example.com\",\"password\":\"password123\"}"
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": "66f4a1b2c3d4e5f6g7h8i9j0",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "role": "EMPLOYEE"
    }
  }
}
```

**Save the token for subsequent requests!**

### 1.3 Get Current User

**Endpoint:** `GET /api/auth/me`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**cURL Command:**
```bash
curl -X GET http://localhost:8080/api/auth/me ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 2. Issue APIs

### 2.1 Create Issue

**Endpoint:** `POST /api/issues`

**Roles:** EMPLOYEE, MANAGER, ADMIN

**Headers:**
```
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Air conditioner not working in Lab 3",
  "description": "The AC unit in Lab 3 has completely stopped working. Temperature is rising and affecting work.",
  "categoryId": "CATEGORY_ID_HERE",
  "location": {
    "building": "Main Building",
    "floor": "2",
    "room": "Lab 3"
  },
  "priority": "HIGH",
  "attachments": [
    "https://cloudinary.com/image1.jpg"
  ]
}
```

**Priority Values:** `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

**cURL Command:**
```bash
curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"AC not working\",\"description\":\"Air conditioner in Lab 3 is not functioning\",\"categoryId\":\"CATEGORY_ID\",\"location\":{\"building\":\"Main Building\",\"floor\":\"2\",\"room\":\"Lab 3\"},\"priority\":\"HIGH\"}"
```

### 2.2 Get All Issues

**Endpoint:** `GET /api/issues`

**Roles:** ALL (EMPLOYEE, MANAGER, TECHNICIAN, ADMIN)

**Query Parameters:**
- `status` (optional): Filter by status
- `priority` (optional): Filter by priority
- `categoryId` (optional): Filter by category
- `assignedTo` (optional): Filter by assigned technician
- `reportedBy` (optional): Filter by reporter
- `page` (default: 0): Page number
- `size` (default: 10): Items per page
- `sortBy` (default: createdAt): Sort field
- `sortDir` (default: DESC): Sort direction (ASC/DESC)

**Examples:**
```bash
# Get all issues
curl -X GET "http://localhost:8080/api/issues" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Filter by status
curl -X GET "http://localhost:8080/api/issues?status=IN_PROGRESS" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Filter by priority
curl -X GET "http://localhost:8080/api/issues?priority=HIGH" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Pagination
curl -X GET "http://localhost:8080/api/issues?page=0&size=20" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Multiple filters
curl -X GET "http://localhost:8080/api/issues?status=ASSIGNED&priority=CRITICAL&page=0&size=10" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Status Values:** `REPORTED`, `UNDER_REVIEW`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `REOPENED`, `CANCELLED`, `ON_HOLD`

### 2.3 Search Issues

**Endpoint:** `GET /api/issues/search`

**Query Parameters:**
- `keyword` (required): Search keyword
- `page` (default: 0)
- `size` (default: 10)

**Example:**
```bash
curl -X GET "http://localhost:8080/api/issues/search?keyword=air%20conditioner" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.4 Get Issue by ID

**Endpoint:** `GET /api/issues/{id}`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/issues/66f4a1b2c3d4e5f6g7h8i9j0" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.5 Update Issue

**Endpoint:** `PUT /api/issues/{id}`

**Roles:** EMPLOYEE, MANAGER, ADMIN

**Request Body:**
```json
{
  "title": "Updated title",
  "description": "Updated description",
  "categoryId": "CATEGORY_ID",
  "location": {
    "building": "Main Building",
    "floor": "3",
    "room": "Lab 5"
  },
  "priority": "CRITICAL"
}
```

**cURL Command:**
```bash
curl -X PUT "http://localhost:8080/api/issues/ISSUE_ID" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Updated AC Issue\",\"description\":\"Updated description\",\"categoryId\":\"CAT_ID\",\"location\":{\"building\":\"Main\",\"floor\":\"2\",\"room\":\"Lab 3\"},\"priority\":\"CRITICAL\"}"
```

### 2.6 Delete Issue

**Endpoint:** `DELETE /api/issues/{id}`

**Roles:** MANAGER, ADMIN only

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/issues/ISSUE_ID" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 2.7 Update Issue Status

**Endpoint:** `PATCH /api/issues/{id}/status`

**Roles:** MANAGER, TECHNICIAN, ADMIN

**Request Body:**
```json
{
  "status": "IN_PROGRESS",
  "comment": "Started working on this issue"
}
```

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"IN_PROGRESS\",\"comment\":\"Started repair work\"}"
```

### 2.8 Update Issue Priority

**Endpoint:** `PATCH /api/issues/{id}/priority`

**Roles:** MANAGER, ADMIN only

**Request Body:**
```json
{
  "priority": "CRITICAL",
  "comment": "Escalating to critical priority"
}
```

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/priority" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"priority\":\"CRITICAL\",\"comment\":\"Escalating priority\"}"
```

### 2.9 Assign Issue to Technician

**Endpoint:** `PATCH /api/issues/{id}/assign`

**Roles:** MANAGER, ADMIN only

**Request Body:**
```json
{
  "technicianId": "TECHNICIAN_USER_ID",
  "comment": "Assigned to John for resolution"
}
```

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/assign" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"technicianId\":\"TECH_ID\",\"comment\":\"Assigned to technician\"}"
```

---

## 3. Comment APIs

### 3.1 Get Issue Comments

**Endpoint:** `GET /api/issues/{id}/comments`

**Roles:** ALL

**Example:**
```bash
curl -X GET "http://localhost:8080/api/issues/ISSUE_ID/comments" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### 3.2 Add Comment to Issue

**Endpoint:** `POST /api/issues/{id}/comments`

**Roles:** ALL

**Request Body:**
```json
{
  "message": "I have inspected the AC unit. The compressor needs replacement."
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/issues/ISSUE_ID/comments" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"Working on the issue now\"}"
```

---

## 4. Issue History API

### 4.1 Get Issue History

**Endpoint:** `GET /api/issues/{id}/history`

**Roles:** ALL

**Example:**
```bash
curl -X GET "http://localhost:8080/api/issues/ISSUE_ID/history" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**
```json
{
  "success": true,
  "message": "History retrieved successfully",
  "data": [
    {
      "id": "hist123",
      "issueId": "issue123",
      "changedBy": "user123",
      "changedByName": "John Manager",
      "action": "STATUS_CHANGE",
      "oldValue": "REPORTED",
      "newValue": "ASSIGNED",
      "comment": "Assigned to technician",
      "timestamp": "2026-09-16T10:30:00"
    }
  ]
}
```

---

## 5. Complete Testing Workflow

### Step 1: Register Users

```bash
# Register Employee
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Alice Employee\",\"email\":\"alice@example.com\",\"password\":\"pass123\",\"phone\":\"9876543210\",\"role\":\"EMPLOYEE\",\"department\":\"IT\",\"employeeId\":\"EMP001\"}"

# Register Manager
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Bob Manager\",\"email\":\"bob@example.com\",\"password\":\"pass123\",\"phone\":\"9876543211\",\"role\":\"MANAGER\",\"department\":\"Admin\",\"employeeId\":\"MGR001\"}"

# Register Technician
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Charlie Tech\",\"email\":\"charlie@example.com\",\"password\":\"pass123\",\"phone\":\"9876543212\",\"role\":\"TECHNICIAN\",\"department\":\"Maintenance\",\"employeeId\":\"TECH001\"}"
```

### Step 2: Login and Get Tokens

```bash
# Login as Employee
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"alice@example.com\",\"password\":\"pass123\"}"

# Save the token from response
```

### Step 3: Create Category (Admin Task)

First, you'll need to create categories. This will be covered in Phase 8.

### Step 4: Employee Creates Issue

```bash
curl -X POST http://localhost:8080/api/issues ^
  -H "Authorization: Bearer EMPLOYEE_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"AC not working\",\"description\":\"Air conditioner stopped working\",\"categoryId\":\"CATEGORY_ID\",\"location\":{\"building\":\"Main\",\"floor\":\"2\",\"room\":\"Lab 3\"},\"priority\":\"HIGH\"}"
```

### Step 5: Manager Reviews and Assigns

```bash
# Update status to UNDER_REVIEW
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"UNDER_REVIEW\",\"comment\":\"Reviewing the issue\"}"

# Assign to technician
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/assign" ^
  -H "Authorization: Bearer MANAGER_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"technicianId\":\"TECHNICIAN_USER_ID\",\"comment\":\"Assigned to Charlie\"}"
```

### Step 6: Technician Updates Progress

```bash
# Update status to IN_PROGRESS
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"IN_PROGRESS\",\"comment\":\"Started repair work\"}"

# Add comment
curl -X POST "http://localhost:8080/api/issues/ISSUE_ID/comments" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"Compressor needs replacement. ETA: 2 hours\"}"

# Mark as resolved
curl -X PATCH "http://localhost:8080/api/issues/ISSUE_ID/status" ^
  -H "Authorization: Bearer TECHNICIAN_TOKEN" ^
  -H "Content-Type: application/json" ^
  -d "{\"status\":\"RESOLVED\",\"comment\":\"Issue fixed. AC working now\"}"
```

### Step 7: View Issue History

```bash
curl -X GET "http://localhost:8080/api/issues/ISSUE_ID/history" ^
  -H "Authorization: Bearer ANY_USER_TOKEN"
```

---

## 6. SLA Management

Issues automatically track SLA based on priority:

| Priority | SLA Time |
|----------|----------|
| LOW      | 72 hours |
| MEDIUM   | 48 hours |
| HIGH     | 24 hours |
| CRITICAL | 4 hours  |

The system automatically calculates `slaDeadline` and marks `slaBreached` if deadline passes.

---

## 7. Response Formats

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2026-09-16T10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error message here",
  "data": null,
  "timestamp": "2026-09-16T10:30:00"
}
```

### Validation Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "title": "Title is required",
    "priority": "Priority must not be null"
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

---

## 8. HTTP Status Codes

- `200 OK` - Success
- `201 CREATED` - Resource created
- `400 BAD REQUEST` - Validation error or bad request
- `401 UNAUTHORIZED` - Authentication required or invalid token
- `403 FORBIDDEN` - Insufficient permissions
- `404 NOT FOUND` - Resource not found
- `409 CONFLICT` - Duplicate resource (e.g., email already exists)
- `500 INTERNAL SERVER ERROR` - Server error

---

## Tips for Testing

1. **Use Postman or Thunder Client** for easier API testing with a GUI
2. **Save your JWT tokens** - they're valid for 24 hours by default
3. **Test role-based access** - Try accessing endpoints with different user roles
4. **Check validation** - Try sending invalid data to test validation rules
5. **Monitor SLA** - Create issues with different priorities and check `slaDeadline`
6. **Test pagination** - Create multiple issues and test pagination parameters

---

Happy Testing! 🚀
