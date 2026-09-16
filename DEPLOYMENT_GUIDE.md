# IssueHub - Deployment Guide

Complete guide for deploying IssueHub backend to production.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [MongoDB Atlas Setup](#mongodb-atlas-setup)
3. [Cloudinary Setup](#cloudinary-setup)
4. [Local Development](#local-development)
5. [Production Build](#production-build)
6. [Deployment Options](#deployment-options)
7. [Environment Variables](#environment-variables)
8. [Post-Deployment](#post-deployment)
9. [Monitoring & Maintenance](#monitoring--maintenance)
10. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software
- **Java 17+** (JDK 17 or higher)
- **Maven 3.6+**
- **Git**
- **MongoDB Atlas Account** (free tier available)
- **Cloudinary Account** (free tier available)

### Recommended Tools
- **Postman** (for API testing)
- **Docker** (optional, for containerization)
- **IDE** (IntelliJ IDEA, VS Code with Java extensions)

---

## MongoDB Atlas Setup

### 1. Create MongoDB Atlas Account

1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Sign up for a free account
3. Verify your email

### 2. Create a Cluster

1. Click **"Build a Database"**
2. Choose **FREE** tier (M0 Sandbox)
3. Select your preferred **Cloud Provider** (AWS, Google Cloud, or Azure)
4. Choose **Region** closest to your users
5. Click **"Create Cluster"** (takes 1-3 minutes)

### 3. Create Database User

1. Go to **Database Access** (left sidebar)
2. Click **"Add New Database User"**
3. Choose **Password** authentication
4. Enter:
   - Username: `issuehub_admin`
   - Password: Generate a strong password (save it!)
5. Set **Database User Privileges** to `Read and write to any database`
6. Click **"Add User"**

### 4. Configure Network Access

1. Go to **Network Access** (left sidebar)
2. Click **"Add IP Address"**
3. For development: Click **"Allow Access from Anywhere"** (0.0.0.0/0)
4. For production: Add your server's specific IP address
5. Click **"Confirm"**

### 5. Get Connection String

1. Go to **Database** (left sidebar)
2. Click **"Connect"** on your cluster
3. Choose **"Connect your application"**
4. Select **"Driver: Java"** and **"Version: 4.3 or later"**
5. Copy the connection string:
   ```
   mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
   ```
6. Replace:
   - `<username>` with `issuehub_admin`
   - `<password>` with your actual password
   - Add `/issuehub` before the `?` to specify database name:
   ```
   mongodb+srv://issuehub_admin:YourPassword@cluster0.xxxxx.mongodb.net/issuehub?retryWrites=true&w=majority
   ```

---

## Cloudinary Setup

### 1. Create Cloudinary Account

1. Go to [Cloudinary](https://cloudinary.com/)
2. Sign up for a free account
3. Verify your email

### 2. Get API Credentials

1. Go to **Dashboard**
2. Note down:
   - **Cloud Name**: `your-cloud-name`
   - **API Key**: `123456789012345`
   - **API Secret**: `abcdefghijklmnopqrstuvwxyz123456`

### 3. Configure Upload Settings (Optional)

1. Go to **Settings** → **Upload**
2. Enable **Auto Backup** (optional)
3. Set **Maximum File Size** if needed

---

## Local Development

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/issuehub.git
cd issuehub
```

### 2. Configure Environment Variables

Create `.env` file in project root:

```env
# MongoDB Configuration
MONGODB_URI=mongodb+srv://issuehub_admin:YourPassword@cluster0.xxxxx.mongodb.net/issuehub?retryWrites=true&w=majority
MONGODB_DATABASE=issuehub

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production-min-256-bits
JWT_EXPIRATION=86400000

# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000

# Server Port
SERVER_PORT=8080
```

### 3. Install Dependencies

```bash
mvn clean install
```

### 4. Run Application

```bash
mvn spring-boot:run
```

Application will start on `http://localhost:8080`

### 5. Test the Application

```bash
# Health check
curl http://localhost:8080/api/health

# MongoDB connection test
curl http://localhost:8080/api/health/mongodb
```

---

## Production Build

### 1. Update Production Environment Variables

Create `.env.production`:

```env
# MongoDB Configuration (Production)
MONGODB_URI=mongodb+srv://prod_user:SecurePassword@prod-cluster.xxxxx.mongodb.net/issuehub?retryWrites=true&w=majority
MONGODB_DATABASE=issuehub

# JWT Configuration (Strong Secret)
JWT_SECRET=Generate-A-Very-Strong-Secret-Key-Using-OpenSSL-Or-UUID-Generator
JWT_EXPIRATION=86400000

# Cloudinary Configuration (Production)
CLOUDINARY_CLOUD_NAME=your-prod-cloud-name
CLOUDINARY_API_KEY=your-prod-api-key
CLOUDINARY_API_SECRET=your-prod-api-secret

# CORS Configuration (Production Frontend URL)
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,https://www.your-frontend-domain.com

# Server Port
SERVER_PORT=8080
```

### 2. Build JAR File

```bash
mvn clean package -DskipTests
```

JAR file will be created: `target/issuehub-1.0.0.jar`

### 3. Test Production Build Locally

```bash
java -jar target/issuehub-1.0.0.jar
```

---

## Deployment Options

### Option 1: Render (Recommended - Free Tier Available)

#### Steps:

1. **Push to GitHub**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/yourusername/issuehub.git
   git push -u origin main
   ```

2. **Create Render Account**
   - Go to [Render](https://render.com/)
   - Sign up with GitHub

3. **Create New Web Service**
   - Click **"New +"** → **"Web Service"**
   - Connect your GitHub repository
   - Configure:
     - **Name**: `issuehub-api`
     - **Region**: Choose closest to your users
     - **Branch**: `main`
     - **Build Command**: `mvn clean package -DskipTests`
     - **Start Command**: `java -jar target/issuehub-1.0.0.jar`

4. **Add Environment Variables**
   - In Render dashboard, go to **Environment**
   - Add all variables from `.env.production`

5. **Deploy**
   - Click **"Create Web Service"**
   - Wait for deployment (5-10 minutes)
   - Your API will be available at: `https://issuehub-api.onrender.com`

#### Render Pricing:
- **Free Tier**: Perfect for testing
- **Starter ($7/month)**: For production with better performance

---

### Option 2: Railway

#### Steps:

1. **Push to GitHub** (same as Render)

2. **Create Railway Account**
   - Go to [Railway](https://railway.app/)
   - Sign up with GitHub

3. **Create New Project**
   - Click **"New Project"**
   - Choose **"Deploy from GitHub repo"**
   - Select your repository

4. **Configure Service**
   - Railway will auto-detect Java/Maven
   - Add environment variables in **Variables** tab

5. **Deploy**
   - Railway auto-deploys on push
   - Your API will be available at: `https://your-project.up.railway.app`

#### Railway Pricing:
- **Free Tier**: $5 credit per month
- **Developer Plan**: $5/month (better for production)

---

### Option 3: Heroku

#### Steps:

1. **Install Heroku CLI**
   ```bash
   # Windows (using Chocolatey)
   choco install heroku-cli
   
   # macOS
   brew tap heroku/brew && brew install heroku
   
   # Linux
   curl https://cli-assets.heroku.com/install.sh | sh
   ```

2. **Login to Heroku**
   ```bash
   heroku login
   ```

3. **Create Heroku App**
   ```bash
   heroku create issuehub-api
   ```

4. **Add Java Buildpack**
   ```bash
   heroku buildpacks:set heroku/java
   ```

5. **Set Environment Variables**
   ```bash
   heroku config:set MONGODB_URI="your-mongodb-uri"
   heroku config:set JWT_SECRET="your-jwt-secret"
   heroku config:set CLOUDINARY_CLOUD_NAME="your-cloud-name"
   heroku config:set CLOUDINARY_API_KEY="your-api-key"
   heroku config:set CLOUDINARY_API_SECRET="your-api-secret"
   heroku config:set CORS_ALLOWED_ORIGINS="https://your-frontend.com"
   ```

6. **Create Procfile**
   Create `Procfile` in project root:
   ```
   web: java -jar target/issuehub-1.0.0.jar
   ```

7. **Deploy**
   ```bash
   git push heroku main
   ```

#### Heroku Pricing:
- **Free Tier**: Discontinued
- **Eco Plan**: $5/month
- **Basic Plan**: $7/month

---

### Option 4: AWS EC2

#### Steps:

1. **Launch EC2 Instance**
   - Go to AWS EC2 Console
   - Launch **t2.micro** instance (free tier eligible)
   - Choose **Amazon Linux 2** or **Ubuntu**
   - Configure Security Group:
     - Allow **HTTP** (port 80)
     - Allow **HTTPS** (port 443)
     - Allow **Custom TCP** (port 8080)
     - Allow **SSH** (port 22)

2. **Connect to Instance**
   ```bash
   ssh -i your-key.pem ec2-user@your-instance-ip
   ```

3. **Install Java & Maven**
   ```bash
   sudo yum update -y
   sudo yum install java-17-amazon-corretto -y
   sudo yum install maven -y
   ```

4. **Upload Application**
   ```bash
   # From local machine
   scp -i your-key.pem target/issuehub-1.0.0.jar ec2-user@your-instance-ip:/home/ec2-user/
   ```

5. **Create Environment Variables**
   ```bash
   sudo nano /etc/environment
   # Add all environment variables
   ```

6. **Run Application**
   ```bash
   nohup java -jar issuehub-1.0.0.jar &
   ```

7. **Setup Nginx (Optional)**
   ```bash
   sudo yum install nginx -y
   sudo systemctl start nginx
   # Configure reverse proxy to port 8080
   ```

---

### Option 5: Docker Deployment

#### Create Dockerfile:

```dockerfile
# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/issuehub-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Build and Run:

```bash
# Build Docker image
docker build -t issuehub-api .

# Run container
docker run -d -p 8080:8080 \
  -e MONGODB_URI="your-mongodb-uri" \
  -e JWT_SECRET="your-jwt-secret" \
  -e CLOUDINARY_CLOUD_NAME="your-cloud-name" \
  -e CLOUDINARY_API_KEY="your-api-key" \
  -e CLOUDINARY_API_SECRET="your-api-secret" \
  --name issuehub-api \
  issuehub-api
```

#### Deploy to Docker Hub:

```bash
docker tag issuehub-api yourusername/issuehub-api:latest
docker push yourusername/issuehub-api:latest
```

---

## Environment Variables

### Required Variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | `mongodb+srv://user:pass@cluster.mongodb.net/issuehub` |
| `MONGODB_DATABASE` | Database name | `issuehub` |
| `JWT_SECRET` | JWT signing secret | `your-256-bit-secret-key` |
| `JWT_EXPIRATION` | JWT expiration time (ms) | `86400000` (24 hours) |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name | `your-cloud-name` |
| `CLOUDINARY_API_KEY` | Cloudinary API key | `123456789012345` |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret | `abcdefg12345` |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend URLs | `https://frontend.com` |

### Optional Variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Server port | `8080` |

---

## Post-Deployment

### 1. Test Deployed API

```bash
# Replace with your deployment URL
curl https://your-deployment-url.com/api/health
curl https://your-deployment-url.com/api/health/mongodb
```

### 2. Import Postman Collection

1. Open Postman
2. Click **Import**
3. Select `IssueHub_Postman_Collection.json`
4. Update `baseUrl` variable to your deployment URL
5. Test all endpoints

### 3. Create Initial Admin User

```bash
curl -X POST https://your-deployment-url.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@yourdomain.com",
    "password": "SecurePassword123!",
    "phone": "1234567890",
    "role": "ADMIN",
    "department": "Administration",
    "employeeId": "ADMIN001"
  }'
```

### 4. Configure Custom Domain (Optional)

#### For Render:
1. Go to **Settings** → **Custom Domain**
2. Add your domain (e.g., `api.yourdomain.com`)
3. Update DNS records:
   ```
   Type: CNAME
   Name: api
   Value: your-app.onrender.com
   ```

#### For Railway:
1. Go to **Settings** → **Domains**
2. Add custom domain
3. Update DNS records as instructed

---

## Monitoring & Maintenance

### 1. Application Monitoring

#### Health Checks:
- Set up automated health checks hitting `/api/health`
- Monitor response time and availability

#### Logs:
```bash
# Render: View logs in dashboard
# Railway: View logs in dashboard
# Heroku: heroku logs --tail
# AWS EC2: tail -f /var/log/application.log
```

### 2. MongoDB Monitoring

1. Go to **MongoDB Atlas Dashboard**
2. View **Metrics**:
   - Connections
   - Operations per second
   - Storage usage
3. Set up **Alerts** for:
   - High connection count
   - Storage reaching limit

### 3. Cloudinary Monitoring

1. Go to **Cloudinary Dashboard**
2. Monitor:
   - Storage usage
   - Bandwidth usage
   - Transformations

### 4. Backup Strategy

#### MongoDB Backups:
- MongoDB Atlas provides **automatic daily backups** (free tier: 2 days retention)
- For longer retention, upgrade to paid tier

#### Code Backups:
- Push code to GitHub regularly
- Tag releases: `git tag v1.0.0`

---

## Troubleshooting

### Issue 1: Application Won't Start

**Symptoms:** Error during startup

**Solutions:**
1. Check environment variables are set correctly
2. Verify MongoDB URI is correct and accessible
3. Check logs for specific error messages
4. Ensure Java 17+ is being used

### Issue 2: MongoDB Connection Fails

**Symptoms:** `Failed to connect to MongoDB`

**Solutions:**
1. Verify MongoDB Atlas IP whitelist includes your server IP
2. Check MongoDB URI format
3. Verify database user credentials
4. Ensure cluster is running (not paused)

### Issue 3: Cloudinary Upload Fails

**Symptoms:** `Failed to upload file to Cloudinary`

**Solutions:**
1. Verify Cloudinary credentials
2. Check file size (< 10MB)
3. Verify file type (JPEG, PNG, WEBP, GIF)
4. Check Cloudinary dashboard for quota limits

### Issue 4: CORS Errors

**Symptoms:** `CORS policy: No 'Access-Control-Allow-Origin' header`

**Solutions:**
1. Add frontend URL to `CORS_ALLOWED_ORIGINS`
2. Ensure URL includes protocol (`https://`)
3. Restart application after updating

### Issue 5: JWT Authentication Fails

**Symptoms:** `Invalid token` or `Unauthorized`

**Solutions:**
1. Verify `JWT_SECRET` is set and consistent
2. Check token hasn't expired
3. Ensure `Authorization: Bearer TOKEN` format
4. Generate new token by logging in again

---

## Performance Optimization

### 1. Database Optimization

```javascript
// Create indexes for frequently queried fields
db.issues.createIndex({ "status": 1 })
db.issues.createIndex({ "priority": 1 })
db.issues.createIndex({ "assignedTo": 1 })
db.issues.createIndex({ "createdAt": -1 })
```

### 2. Application Optimization

- Enable **Response Compression**
- Implement **Caching** for dashboard data
- Use **Connection Pooling** for MongoDB

### 3. CDN for Cloudinary

Cloudinary automatically serves images via CDN. No configuration needed!

---

## Security Checklist

- [ ] Use strong JWT secret (256-bit minimum)
- [ ] Enable HTTPS in production
- [ ] Restrict CORS to specific frontend URLs
- [ ] Use environment variables for secrets
- [ ] Enable MongoDB Atlas IP whitelist (production)
- [ ] Regularly update dependencies
- [ ] Use strong database passwords
- [ ] Implement rate limiting (optional)
- [ ] Enable security headers
- [ ] Regular security audits

---

## Support & Resources

### Documentation:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MongoDB Atlas Documentation](https://docs.atlas.mongodb.com/)
- [Cloudinary Documentation](https://cloudinary.com/documentation)

### Community:
- Stack Overflow: [spring-boot tag](https://stackoverflow.com/questions/tagged/spring-boot)
- MongoDB Community: [https://www.mongodb.com/community](https://www.mongodb.com/community)

---

**Congratulations!** 🎉 Your IssueHub backend is now deployed and ready for production use!

For frontend deployment, proceed to **Phase 11-12: React Frontend Development**.
