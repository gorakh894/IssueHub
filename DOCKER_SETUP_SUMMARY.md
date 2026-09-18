# Docker Setup Summary

Complete Docker deployment configuration has been added to IssueHub! 🎉

## 📦 What Was Created

### Core Docker Files
- ✅ **Dockerfile** - Multi-stage build for Spring Boot backend
- ✅ **frontend/Dockerfile** - Multi-stage build for React frontend with Nginx
- ✅ **frontend/nginx.conf** - Production-ready Nginx configuration

### Docker Compose Files
- ✅ **docker-compose.yml** - Full stack (Backend + Frontend + MongoDB)
- ✅ **docker-compose.dev.yml** - Development with MongoDB Express UI
- ✅ **docker-compose.prod.yml** - Production optimized configuration

### Configuration Files
- ✅ **.dockerignore** - Backend Docker ignore rules
- ✅ **frontend/.dockerignore** - Frontend Docker ignore rules
- ✅ **.env.docker** - Environment template for Docker
- ✅ **frontend/.env.docker** - Frontend environment template
- ✅ **mongo-init.js** - MongoDB initialization with indexes

### Deployment Scripts
- ✅ **scripts/docker-deploy.sh** - Bash script for Unix/Linux/Mac
- ✅ **scripts/docker-deploy.ps1** - PowerShell script for Windows

### Documentation
- ✅ **DOCKER_QUICKSTART.md** - Get started in 5 minutes
- ✅ **DOCKER_DEPLOYMENT_GUIDE.md** - Comprehensive deployment guide
- ✅ **DOCKER_SETUP_SUMMARY.md** - This file

### CI/CD
- ✅ **.github/workflows/docker-build.yml** - GitHub Actions workflow

## 🚀 Quick Start Commands

### Windows (PowerShell)
```powershell
# Setup
cp .env.docker .env
notepad .env  # Update with your values

# Deploy
.\scripts\docker-deploy.ps1 deploy

# Or manually
docker-compose up -d
```

### Linux/Mac (Bash)
```bash
# Setup
cp .env.docker .env
nano .env  # Update with your values

# Deploy using script
chmod +x scripts/docker-deploy.sh
./scripts/docker-deploy.sh deploy

# Or manually
docker-compose up -d
```

## 🎯 Deployment Modes

### 1. Full Stack (Production)
```bash
docker-compose up -d
```
**Includes:** Backend + Frontend + MongoDB
**Ports:** Frontend (3000), Backend (8080), MongoDB (27017)

### 2. Development Mode
```bash
docker-compose -f docker-compose.dev.yml up -d
```
**Includes:** MongoDB + Mongo Express (DB Admin UI)
**Ports:** MongoDB (27017), Mongo Express (8081)
**Use Case:** Run backend/frontend locally, use Docker only for database

### 3. Production (with MongoDB Atlas)
```bash
docker-compose -f docker-compose.prod.yml up -d
```
**Includes:** Backend + Frontend (no local MongoDB)
**Requirements:** Update `.env` with MongoDB Atlas URI

## 📝 Configuration Required

Update `.env` file with these values:

```env
# MongoDB (for local deployment)
MONGO_ROOT_USERNAME=admin
MONGO_ROOT_PASSWORD=your_secure_password

# JWT Secret (generate with: openssl rand -base64 64)
JWT_SECRET=your_generated_secret

# Cloudinary (get free account at cloudinary.com)
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# For production deployment
MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/issuehub
CORS_ALLOWED_ORIGINS=https://yourdomain.com
```

## 🔍 Verification Steps

1. **Check Services Status**
```bash
docker-compose ps
```

2. **View Logs**
```bash
docker-compose logs -f
```

3. **Test Backend**
```bash
curl http://localhost:8080/actuator/health
```

4. **Test Frontend**
```bash
curl http://localhost:3000
```

5. **Access Application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Health Check: http://localhost:8080/actuator/health

## 🛠️ Common Operations

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mongodb
```

### Restart Services
```bash
# All services
docker-compose restart

# Specific service
docker-compose restart backend
```

### Stop Services
```bash
# Stop (keeps data)
docker-compose stop

# Stop and remove containers (keeps data)
docker-compose down

# Remove everything including data
docker-compose down -v
```

### Rebuild After Changes
```bash
# Rebuild all
docker-compose build --no-cache

# Rebuild and restart
docker-compose up -d --build

# Rebuild specific service
docker-compose build backend
docker-compose up -d backend
```

### Database Operations
```bash
# Connect to MongoDB
docker exec -it issuehub-mongodb mongosh -u admin -p yourpassword

# Backup database
docker exec issuehub-mongodb mongodump --out=/data/backup
docker cp issuehub-mongodb:/data/backup ./mongodb-backup

