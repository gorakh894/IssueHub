# IssueHub - Centralized Issue Tracking & Resolution Platform

**Tagline:** Report. Track. Resolve.

## Overview

IssueHub is a comprehensive issue tracking and resolution platform designed for private organizations. It enables employees to report workplace and facility-related issues, managers to review and assign issues, and technicians to update and resolve them efficiently.

## Technology Stack

### Backend
- **Java 17+**
- **Spring Boot 3.2.5**
- **Spring Web**
- **Spring Data MongoDB**
- **Spring Security**
- **JWT Authentication**
- **Lombok**
- **Bean Validation**
- **Maven**

### Database
- **MongoDB**
- **MongoDB Atlas** (Cloud support)

### Frontend (To be implemented)
- React.js
- Vite
- HTML5/CSS3
- JavaScript/TypeScript
- Axios
- React Router

## Features

### User Roles
- **Employee**: Create and track issues
- **Manager/Admin**: Review, assign, and manage issues
- **Technician**: Accept, update, and resolve assigned issues

### Issue Categories
- Electrical
- Plumbing
- IT Support
- Housekeeping
- Furniture
- Security
- Safety
- Internet/Network
- HVAC
- Other

### Issue Lifecycle
```
REPORTED → UNDER_REVIEW → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED
Alternative states: REOPENED, CANCELLED, ON_HOLD
```

## Project Structure

```
src/main/java/com/issuehub/
├── config/                 # Configuration classes
│   ├── MongoConfig.java
│   ├── CorsConfig.java
│   └── SecurityConfig.java (Phase 4)
├── controller/            # REST controllers
│   └── HealthController.java
├── dto/                   # Data Transfer Objects (Phase 3+)
├── model/                 # MongoDB documents/entities (Phase 3+)
├── repository/            # MongoDB repositories (Phase 3+)
├── service/               # Business logic (Phase 3+)
├── security/              # Security components (Phase 4)
├── exception/             # Exception handling (Phase 3+)
├── util/                  # Utility classes
│   └── ApiResponse.java
└── IssueHubApplication.java
```

## Prerequisites

- **Java 17+** installed
- **Maven 3.6+** installed
- **MongoDB Atlas account** (or local MongoDB)
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd issuehub
```

### 2. Configure Environment Variables

Create a `.env` file in the project root (copy from `.env.example`):

```env
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/issuehub?retryWrites=true&w=majority
MONGODB_DATABASE=issuehub
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRATION=86400000
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
```

### 3. MongoDB Atlas Setup

1. Create a free MongoDB Atlas account at https://www.mongodb.com/cloud/atlas
2. Create a new cluster
3. Create a database user with read/write permissions
4. Whitelist your IP address (or use 0.0.0.0/0 for development)
5. Get your connection string and update `MONGODB_URI` in `.env`

### 4. Install Dependencies

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run from your IDE by executing `IssueHubApplication.java`

The application will start on `http://localhost:8080`

## 🔐 Sample Login Credentials (Auto-seeded on First Run)

The application automatically creates sample users and categories on first startup:

| Role | Email | Password | Employee ID |
|------|-------|----------|-------------|
| **Admin** | admin@issuehub.com | admin123 | ADMIN001 |
| **Manager** | manager@issuehub.com | manager123 | MGR001 |
| **Technician 1** | tech1@issuehub.com | tech123 | TECH001 |
| **Technician 2** | tech2@issuehub.com | tech123 | TECH002 |
| **Employee 1** | employee1@issuehub.com | emp123 | EMP001 |
| **Employee 2** | employee2@issuehub.com | emp123 | EMP002 |

**10 predefined categories** are also created: Electrical, Plumbing, IT Support, Housekeeping, Furniture, Security, Safety, Internet/Network, HVAC, Other

Check the console on first startup for the login credentials!

## API Endpoints

### Health Check Endpoints

#### 1. Basic Health Check
```http
GET http://localhost:8080/api/health
```

#### 2. MongoDB Connection Test
```http
GET http://localhost:8080/api/health/mongodb
```

#### 3. Application Info
```http
GET http://localhost:8080/api/health/info
```

### Authentication Endpoints

