# Docker Quick Start Guide

Get IssueHub running with Docker in 5 minutes!

## Prerequisites

- Docker Desktop installed ([Download here](https://www.docker.com/products/docker-desktop))
- 4GB+ RAM available
- 10GB+ disk space

## Quick Start (3 Commands)

### 1. Setup Environment
```bash
# Copy environment template
cp .env.docker .env

# Edit .env with your values (use any text editor)
notepad .env
```

**Required Changes in .env:**
- Set a strong `MONGO_ROOT_PASSWORD`
- Add your Cloudinary credentials (get free account at https://cloudinary.com)
- Generate JWT secret: `openssl rand -base64 64` (or use any random string)

### 2. Deploy
```bash
# Build and start everything
docker-compose up -d
```

### 3. Verify
```bash
# Check if services are running
docker-compose ps

# View logs
docker-compose logs -f
```

## Access Your Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/actuator/health

## Common Commands

```bash
# Stop application
docker-compose down

# Restart application
docker-compose restart

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Rebuild after code changes
docker-compose up -d --build

# Complete cleanup (removes data!)
docker-compose down -v
```

## Using the Scripts

### Windows (PowerShell)
```powershell
# Deploy
.\scripts\docker-deploy.ps1 deploy

# Stop
.\scripts\docker-deploy.ps1 stop

# View logs
.\scripts\docker-deploy.ps1 logs
```

### Linux/Mac
```bash
# Make script executable
chmod +x scripts/docker-deploy.sh

# Deploy
./scripts/docker-deploy.sh deploy

# Stop
./scripts/docker-deploy.sh stop

# View logs
./scripts/docker-deploy.sh logs
```

## Troubleshooting

### Port Already in Use
```bash
# Windows: Find process using port 8080
netstat -ano | findstr :8080
taskkill /PID <process_id> /F

# Linux/Mac: Find and kill process
lsof -ti:8080 | xargs kill -9
```

### Services Won't Start
```bash
# Check logs
docker-compose logs

# Restart with clean build
docker-compose down
docker-compose up -d --build
```

### MongoDB Connection Failed
```bash
# Check MongoDB status
docker-compose ps mongodb

# Restart MongoDB
docker-compose restart mongodb

# Wait 30 seconds, then check again
docker-compose ps
```

## Development Mode

To run with MongoDB Express (database admin UI):

```bash
# Start MongoDB + Mongo Express
docker-compose -f docker-compose.dev.yml up -d

# Access Mongo Express
# URL: http://localhost:8081
# Username: admin
# Password: admin

# Run backend and frontend locally (not in Docker)
# Backend: mvn spring-boot:run
# Frontend: npm run dev
```

## Production Deployment

For production with MongoDB Atlas:

```bash
# Use production compose file
docker-compose -f docker-compose.prod.yml up -d

# Update .env with:
# - MongoDB Atlas URI
# - Production domain for CORS
# - Strong passwords and secrets
```

## Next Steps

1. **Create First User**: Use the registration endpoint or MongoDB
2. **Test API**: Import Postman collection from `IssueHub_Postman_Collection.json`
3. **Configure HTTPS**: Set up reverse proxy (Nginx/Traefik)
4. **Set Up Backups**: Use `docker exec issuehub-mongodb mongodump`

## Need More Help?

- Full guide: [DOCKER_DEPLOYMENT_GUIDE.md](./DOCKER_DEPLOYMENT_GUIDE.md)
- API testing: [API_TESTING.md](./API_TESTING.md)
- Deployment: [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)

## Support

Issues? Check logs first:
```bash
docker-compose logs -f
```

Still stuck? Create an issue on GitHub with:
- Error message
- Output of `docker-compose ps`
- Relevant logs
