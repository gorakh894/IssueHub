# Docker deployment script for IssueHub (PowerShell)

$ErrorActionPreference = "Stop"

# Functions
function Write-Info {
    param($Message)
    Write-Host "[INFO] $Message" -ForegroundColor Green
}

function Write-Warning {
    param($Message)
    Write-Host "[WARNING] $Message" -ForegroundColor Yellow
}

function Write-Error {
    param($Message)
    Write-Host "[ERROR] $Message" -ForegroundColor Red
}

function Check-Dependencies {
    Write-Info "Checking dependencies..."
    
    try {
        $null = docker --version
        Write-Info "✓ Docker is installed"
    }
    catch {
        Write-Error "Docker is not installed. Please install Docker Desktop first."
        exit 1
    }
    
    try {
        $null = docker-compose --version
        Write-Info "✓ Docker Compose is installed"
    }
    catch {
        Write-Error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    }
}

function Check-EnvFile {
    if (-not (Test-Path .env)) {
        Write-Warning ".env file not found"
        if (Test-Path .env.docker) {
            Write-Info "Copying .env.docker to .env"
            Copy-Item .env.docker .env
            Write-Warning "Please update .env with your actual configuration values"
            exit 1
        }
        else {
            Write-Error "No environment file found. Please create .env file"
            exit 1
        }
    }
    
    Write-Info "✓ Environment file exists"
}

function Build-Images {
    Write-Info "Building Docker images..."
    docker-compose build --no-cache
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to build images"
        exit 1
    }
    Write-Info "✓ Images built successfully"
}

function Start-Services {
    Write-Info "Starting services..."
    docker-compose up -d
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Failed to start services"
        exit 1
    }
    Write-Info "✓ Services started"
}

function Check-Health {
    Write-Info "Waiting for services to be healthy..."
    
    $maxAttempts = 30
    $attempt = 0
    
    while ($attempt -lt $maxAttempts) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 2
            if ($response.StatusCode -eq 200) {
                Write-Info "✓ Backend is healthy"
                break
            }
        }
        catch {
            $attempt++
            if ($attempt -eq $maxAttempts) {
                Write-Error "Backend failed to become healthy"
                docker-compose logs backend
                exit 1
            }
            Write-Host "." -NoNewline
            Start-Sleep -Seconds 2
        }
    }
    
    # Check frontend
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:3000/health" -UseBasicParsing -TimeoutSec 2
        if ($response.StatusCode -eq 200) {
            Write-Info "✓ Frontend is healthy"
        }
    }
    catch {
        Write-Warning "Frontend health check failed (this might be normal)"
    }
}

function Show-Info {
    Write-Host ""
    Write-Host "======================================"
    Write-Host "  IssueHub Deployment Successful! 🚀"
    Write-Host "======================================"
    Write-Host ""
    Write-Host "Access your application:"
    Write-Host "  Frontend:  http://localhost:3000"
    Write-Host "  Backend:   http://localhost:8080"
    Write-Host "  API Docs:  http://localhost:8080/api"
    Write-Host ""
    Write-Host "Useful commands:"
    Write-Host "  View logs:        docker-compose logs -f"
    Write-Host "  Stop services:    docker-compose down"
    Write-Host "  Restart services: docker-compose restart"
    Write-Host ""
}

function Deploy {
    Write-Info "Starting IssueHub deployment..."
    Write-Host ""
    
    Check-Dependencies
    Check-EnvFile
    Build-Images
    Start-Services
    Check-Health
    Show-Info
}

function Stop-Deployment {
    Write-Info "Stopping services..."
    docker-compose down
    Write-Info "✓ Services stopped"
}

function Restart-Deployment {
    Write-Info "Restarting services..."
    docker-compose restart
    Write-Info "✓ Services restarted"
}

function Show-Logs {
    docker-compose logs -f
}

function Clean-Deployment {
    Write-Warning "This will remove all containers and volumes. Are you sure? (y/N)"
    $response = Read-Host
    if ($response -match '^[yY]([eE][sS])?$') {
        docker-compose down -v
        Write-Info "✓ Cleaned up all containers and volumes"
    }
    else {
        Write-Info "Cancelled"
    }
}

# Main execution
$command = if ($args.Count -gt 0) { $args[0] } else { "deploy" }

switch ($command) {
    "deploy" {
        Deploy
    }
    "stop" {
        Stop-Deployment
    }
    "restart" {
        Restart-Deployment
    }
    "logs" {
        Show-Logs
    }
    "clean" {
        Clean-Deployment
    }
    default {
        Write-Host "Usage: .\docker-deploy.ps1 {deploy|stop|restart|logs|clean}"
        Write-Host ""
        Write-Host "Commands:"
        Write-Host "  deploy  - Build and start all services (default)"
        Write-Host "  stop    - Stop all services"
        Write-Host "  restart - Restart all services"
        Write-Host "  logs    - View logs from all services"
        Write-Host "  clean   - Remove all containers and volumes"
        exit 1
    }
}
