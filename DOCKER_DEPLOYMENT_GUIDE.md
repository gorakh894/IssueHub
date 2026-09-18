# Docker Deployment Guide for IssueHub

This guide provides comprehensive instructions for deploying the IssueHub application using Docker and Docker Compose.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Deployment Modes](#deployment-modes)
- [Management Commands](#management-commands)
- [Troubleshooting](#troubleshooting)
- [Production Deployment](#production-deployment)

## Prerequisites

### Required Software
- **Docker**: 20.10 or higher
- **Docker Compose**: 2.0 or higher

### Installation

#### Windows
Download and install [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/)

#### macOS
Download and install [Docker Desktop for Mac](https://docs.docker.com/desktop/install/mac-install/)

#### Linux
```bash
# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### Verify Installation
```bash
docker --version
docker-compose --version
```

## Quick Start

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd IssueHub
```

### 2. Configure Environment Variables
```bash
# Copy the example environment file
cp .env.docker .env

# Edit .env with your actual values
# Important: Update these values!
nano .env
```

**Required Configuration:**
```env
# MongoDB Credentials
MONGO_ROOT_USERNAME=admin
MONGO_ROOT_PASSWORD=your_secure_password_here

# JWT Secret (generate with: openssl rand -base64 64)
JWT_SECRET=your_generated_secret_here

# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your_cloudinary_name
CLOUDINARY_API_KEY=your_cloudinary_key
CLOUDINARY_API_SECRET=your_cloudinary_secret
```

### 3. Build and Start Services
```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f
```

### 4. Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **API Health Check**: http://localhost:8080/actuator/health
- **MongoDB**: localhost:27017

### 5. Create Admin User (First Time Setup)
```bash
# Access backend container
docker exec -it issuehub-backend bash

# Use the API or MongoDB to create initial users
# Or use the registration endpoint: POST http://localhost:8080/api/auth/register
```

## Configuration

### Environment Variables

#### MongoDB Settings
| Variable | Description | Default |
|----------|-------------|---------|
| `MONGO_ROOT_USERNAME` | MongoDB root username | `admin` |
| `MONGO_ROOT_PASSWORD` | MongoDB root password | `changeme123!` |
| `MONGODB_DATABASE` | Database name | `issuehub` |

#### Backend Settings
| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Backend port | `8080` |
| `JWT_SECRET` | JWT signing secret | Required |
| `JWT_EXPIRATION` | Token expiration (ms) | `604800000` (7 days) |
| `SPRING_PROFILES_ACTIVE` | Spring profile | `docker` |

#### Frontend Settings
| Variable | Description | Default |
|----------|-------------|---------|
| `FRONTEND_PORT` | Frontend port | `3000` |
| `VITE_API_BASE_URL` | Backend API URL | `http://localhost:8080/api` |

#### Cloudinary Settings
| Variable | Description |
|----------|-------------|
| `CLOUDINARY_CLOUD_NAME` | Your Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Your Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Your Cloudinary API secret |

### Custom Ports
To change default ports, update your `.env` file:
```env
SERVER_PORT=9090
FRONTEND_PORT=4000
```

Then restart services:
```bash
docker-compose down
docker-compose up -d
```

## Deployment Modes

### Production Deployment (Full Stack)
```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Development Mode (with MongoDB Express)
```bash
# Start only MongoDB with admin UI
docker-compose -f docker-compose.dev.yml up -d

# Access MongoDB Express UI
# URL: http://localhost:8081
# Username: admin
# Password: admin

# Stop services
docker-compose -f docker-compose.dev.yml down
```

### Individual Services

#### Start Only Backend + MongoDB
```bash
docker-compose up -d mongodb backend
```

#### Start Only Frontend
```bash
docker-compose up -d frontend
```

## Management Commands

### View Service Status
```bash
# List running containers
docker-compose ps

# View resource usage
docker stats
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mongodb

# Last 100 lines
docker-compose logs --tail=100 backend
```

### Restart Services
```bash
# Restart all services
docker-compose restart

# Restart specific service
docker-compose restart backend
```

### Stop Services
```bash
# Stop all services (keeps data)
docker-compose stop

# Stop and remove containers (keeps data)
docker-compose down

# Stop and remove everything including volumes (⚠️ DELETES DATA)
docker-compose down -v
```

### Rebuild Services
```bash
# Rebuild all services
docker-compose build

# Rebuild specific service
docker-compose build backend

# Rebuild and restart
docker-compose up -d --build
```

### Access Container Shell
```bash
# Backend container
docker exec -it issuehub-backend sh

# Frontend container
docker exec -it issuehub-frontend sh

# MongoDB container
docker exec -it issuehub-mongodb mongosh
```

### Database Operations

#### Backup MongoDB Data
```bash
# Create backup
docker exec issuehub-mongodb mongodump --out=/data/backup

# Copy backup to host
docker cp issuehub-mongodb:/data/backup ./mongodb-backup
```

#### Restore MongoDB Data
```bash
# Copy backup to container
docker cp ./mongodb-backup issuehub-mongodb:/data/backup

# Restore backup
docker exec issuehub-mongodb mongorestore /data/backup
```

#### Connect to MongoDB
```bash
# Using mongosh
docker exec -it issuehub-mongodb mongosh -u admin -p changeme123!

# Select database
use issuehub

# View collections
show collections

# Query users
db.users.find().pretty()
```

## Troubleshooting

### Common Issues

#### 1. Port Already in Use
**Error:** `Bind for 0.0.0.0:8080 failed: port is already allocated`

**Solution:**
```bash
# Find process using the port (Windows)
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <process_id> /F

# Or change port in .env file
SERVER_PORT=8081
```

#### 2. MongoDB Connection Failed
**Error:** `Connection refused to mongodb:27017`

**Solution:**
```bash
# Check if MongoDB is running
docker-compose ps mongodb

# Check MongoDB logs
docker-compose logs mongodb

# Restart MongoDB
docker-compose restart mongodb

# Wait for health check
docker-compose ps
```

#### 3. Backend Fails to Start
**Error:** `Application failed to start`

**Solution:**
```bash
# Check backend logs
docker-compose logs backend

# Verify environment variables
docker exec issuehub-backend env | grep MONGODB

# Restart with clean build
docker-compose down
docker-compose up -d --build backend
```

#### 4. Frontend Cannot Connect to Backend
**Error:** `Network Error` or `CORS Error`

**Solution:**
```bash
# Check if backend is running
curl http://localhost:8080/actuator/health

# Verify CORS settings in .env
CORS_ALLOWED_ORIGINS=http://localhost:3000

# Update frontend .env
VITE_API_BASE_URL=http://localhost:8080/api

# Rebuild frontend
docker-compose up -d --build frontend
```

#### 5. Out of Disk Space
```bash
# Clean up unused Docker resources
docker system prune -a

# Remove unused volumes
docker volume prune

# Remove specific volume (⚠️ deletes data)
docker volume rm issuehub_mongodb_data
```

### Health Checks

#### Check Service Health
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend health
curl http://localhost:3000/health

# MongoDB health
docker exec issuehub-mongodb mongosh --eval "db.adminCommand('ping')"
```

#### View Container Health Status
```bash
docker-compose ps
```

### Debug Mode

#### Run Backend in Debug Mode
```bash
# Stop current backend
docker-compose stop backend

# Run with debug logs
docker-compose run --rm -p 8080:8080 -e LOGGING_LEVEL_ROOT=DEBUG backend
```

## Production Deployment

### Best Practices

#### 1. Use Strong Passwords
```bash
# Generate secure passwords
openssl rand -base64 32

# Update .env
MONGO_ROOT_PASSWORD=<generated-password>
JWT_SECRET=<generated-secret>
```

#### 2. Use Production MongoDB
Update `.env` with MongoDB Atlas or production instance:
```env
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/issuehub
```

#### 3. Update CORS Origins
```env
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com
```

#### 4. Use HTTPS
Set up reverse proxy (Nginx/Apache) with SSL certificates:
```nginx
server {
    listen 443 ssl;
    server_name yourdomain.com;
    
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    location / {
        proxy_pass http://localhost:3000;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
    }
}
```

#### 5. Enable Monitoring
```bash
# View resource usage
docker stats

# Set up log aggregation
docker-compose logs -f > app.log
```

#### 6. Regular Backups
```bash
# Create backup script
cat > backup.sh << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
docker exec issuehub-mongodb mongodump --out=/data/backup_$DATE
docker cp issuehub-mongodb:/data/backup_$DATE ./backups/
EOF

chmod +x backup.sh

# Schedule with cron (Linux/Mac)
crontab -e
# Add: 0 2 * * * /path/to/backup.sh
```

### Cloud Deployment

#### AWS EC2
```bash
# Install Docker on EC2
sudo yum update -y
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Deploy application
git clone <repo>
cd IssueHub
cp .env.docker .env
nano .env  # Update values
docker-compose up -d
```

#### DigitalOcean Droplet
```bash
# Similar to AWS EC2 setup
# Use Docker Droplet for pre-installed Docker
docker-compose up -d
```

#### Azure Container Instances
```bash
# Push images to Azure Container Registry
docker-compose build
docker tag issuehub-backend:latest myregistry.azurecr.io/issuehub-backend
docker tag issuehub-frontend:latest myregistry.azurecr.io/issuehub-frontend
docker push myregistry.azurecr.io/issuehub-backend
docker push myregistry.azurecr.io/issuehub-frontend
```

## Monitoring & Maintenance

### Regular Maintenance Tasks

#### 1. Update Images
```bash
# Pull latest base images
docker-compose pull

# Rebuild with latest dependencies
docker-compose build --no-cache
docker-compose up -d
```

#### 2. Clean Up Logs
```bash
# Limit log file size in docker-compose.yml
services:
  backend:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

#### 3. Monitor Resources
```bash
# Check disk usage
docker system df

# Check container resources
docker stats --no-stream
```

### Performance Tuning

#### Increase Java Memory (Backend)
```yaml
# docker-compose.yml
services:
  backend:
    environment:
      JAVA_OPTS: -Xmx2g -Xms1g
```

#### Increase MongoDB Cache
```yaml
# docker-compose.yml
services:
  mongodb:
    command: --wiredTigerCacheSizeGB 2
```

## Support

For issues and questions:
- Check logs: `docker-compose logs -f`
- GitHub Issues: [Your Repo Issues]
- Documentation: [Your Docs URL]

## License

[Your License]
