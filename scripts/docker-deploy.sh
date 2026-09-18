#!/bin/bash
# Docker deployment script for IssueHub

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Functions
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_dependencies() {
    print_info "Checking dependencies..."
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    print_info "✓ Docker and Docker Compose are installed"
}

check_env_file() {
    if [ ! -f .env ]; then
        print_warning ".env file not found"
        if [ -f .env.docker ]; then
            print_info "Copying .env.docker to .env"
            cp .env.docker .env
            print_warning "Please update .env with your actual configuration values"
            exit 1
        else
            print_error "No environment file found. Please create .env file"
            exit 1
        fi
    fi
    
    print_info "✓ Environment file exists"
}

build_images() {
    print_info "Building Docker images..."
    docker-compose build --no-cache
    print_info "✓ Images built successfully"
}

start_services() {
    print_info "Starting services..."
    docker-compose up -d
    print_info "✓ Services started"
}

check_health() {
    print_info "Waiting for services to be healthy..."
    
    # Wait for backend
    local max_attempts=30
    local attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -f http://localhost:8080/actuator/health &> /dev/null; then
            print_info "✓ Backend is healthy"
            break
        fi
        
        attempt=$((attempt + 1))
        if [ $attempt -eq $max_attempts ]; then
            print_error "Backend failed to become healthy"
            docker-compose logs backend
            exit 1
        fi
        
        echo -n "."
        sleep 2
    done
    
    # Wait for frontend
    if curl -f http://localhost:3000/health &> /dev/null; then
        print_info "✓ Frontend is healthy"
    else
        print_warning "Frontend health check failed (this might be normal)"
    fi
}

show_info() {
    echo ""
    echo "======================================"
    echo "  IssueHub Deployment Successful! 🚀"
    echo "======================================"
    echo ""
    echo "Access your application:"
    echo "  Frontend:  http://localhost:3000"
    echo "  Backend:   http://localhost:8080"
    echo "  API Docs:  http://localhost:8080/api"
    echo ""
    echo "Useful commands:"
    echo "  View logs:        docker-compose logs -f"
    echo "  Stop services:    docker-compose down"
    echo "  Restart services: docker-compose restart"
    echo ""
}

# Main execution
main() {
    print_info "Starting IssueHub deployment..."
    echo ""
    
    check_dependencies
    check_env_file
    build_images
    start_services
    check_health
    show_info
}

# Handle script arguments
case "${1:-deploy}" in
    deploy)
        main
        ;;
    stop)
        print_info "Stopping services..."
        docker-compose down
        print_info "✓ Services stopped"
        ;;
    restart)
        print_info "Restarting services..."
        docker-compose restart
        print_info "✓ Services restarted"
        ;;
    logs)
        docker-compose logs -f
        ;;
    clean)
        print_warning "This will remove all containers and volumes. Are you sure? (y/N)"
        read -r response
        if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
            docker-compose down -v
            print_info "✓ Cleaned up all containers and volumes"
        else
            print_info "Cancelled"
        fi
        ;;
    *)
        echo "Usage: $0 {deploy|stop|restart|logs|clean}"
        echo ""
        echo "Commands:"
        echo "  deploy  - Build and start all services (default)"
        echo "  stop    - Stop all services"
        echo "  restart - Restart all services"
        echo "  logs    - View logs from all services"
        echo "  clean   - Remove all containers and volumes"
        exit 1
        ;;
esac
