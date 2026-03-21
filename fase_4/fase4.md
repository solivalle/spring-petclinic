# 🐳 Spring Boot + Docker Compose Guide

This project includes a **Makefile** and **Docker Compose** setup to simplify building and running the application.

---

## 📦 Prerequisites

Make sure you have installed:

- Docker
- Docker Compose (or Docker with `docker compose`)
- Make
- Java (only if you want to build manually)

---

# 🐳 Quick Start

## First time setup
    make reset

## Just run containers
    make up

## See logs
    make logs

## Stop everything
    make down

## Rebuild after code changes
    make build docker-build up