# Restore database
docker cp ./mongodb-backup issuehub-mongodb:/data/backup
docker exec issuehub-mongodb mongorestore /data/backup
```

## 🎨 Key Features

### Backend Dockerfile
- Multi-stage build for smaller image size
- Maven dependency caching
- Non-root user for security
- Health checks configured
- JVM memory optimization for containers

### Frontend Dockerfile
- Multi-stage build with Node.js and Nginx
- Production-optimized Nginx configuration
- Gzip compression enabled
- Security headers configured
- SPA routing support
- Static asset caching

### Docker Compose
- Service orchestration
- Health checks for all services
- Automatic restart policies
- Volume management for data persistence
- Network isolation
- Environment variable support
- Logging configuration

### MongoDB Initialization
- Automatic database creation
- Collection creation with validation
- Performance indexes
- Sample data seeding ready

## 📊 Resource Requirements

### Minimum
- **RAM:** 4GB
- **CPU:** 2 cores
- **Disk:** 10GB free space

### Recommended
- **RAM:** 8GB+
- **CPU:** 4 cores
- **Disk:** 20GB+ free space

## 🔐 Security Best Practices

1. **Never commit `.env` files**
   - `.env` is in `.gitignore`
   - Use `.env.docker` as template

2. **Use strong passwords**
   ```bash
   # Generate secure password
   openssl rand -base64 32
   ```

3. **Update MongoDB credentials**
   - Change default admin password
   - Use strong passwords in production

4. **Use HTTPS in production**
   - Set up reverse proxy (Nginx/Traefik)
   - Obtain SSL certificates (Let's Encrypt)

5. **Limit CORS origins**
   - Update `CORS_ALLOWED_ORIGINS` with actual domains
   - Don't use `*` in production

6. **Keep secrets secure**
   - Use environment variables
   - Consider Docker secrets for Swarm
   - Use cloud secret managers in production

## 🌐 Production Deployment

### Cloud Platforms

#### AWS EC2
```bash
# Install Docker on EC2
sudo yum update -y
sudo yum install docker -y
sudo service docker start

# Deploy application
git clone <repo-url>
cd IssueHub
cp .env.docker .env
nano .env
docker-compose -f docker-compose.prod.yml up -d
```

#### DigitalOcean
```bash
# Use Docker Droplet (Docker pre-installed)
git clone <repo-url>
cd IssueHub
cp .env.docker .env
nano .env
docker-compose -f docker-compose.prod.yml up -d
```

#### Azure
```bash
# Push to Azure Container Registry
docker-compose build
docker tag issuehub-backend:latest myregistry.azurecr.io/issuehub-backend
docker push myregistry.azurecr.io/issuehub-backend
```

### With Reverse Proxy (Nginx)

Create `/etc/nginx/sites-available/issuehub`:
```nginx
server {
    listen 80;
    server_name yourdomain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### SSL with Let's Encrypt
```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Obtain certificate
sudo certbot --nginx -d yourdomain.com

# Auto-renewal
sudo certbot renew --dry-run
```

## 📈 Monitoring

### Resource Usage
```bash
# Real-time stats
docker stats

# Disk usage
docker system df
```

### Application Logs
```bash
# Follow logs
docker-compose logs -f

# Save logs to file
docker-compose logs > app.log
```

### Health Monitoring
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend health
curl http://localhost:3000/health

# MongoDB health
docker exec issuehub-mongodb mongosh --eval "db.adminCommand('ping')"
```

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <pid> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Services Not Starting
```bash
# Check logs
docker-compose logs

# Check individual service
docker-compose logs backend

# Restart with clean build
docker-compose down
docker-compose up -d --build
```

### Database Connection Failed
```bash
# Check MongoDB status
docker-compose ps mongodb

# Check MongoDB logs
docker-compose logs mongodb

# Restart MongoDB
docker-compose restart mongodb
```

### Out of Disk Space
```bash
# Clean up unused resources
docker system prune -a

# Remove unused volumes
docker volume prune

# Check disk usage
docker system df
```

## 📚 Additional Resources

- **Quick Start Guide:** [DOCKER_QUICKSTART.md](./DOCKER_QUICKSTART.md)
- **Full Documentation:** [DOCKER_DEPLOYMENT_GUIDE.md](./DOCKER_DEPLOYMENT_GUIDE.md)
- **API Testing:** [API_TESTING.md](./API_TESTING.md)
- **Deployment Guide:** [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)

## ✅ Verification Checklist

Before deploying to production:

- [ ] Updated `.env` with production values
- [ ] Changed default passwords
- [ ] Generated secure JWT secret
- [ ] Configured Cloudinary credentials
- [ ] Updated CORS origins
- [ ] Set up MongoDB Atlas (or production MongoDB)
- [ ] Configured SSL/HTTPS
- [ ] Set up backups
- [ ] Configured monitoring
- [ ] Tested all endpoints
- [ ] Reviewed security settings
- [ ] Set up CI/CD pipeline

## 🎉 Success!

Your IssueHub application is now fully containerized and ready for deployment!

**Next Steps:**
1. Review [DOCKER_QUICKSTART.md](./DOCKER_QUICKSTART.md)
2. Configure your `.env` file
3. Run `docker-compose up -d`
4. Access http://localhost:3000

**Need Help?**
- Check logs: `docker-compose logs -f`
- Read full guide: [DOCKER_DEPLOYMENT_GUIDE.md](./DOCKER_DEPLOYMENT_GUIDE.md)
- Create GitHub issue with logs and error messages

---

**Created:** September 18, 2026
**Status:** ✅ Complete and Ready for Deployment