#### 1. Register New User
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

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

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": "66f4a1b2c3d4e5f6g7h8i9j0",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "9876543210",
      "role": "EMPLOYEE",
      "department": "IT",
      "employeeId": "EMP001",
      "isActive": true,
      "createdAt": "2026-09-16T10:30:00"
    }
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

**Available Roles:**
- `EMPLOYEE` - Regular employees who can report issues
- `MANAGER` - Managers who can review and assign issues
- `TECHNICIAN` - Technicians who resolve issues
- `ADMIN` - System administrators with full access

#### 2. Login User
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
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
      "role": "EMPLOYEE",
      "isActive": true
    }
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

#### 3. Get Current User (Authenticated)
```http
GET http://localhost:8080/api/auth/me
Authorization: Bearer <your-jwt-token>
```

**Response:**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": "66f4a1b2c3d4e5f6g7h8i9j0",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "9876543210",
    "role": "EMPLOYEE",
    "department": "IT",
    "employeeId": "EMP001",
    "isActive": true,
    "createdAt": "2026-09-16T10:30:00"
  },
  "timestamp": "2026-09-16T10:30:00"
}
```

## Testing

### Using cURL

```bash
# Health check
curl http://localhost:8080/api/health

# MongoDB connection test
curl http://localhost:8080/api/health/mongodb

# Application info
curl http://localhost:8080/api/health/info
```

### Using Postman

1. Import the provided Postman collection (to be created)
2. Test the health endpoints

## Build for Production

```bash
mvn clean package -DskipTests
```

The JAR file will be created in `target/issuehub-1.0.0.jar`

Run the production JAR:
```bash
java -jar target/issuehub-1.0.0.jar
```

## Development Phases

- [x] **Phase 1**: Spring Boot project setup ✅
- [x] **Phase 2**: Configure MongoDB Atlas ✅
- [x] **Phase 3**: Create User model and authentication ✅
- [x] **Phase 4**: Implement JWT security ✅
- [x] **Phase 5**: Create Issue model and CRUD APIs ✅
- [x] **Phase 6**: Implement issue assignment and status workflow ✅
- [x] **Phase 7**: Implement comments and issue history ✅
- [x] **Phase 8**: Implement categories and notifications ✅
- [x] **Phase 9**: Implement file uploads with Cloudinary ✅
- [x] **Phase 10**: Implement dashboard and MongoDB aggregation ✅
- [ ] **Phase 11**: Create React frontend
- [ ] **Phase 12**: Connect React frontend with REST APIs
- [ ] **Phase 13**: Implement WebSocket real-time updates
- [ ] **Phase 14**: Implement testing
- [ ] **Phase 15**: Deploy backend, MongoDB Atlas, and frontend

## 📚 Documentation

- **README.md** - Project overview and quick start
- **API_TESTING.md** - Complete API testing guide
- **PHASE_8_TESTING.md** - Categories & Notifications testing
- **PHASE_9_TESTING.md** - File upload testing guide  
- **PHASE_10_TESTING.md** - Dashboard & Analytics testing
- **DEPLOYMENT_GUIDE.md** - Complete deployment guide ✅ NEW
- **IssueHub_Postman_Collection.json** - Postman collection for API testing ✅ NEW

## 🚀 Quick Start

### 1. Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB Atlas account
- Cloudinary account

### 2. Setup

```bash
# Clone repository
git clone <your-repo-url>
cd issuehub

# Configure environment
copy .env.example .env
# Update .env with your credentials

# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run
```

### 3. Test the Application

```bash
# Health check
curl http://localhost:8080/api/health

# Login with pre-seeded user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"employee1@issuehub.com","password":"emp123"}'
```

### 4. Import Postman Collection

1. Open Postman
2. Click **Import**
3. Select `IssueHub_Postman_Collection.json`
4. Update `baseUrl` variable
5. Start testing!

## Security Notes

- Never commit `.env` file to version control
- Always use environment variables for sensitive data
- Use strong JWT secrets in production
- Enable MongoDB Atlas IP whitelisting in production
- Use HTTPS in production

## Support

For issues and questions, please create an issue in the repository.

## License

Copyright © 2026 IssueHub. All rights reserved.

---

**Current Status:** Phase 10 Complete - Dashboard & Analytics with MongoDB aggregation implemented! Real-time statistics for all user roles.
#   I s s u e H u b  
 