---
title: "Part 2: Backend Deployment on AWS Lightsail"
date: 2026-05-30
slug: "aws-lightsail-deployment-part-2-backend"
tags: [ "AWS", "Lightsail", "Docker", "MySQL", "Nginx", "Deployment" ]
summary: "Step-by-step backend deployment guide: provision Lightsail, run Docker containers, set up MySQL, configure Nginx, enable HTTPS, and deploy production-ready APIs."
categories: AWS
readTime: 21
---

# Part 2 — Backend Deployment on AWS Lightsail

## A Practical, Beginner-Friendly Guide to Deploying a Dockerized Backend with MySQL, Static IP, Nginx, HTTPS, and Production Environment Configuration

> This is Part 2 of a deployment blog series about taking a full-stack application from local development to a working cloud deployment.  
> Part 1 covered architecture and deployment strategy.  
> This part focuses on the backend: provisioning an AWS Lightsail server, preparing Ubuntu, running Docker, deploying MySQL, deploying a backend container, exposing the API, and preparing the backend for a frontend hosted elsewhere.

---

## Table of Contents

1. [What We Are Deploying in This Part](#1-what-we-are-deploying-in-this-part)
2. [High-Level Backend Deployment Flow](#2-high-level-backend-deployment-flow)
3. [Why AWS Lightsail for This Stage?](#3-why-aws-lightsail-for-this-stage)
4. [Choosing the Right Lightsail Plan](#4-choosing-the-right-lightsail-plan)
5. [Creating the Lightsail Instance](#5-creating-the-lightsail-instance)
6. [Understanding Static IPs](#6-understanding-static-ips)
7. [Configuring Lightsail Firewall Rules](#7-configuring-lightsail-firewall-rules)
8. [Connecting to the Server Using SSH](#8-connecting-to-the-server-using-ssh)
9. [Updating Ubuntu and Installing Required Tools](#9-updating-ubuntu-and-installing-required-tools)
10. [Installing Docker and Docker Compose](#10-installing-docker-and-docker-compose)
11. [Adding Swap Memory for Stability](#11-adding-swap-memory-for-stability)
12. [Cloning or Copying the Backend Project](#12-cloning-or-copying-the-backend-project)
13. [Preparing Production Environment Variables](#13-preparing-production-environment-variables)
14. [Creating the Docker Compose File](#14-creating-the-docker-compose-file)
15. [Running MySQL in Docker with Persistent Storage](#15-running-mysql-in-docker-with-persistent-storage)
16. [Loading the Database Schema](#16-loading-the-database-schema)
17. [Building the Backend: Local Build vs Server Build](#17-building-the-backend-local-build-vs-server-build)
18. [Deploying the Backend Container](#18-deploying-the-backend-container)
19. [Testing the Backend Locally on the Server](#19-testing-the-backend-locally-on-the-server)
20. [Testing the Backend Publicly Using the Server IP](#20-testing-the-backend-publicly-using-the-server-ip)
21. [Common Problems and How to Fix Them](#21-common-problems-and-how-to-fix-them)
22. [Security Notes for Backend Deployment](#22-security-notes-for-backend-deployment)
23. [Operational Commands You Should Know](#23-operational-commands-you-should-know)
24. [Recommended Final Backend State](#24-recommended-final-backend-state)
25. [What Comes Next in Part 3](#25-what-comes-next-in-part-3)

---

## 1. What We Are Deploying in This Part

In this article, we are deploying a generic backend application to AWS Lightsail.

The backend could be built using:

- Spring Boot
- Node.js / Express
- Django
- FastAPI
- Laravel
- Go
- .NET
- Any other API framework

The exact framework is not the main focus. The deployment principles are the same.

The backend needs:

- A Linux server
- A runtime environment
- A database
- Environment variables
- A stable public IP address
- Docker for packaging
- A reverse proxy later for HTTPS
- A way to connect to a frontend hosted somewhere else

For this guide, the example setup is:

```text
Backend Application → Docker container
Database            → MySQL Docker container
Server              → AWS Lightsail Ubuntu instance
Public access       → Static IP
Later HTTPS         → Nginx + Let's Encrypt
```

The key idea is simple:

```text
Instead of installing everything manually on the server,
we package the application and database using Docker.
```

This makes the deployment easier to repeat, debug, and move later.

---

## 2. High-Level Backend Deployment Flow

Before jumping into commands, it helps to understand the complete backend flow.

```text
Developer Laptop
    ↓
Prepare backend project
    ↓
Create Dockerfile
    ↓
Create docker-compose.yml
    ↓
Push code or Docker image
    ↓
AWS Lightsail Ubuntu Server
    ↓
Install Docker
    ↓
Run MySQL container
    ↓
Load database schema
    ↓
Run backend container
    ↓
Test API locally on server
    ↓
Test API using public IP
    ↓
Attach domain and HTTPS later
```

At the end of this part, the backend should respond from a URL like:

```text
http://YOUR_STATIC_IP:10000/api/...
```

In Part 3, we improve this to:

```text
https://api.yourdomain.com/api/...
```

That final HTTPS version is what your frontend should use in production.

---

## 3. Why AWS Lightsail for This Stage?

AWS has many hosting options:

- EC2
- ECS
- EKS
- Lambda
- Elastic Beanstalk
- Lightsail
- App Runner

For a beginner-friendly and low-cost deployment, AWS Lightsail is a strong option.

Lightsail provides:

- Simple virtual server creation
- Predictable monthly pricing
- Static IP support
- Built-in firewall UI
- SSH access
- Easy snapshots
- Simple upgrades through snapshots

Compared to EC2, Lightsail hides many advanced AWS networking details. That is useful when the goal is to get a real backend online without spending days configuring VPCs, subnets, route tables, and security groups.

For a demo, portfolio, small MVP, or learning project, Lightsail is usually easier than EC2.

However, for heavy production workloads, ECS Fargate or EC2 Auto Scaling with RDS would be more scalable.

A practical way to think about it:

| Stage | Recommended Backend Hosting |
|---|---|
| Learning / demo | Lightsail |
| Portfolio MVP | Lightsail |
| Small real users | Lightsail or ECS + RDS |
| Serious production | ECS/Fargate + ALB + RDS |
| Large-scale platform | ECS/EKS + ALB + RDS/Aurora + observability |

For this guide, the goal is the best low-cost practical deployment, so Lightsail is a good fit.

---

## 4. Choosing the Right Lightsail Plan

The biggest mistake many beginners make is choosing the cheapest possible server without understanding memory requirements.

A backend API plus a database needs RAM.

For example:

```text
Backend runtime       → memory
Database              → memory
Docker engine         → memory
Nginx                 → small memory
Operating system      → memory
Build tools           → a lot of memory
```

If you run the database and backend on the same server, do not underestimate RAM.

### 512 MB Plan

A 512 MB plan can work for very small static services or tiny APIs, but it becomes painful when running:

- Docker
- MySQL
- A Java/Spring Boot backend
- Gradle build
- Nginx

The server may become slow or use swap heavily.

Symptoms:

```text
Build takes extremely long
Containers restart
API response resets
SSH becomes slow
MySQL crashes during startup
```

### 2 GB Plan

A 2 GB Lightsail instance is much more comfortable for:

- Docker
- MySQL
- Backend container
- Nginx reverse proxy
- SSL termination
- Small to medium traffic

For a full-stack portfolio or MVP deployment, this is a sensible starting point.

### Practical Recommendation

| Plan | Use Case |
|---|---|
| 512 MB | Very small experiments only |
| 1 GB | Lightweight Node/Python backend without DB-heavy work |
| 2 GB | Recommended minimum for backend + MySQL + Docker |
| 4 GB+ | Better for heavier traffic or build workloads |

If your backend is Java/Spring Boot or another heavier runtime, start with 2 GB.

---

## 5. Creating the Lightsail Instance

Go to AWS Lightsail and create a new instance.

Recommended choices:

```text
Platform: Linux/Unix
Blueprint: OS Only
OS: Ubuntu LTS
Plan: 2 GB RAM or higher
Instance name: meaningful backend name
```

Example instance names:

```text
backend-prod
booking-api-server
marketplace-backend
api-server-01
```

Avoid names like:

```text
Test123
Ubuntu-1
MyServerFinalFinal
```

A professional naming convention helps later when you manage multiple environments.

Good examples:

```text
myapp-backend-prod
myapp-backend-staging
myapp-api-lightsail
```

During instance creation, you may also create or download an SSH key. Keep the key safe.

If using a `.pem` key on your Mac or Linux machine, you will later run:

```bash
chmod 400 path/to/key.pem
ssh -i path/to/key.pem ubuntu@YOUR_SERVER_IP
```

---

## 6. Understanding Static IPs

A cloud instance normally gets a public IP address. But that IP may change if the instance is stopped or recreated.

For a backend API, changing IPs is a problem because:

- DNS records point to an IP
- frontend environment variables may point to an IP
- SSL/domain setup depends on stable routing
- debugging becomes confusing

That is why you should attach a static IP.

In Lightsail:

```text
Instance → Networking → Attach static IP
```

Once attached, your server has a stable public IP.

Example:

```text
YOUR_STATIC_IP = 12.34.56.78
```

Later, a DNS record can point to it:

```text
api.yourdomain.com → 12.34.56.78
```

This is one of the most important steps in a clean deployment.

---

## 7. Configuring Lightsail Firewall Rules

Lightsail has its own firewall rules separate from Ubuntu's internal firewall.

Common ports:

| Port | Purpose |
|---|---|
| 22 | SSH |
| 80 | HTTP |
| 443 | HTTPS |
| 10000 | Example backend port |
| 3306 | MySQL, usually should NOT be public |

For a secure backend deployment, expose only what is required.

### Recommended Lightsail Firewall

```text
22    SSH       Your IP or Any IPv4 during setup
80    HTTP      Any IPv4
443   HTTPS     Any IPv4
10000 Backend   Optional, temporary during testing
```

For production, the backend app port should ideally not be exposed publicly. Instead:

```text
Internet → Nginx :443 → Backend :10000 locally
```

That means after Nginx + HTTPS are working, you can close public port `10000` if Nginx is the only entry point.

### Do Not Publicly Expose MySQL

Avoid this:

```text
3306 open to the internet
```

The database should stay inside Docker network or private server network.

---

## 8. Connecting to the Server Using SSH

You can connect using the Lightsail browser terminal, but for long sessions, your local terminal is more reliable.

From Mac/Linux:

```bash
chmod 400 ~/path/to/key.pem
ssh -i ~/path/to/key.pem ubuntu@YOUR_STATIC_IP
```

If the SSH key is managed by Lightsail browser console, you can also use:

```text
Lightsail → Instance → Connect using SSH
```

Once connected, confirm the server is Ubuntu:

```bash
lsb_release -a
```

Check current user:

```bash
whoami
```

Check current directory:

```bash
pwd
```

Check server resources:

```bash
free -h
df -h
lscpu
```

These commands are useful before installing anything.

---

## 9. Updating Ubuntu and Installing Required Tools

Always update packages first.

```bash
sudo apt update
sudo apt upgrade -y
```

Install common tools:

```bash
sudo apt install -y git curl wget nano unzip ca-certificates gnupg lsb-release
```

Useful verification commands:

```bash
git --version
curl --version
wget --version
```

If you prefer `vim`:

```bash
sudo apt install -y vim
```

For beginners, `nano` is easier.

---

## 10. Installing Docker and Docker Compose

Docker is the foundation of this deployment.

Install Docker:

```bash
sudo apt install -y docker.io docker-compose-v2
```

Enable Docker on startup:

```bash
sudo systemctl enable docker
sudo systemctl start docker
```

Check Docker status:

```bash
sudo systemctl status docker
```

Press `q` to exit the status screen.

Check versions:

```bash
docker --version
docker compose version
```

By default, Docker may require `sudo`. To run Docker commands as the `ubuntu` user:

```bash
sudo usermod -aG docker ubuntu
newgrp docker
```

Then test:

```bash
docker ps
```

If it works without `sudo`, Docker permissions are correct.

---

## 11. Adding Swap Memory for Stability

Small cloud instances may run out of RAM during builds or container startup.

Swap is disk-based backup memory. It is slower than RAM but can prevent crashes.

Create 2 GB swap:

```bash
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
```

Make it permanent:

```bash
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

Check:

```bash
free -h
```

Expected:

```text
Swap: 2.0Gi
```

Swap is useful, but it is not a replacement for enough RAM. If swap is heavily used all the time, upgrade the instance.

---

## 12. Cloning or Copying the Backend Project

There are two common ways to get the backend onto the server.

### Option A: Clone from GitHub

If the repository is public:

```bash
cd /home/ubuntu
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git app-backend
cd app-backend
```

If the repository is private, use a GitHub Personal Access Token or deploy via Docker image instead.

Private repo example:

```bash
git clone https://YOUR_TOKEN@github.com/YOUR_USERNAME/YOUR_REPO.git app-backend
```

Avoid storing long-lived tokens permanently on servers if possible.

### Option B: Pull Docker Image

A more production-like method is:

```text
Build image locally or in CI
Push image to registry
Pull image on server
```

Example image:

```text
yourdockeruser/your-backend:latest
```

Then the server does not need to build the app.

For small servers, this is often the best approach.

---

## 13. Preparing Production Environment Variables

Never hardcode secrets into the application code.

Use an environment file.

Create:

```bash
nano .env.prod
```

Example generic file:

```env
PORT=10000
BACKEND_BIND_ADDRESS=127.0.0.1

MYSQL_DATABASE=app_database
MYSQL_ROOT_PASSWORD=replace_with_strong_root_password
MYSQL_USER=app_user
MYSQL_PASSWORD=replace_with_strong_app_password

SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/app_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=app_user
SPRING_DATASOURCE_PASSWORD=replace_with_strong_app_password

JWT_SECRET=replace_with_at_least_64_random_characters
APP_CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,http://localhost:5173

SECURITY_AUTH_COOKIE_SECURE=true
SECURITY_AUTH_COOKIE_SAME_SITE=None
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

Important notes:

- `MYSQL_PASSWORD` and `SPRING_DATASOURCE_PASSWORD` must match.
- Use strong generated passwords.
- Do not commit `.env.prod` to Git.
- Use HTTPS frontend domains in CORS.
- If using secure cookies, the backend must be served over HTTPS.

A simple way to generate a strong secret:

```bash
openssl rand -base64 64
```

---

## 14. Creating the Docker Compose File

Docker Compose allows multiple containers to run together.

Typical backend deployment needs:

- MySQL container
- Backend app container
- Shared Docker network
- Persistent volume for database data

Create:

```bash
nano docker-compose.prod.yml
```

Example:

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: app_mysql
    restart: unless-stopped
    env_file:
      - .env.prod
    environment:
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h localhost -u$${MYSQL_USER} -p$${MYSQL_PASSWORD} --silent"]
      interval: 10s
      timeout: 5s
      retries: 10
      start_period: 30s
    networks:
      - app_network

  backend:
    image: yourdockeruser/your-backend:latest
    container_name: app_backend
    restart: unless-stopped
    env_file:
      - .env.prod
    environment:
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
      PORT: ${PORT}
      SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL}
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
      JWT_SECRET: ${JWT_SECRET}
      APP_CORS_ALLOWED_ORIGINS: ${APP_CORS_ALLOWED_ORIGINS}
    ports:
      - "127.0.0.1:${PORT}:10000"
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - app_network

volumes:
  mysql_data:

networks:
  app_network:
    driver: bridge
```

Notice this line:

```yaml
ports:
  - "127.0.0.1:${PORT}:10000"
```

This binds the backend only to localhost. That is safer when Nginx is used as the public gateway.

During early testing, you may expose it publicly:

```yaml
ports:
  - "0.0.0.0:${PORT}:10000"
```

But after Nginx is configured, localhost-only binding is better.

---

## 15. Running MySQL in Docker with Persistent Storage

Start MySQL first:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d mysql
```

Check containers:

```bash
docker ps
```

Check logs:

```bash
docker logs --tail=100 app_mysql
```

Check health:

```bash
docker inspect --format='{{json .State.Health}}' app_mysql
```

The MySQL data is stored in a Docker volume:

```text
mysql_data:/var/lib/mysql
```

This means data survives container restarts.

Check volumes:

```bash
docker volume ls
```

Inspect volume:

```bash
docker volume inspect app_mysql_data
```

The exact volume name may be prefixed by your Compose project name.

---

## 16. Loading the Database Schema

There are multiple ways to create database tables.

### Option A: Application Auto-Creates Tables

Some applications use ORM auto-generation:

```env
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

This is convenient for development, but not ideal for production.

### Option B: SQL Schema Script

A cleaner production-like approach is to load a schema script:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml exec -T mysql \
  sh -c 'mysql -h 127.0.0.1 -P 3306 -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE"' \
  < path/to/schema.sql
```

Verify tables:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml exec mysql \
  sh -c 'mysql -h 127.0.0.1 -P 3306 -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -e "SHOW TABLES;"'
```

### Option C: Migration Tool

In mature production systems, use:

- Flyway
- Liquibase
- Prisma migrations
- Django migrations
- Rails migrations

For an MVP, a SQL schema file can be enough if the application is unreleased.

---

## 17. Building the Backend: Local Build vs Server Build

This is one of the most important lessons.

### Building on the Server

You can build on Lightsail:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d --build backend
```

But this may be slow on small servers.

Build tools need memory:

- Gradle
- Maven
- npm
- pip
- Docker build layers
- dependency downloads

If the server has low RAM, the build may:

- take a long time
- fail during dependency downloads
- freeze SSH
- use heavy swap
- fail with timeout errors

### Better Approach: Build Locally or in CI

A better workflow:

```text
Build on laptop or GitHub Actions
Push Docker image to registry
Server pulls final image
```

Example Docker build and push:

```bash
docker login

docker buildx build --platform linux/amd64 \
  -t yourdockeruser/your-backend:latest \
  --push .
```

Then on server:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml pull backend
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d backend
```

This is faster, cleaner, and more production-like.

### Why linux/amd64?

Many developers use Apple Silicon Macs. Docker may build ARM images by default.

Most Lightsail Ubuntu instances are `amd64`.

So build with:

```bash
--platform linux/amd64
```

This prevents architecture mismatch.

---

## 18. Deploying the Backend Container

If using image-based deployment, make sure your Compose file uses:

```yaml
backend:
  image: yourdockeruser/your-backend:latest
```

Not:

```yaml
backend:
  build:
    context: .
    dockerfile: Dockerfile
```

Then run:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml pull backend
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d backend
```

Check running containers:

```bash
docker ps
```

Expected:

```text
app_mysql      Up healthy
app_backend    Up
```

Check backend logs:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml logs --tail=100 backend
```

Follow logs live:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml logs -f backend
```

Exit logs:

```text
CTRL + C
```

---

## 19. Testing the Backend Locally on the Server

Before testing from the internet, test inside the server.

Example health endpoint:

```bash
curl -i http://127.0.0.1:10000/health
```

Or generic API endpoint:

```bash
curl -i http://127.0.0.1:10000/api/v1/public/status
```

If your backend has a context path, adjust accordingly.

Examples:

```text
http://127.0.0.1:10000/api/v1/...
http://127.0.0.1:8080/...
http://127.0.0.1:3000/...
```

If local curl fails, check:

```bash
docker ps -a
docker logs --tail=100 app_backend
```

Also check which ports are listening:

```bash
sudo ss -tulpn
```

---

## 20. Testing the Backend Publicly Using the Server IP

If the backend port is temporarily open publicly:

```bash
curl -i http://YOUR_STATIC_IP:10000/api/v1/public/status
```

If it works locally but not publicly, check:

1. Lightsail firewall
2. Ubuntu firewall
3. Docker port binding
4. Application binding address

### Check Lightsail Firewall

Make sure the port is allowed:

```text
Custom TCP 10000 Any IPv4
```

### Check Ubuntu Firewall

```bash
sudo ufw status
```

If enabled, allow the port:

```bash
sudo ufw allow 10000/tcp
```

### Check Docker Port Binding

```bash
docker ps
```

You should see something like:

```text
0.0.0.0:10000->10000/tcp
```

or, if using Nginx only:

```text
127.0.0.1:10000->10000/tcp
```

---

## 21. Common Problems and How to Fix Them

### Problem 1: Docker Permission Denied

Error:

```text
permission denied while trying to connect to Docker daemon socket
```

Fix:

```bash
sudo usermod -aG docker ubuntu
newgrp docker
```

Then:

```bash
docker ps
```

---

### Problem 2: MySQL Container Keeps Restarting

Check logs:

```bash
docker logs --tail=100 app_mysql
```

Common causes:

- low RAM
- corrupted volume after crash
- wrong env variables
- database already initialized with old credentials

For a fresh unreleased setup, reset the volume:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml down
docker volume ls
docker volume rm YOUR_VOLUME_NAME
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d mysql
```

Warning: deleting a database volume deletes data.

Only do this for fresh/dev/unreleased deployments.

---

### Problem 3: Access Denied for Database User

Error:

```text
Access denied for user
```

Check:

```bash
cat .env.prod
```

Verify:

```env
MYSQL_USER
MYSQL_PASSWORD
MYSQL_DATABASE
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_DATASOURCE_URL
```

The application password and MySQL password must match.

---

### Problem 4: Wrong Database Name

Error:

```text
Access denied to database app_db
```

This happens when the SQL file uses one database name but the environment uses another.

Fix by aligning:

```env
MYSQL_DATABASE=your_database_name
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/your_database_name
```

---

### Problem 5: Build Takes Forever on Lightsail

This usually means the server is too small.

Check:

```bash
free -h
docker stats
```

If swap is heavily used, stop building on the server.

Better:

```text
Build image locally or in CI
Push to Docker Hub/ECR
Pull on Lightsail
```

---

### Problem 6: Backend Starts Then Shuts Down

Check logs:

```bash
docker logs --tail=200 app_backend
```

Common causes:

- missing environment variable
- production startup validation failed
- database schema mismatch
- SSL/cookie settings wrong
- CORS misconfiguration
- invalid JWT secret length

Always read the final error lines, not only the first logs.

---

### Problem 7: `curl: Connection reset by peer`

This usually means:

- application is still starting
- app crashed while request was sent
- container restarted

Check:

```bash
docker ps -a
docker logs --tail=200 app_backend
```

Wait until logs show startup completion.

---

### Problem 8: Public IP Works but Vercel Frontend Shows Network Error

Usually:

```text
HTTPS frontend is trying to call HTTP backend
```

Browser blocks mixed content.

Fix:

```text
Use HTTPS backend domain
```

Example:

```text
https://api.yourdomain.com/api
```

Not:

```text
http://YOUR_IP:10000/api
```

This is handled in Part 3.

---

## 22. Security Notes for Backend Deployment

A backend deployment is not only about making the API respond. It must also be safe enough for public exposure.

### Do Not Commit Secrets

Never commit:

```text
.env.prod
JWT secret
DB password
API keys
SMTP password
Cloud credentials
```

Add to `.gitignore`:

```gitignore
.env
.env.*
application-local.properties
```

### Use Strong Passwords

Bad:

```text
password123
admin123
root
```

Better:

```bash
openssl rand -base64 32
```

### Do Not Expose MySQL Publicly

Keep MySQL inside Docker network.

Do not open port 3306 unless absolutely necessary.

### Use HTTPS for Cookies

If your backend uses secure cookies:

```text
Secure; HttpOnly; SameSite=None
```

Then backend must be served over HTTPS.

### Use Nginx as the Public Gateway

Recommended:

```text
Internet → Nginx HTTPS → Backend localhost port
```

Not ideal:

```text
Internet → Backend raw port
```

---

## 23. Operational Commands You Should Know

These commands are useful during real deployment work.

### Check containers

```bash
docker ps
```

Show stopped containers too:

```bash
docker ps -a
```

### View logs

```bash
docker logs --tail=100 CONTAINER_NAME
```

Compose logs:

```bash
docker compose -f docker-compose.prod.yml logs --tail=100 backend
```

Follow logs:

```bash
docker compose -f docker-compose.prod.yml logs -f backend
```

### Restart a service

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml restart backend
```

### Recreate containers after env changes

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d
```

### Pull latest image

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml pull backend
```

### Check memory usage

```bash
free -h
docker stats --no-stream
```

### Check disk usage

```bash
df -h
docker system df
```

### Clean unused Docker data

```bash
docker system prune
```

Be careful with:

```bash
docker system prune -a
```

It removes unused images too.

### Check listening ports

```bash
sudo ss -tulpn
```

### Test an API

```bash
curl -i http://127.0.0.1:10000/api/v1/public/status
```

With timeout:

```bash
curl -i --max-time 20 http://127.0.0.1:10000/api/v1/public/status
```

### Test CORS preflight

```bash
curl -i -X OPTIONS "https://api.yourdomain.com/api/v1/auth/login" \
  -H "Origin: https://your-frontend-domain.com" \
  -H "Access-Control-Request-Method: POST"
```

Expected:

```text
Access-Control-Allow-Origin: https://your-frontend-domain.com
```

---

## 24. Recommended Final Backend State

At the end of backend deployment, the recommended state is:

```text
AWS Lightsail Ubuntu server
    ↓
Docker installed
    ↓
MySQL container running with persistent volume
    ↓
Backend container running
    ↓
Nginx installed
    ↓
Domain points to static IP
    ↓
HTTPS certificate installed
    ↓
Frontend calls backend through HTTPS domain
```

The final URL should look like:

```text
https://api.yourdomain.com/api
```

The frontend should not call:

```text
http://SERVER_IP:10000
```

That raw IP version is okay only for temporary testing.

---

## 25. What Comes Next in Part 3

In Part 3, we complete the full-stack deployment by connecting the backend to the frontend.

Part 3 covers:

- Buying or connecting a domain
- Understanding root domains and subdomains
- Creating `api.yourdomain.com`
- Configuring Nginx reverse proxy
- Installing Let's Encrypt SSL
- Fixing HTTPS vs HTTP mixed-content errors
- Deploying the frontend to Vercel
- Setting frontend environment variables
- Fixing CORS issues
- Verifying login/API flow from the browser
- Troubleshooting real-world deployment errors

After Part 3, the architecture becomes:

```text
https://yourdomain.com
        ↓
Vercel frontend
        ↓
https://api.yourdomain.com/api
        ↓
Nginx HTTPS reverse proxy
        ↓
Backend Docker container
        ↓
MySQL Docker container
```

That is a clean, low-cost, production-style deployment for a full-stack web application.

---

## Final Thoughts

Deploying a backend is not just about running a command. It requires understanding how several layers work together:

- Linux server
- firewall
- Docker
- database
- environment variables
- public IP
- application ports
- logs
- memory
- networking
- security

The biggest lesson is this:

```text
A deployment becomes easier when every layer is tested separately.
```

Test MySQL first.  
Then test the backend locally.  
Then test the backend through public IP.  
Then add Nginx.  
Then add HTTPS.  
Then connect the frontend.

This step-by-step approach avoids confusion and makes debugging much easier.

In the next part, we will make this backend production-ready for a browser-based frontend using domains, SSL, Vercel, and CORS configuration.
