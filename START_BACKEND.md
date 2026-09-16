# Starting the Backend - Maven Not Found

## Issue
Maven (mvn) is not installed or not in your system PATH.

## ✅ Frontend Already Running!
Your frontend is successfully running at: **http://localhost:5173**

## 🔧 Options to Start Backend

### Option 1: Install Maven (Recommended)

**Download Maven:**
1. Go to: https://maven.apache.org/download.cgi
2. Download "Binary zip archive" (apache-maven-3.9.x-bin.zip)
3. Extract to: `C:\Program Files\Apache\maven`
4. Add to PATH:
   - Windows Key + Search "Environment Variables"
   - Edit "Path" variable
   - Add: `C:\Program Files\Apache\maven\bin`
5. Open NEW terminal and run: `mvn spring-boot:run`

### Option 2: Use Your IDE (Easiest)

**IntelliJ IDEA:**
1. Open project in IntelliJ
2. Wait for Maven dependencies to load
3. Find `IssueHubApplication.java`
4. Right-click → Run 'IssueHubApplication'

**Eclipse:**
1. Import as Maven project
2. Right-click project → Run As → Spring Boot App

**VS Code:**
1. Install "Spring Boot Extension Pack"
2. Open project
3. Press F5 to run

### Option 3: Create Maven Wrapper (One-time setup)

If you have Maven on another machine, you can create a wrapper:
```bash
mvn -N wrapper:wrapper
```

Then use:
```bash
.\mvnw.cmd spring-boot:run
```

### Option 4: Use Java Directly (Advanced)

You have Java installed at: `C:\Program Files\Java\jdk-23\bin\java.exe`

Build and run manually:
```bash
# Build (need Maven first)
mvn clean package

# Then run
java -jar target\issuehub-0.0.1-SNAPSHOT.jar
```

## 📋 What You Need Running

For full functionality:
- ✅ **Frontend**: http://localhost:5173 (RUNNING)
- ❌ **Backend**: http://localhost:8080 (NOT RUNNING)
- ❌ **MongoDB**: Connection needed (check .env file)
- ❌ **Cloudinary**: For file uploads (check .env file)

## 🚀 Quick Test (Frontend Only)

You can still test the frontend UI without backend:
1. Open http://localhost:5173
2. You'll see login page
3. UI components and routing will work
4. API calls will fail (no backend)

## ⚡ Recommended Next Steps

1. **Install Maven** (Option 1 above) - Takes 5 minutes
2. Verify installation: Open new terminal, run `mvn -version`
3. Start backend: `mvn spring-boot:run`
4. Wait for "Started IssueHubApplication" message
5. Open http://localhost:5173
6. Login with: `employee1@issuehub.com` / `emp123`

## 📝 Environment Setup Checklist

Before running backend, ensure `.env` file has:
```
MONGODB_URI=your_mongodb_connection_string
JWT_SECRET=your_jwt_secret
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

See `.env.example` for template.

---

**Current Status:**
- Frontend: ✅ Running on port 5173
- Backend: ❌ Waiting for Maven installation
- Total setup time: ~10 minutes
