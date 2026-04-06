# Variables
APP_NAME=petclinic
JAR_FILE=target/spring-petclinic-4.0.0-SNAPSHOT.jar

# Default goal
.PHONY: all
all: build run

# Compile the project
.PHONY: build
build:
	./mvnw clean package -DskipTests

# Build docker images
.PHONY: docker-build
docker-build:
	docker-compose build

# Start containers
.PHONY: run
run:
	docker-compose up

# Start containers in background
.PHONY: up
up:
	docker-compose up -d

# Stop containers
.PHONY: down
down:
	docker-compose down

# Restart everything
.PHONY: restart
restart: down up

# Clean project
.PHONY: clean
clean:
	./mvnw clean

# Full reset (clean + rebuild + restart containers)
.PHONY: reset
reset: clean build docker-build up

# Show logs
.PHONY: logs
logs:
	docker-compose logs -f

# Run Delivery 5 reproducible benchmark (requires k6)
.PHONY: benchmark-finops
benchmark-finops:
	./scripts/run_finops_benchmark.sh

# Stop and remove everything including volumes
.PHONY: destroy
destroy:
	docker-compose down -v
