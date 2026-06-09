---
title: "Part 1: Architecture and Deployment Strategy for Full-Stack Applications"
date: 2026-05-30
slug: "aws-lightsail-deployment-part-1-architecture"
tags: [ "AWS", "Lightsail", "Vercel", "Deployment", "Docker", "Nginx" ]
summary: "Comprehensive guide to planning cost-effective full-stack deployments: learn architecture decisions, Lightsail vs EC2, Docker strategy, domain setup, and deployment best practices."
categories: AWS
readTime: 22
---

# Part 1 — Architecture and Deployment Strategy

## How to Plan a Cost-Effective Full-Stack Deployment with AWS Lightsail, Docker, MySQL, Nginx, SSL, and Vercel

> This is Part 1 of a three-part deployment series. In this part, we focus on the architecture, decision-making, trade-offs, cost strategy, and production mindset before touching the server. The goal is to help a beginner understand not just *what commands to run*, but *why each piece exists* in a real deployment.

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [What We Are Trying to Deploy](#2-what-we-are-trying-to-deploy)
3. [The Final Target Architecture](#3-the-final-target-architecture)
4. [Why Deployment Planning Matters](#4-why-deployment-planning-matters)
5. [Choosing the Frontend Hosting Platform](#5-choosing-the-frontend-hosting-platform)
6. [Choosing the Backend Hosting Platform](#6-choosing-the-backend-hosting-platform)
7. [Why AWS Lightsail Is a Good MVP Choice](#7-why-aws-lightsail-is-a-good-mvp-choice)
8. [Where Lightsail Is Not Enough](#8-where-lightsail-is-not-enough)
9. [Why Docker Is Useful in This Architecture](#9-why-docker-is-useful-in-this-architecture)
10. [Why Docker Compose Is Useful](#10-why-docker-compose-is-useful)
11. [Database Strategy: MySQL in Docker vs Managed Database](#11-database-strategy-mysql-in-docker-vs-managed-database)
12. [Static IP Strategy](#12-static-ip-strategy)
13. [Domain and Subdomain Strategy](#13-domain-and-subdomain-strategy)
14. [Why HTTPS Is Mandatory](#14-why-https-is-mandatory)
15. [Reverse Proxy Strategy with Nginx](#15-reverse-proxy-strategy-with-nginx)
16. [Environment Variable Strategy](#16-environment-variable-strategy)
17. [CORS Strategy](#17-cors-strategy)
18. [Cookie and Authentication Strategy](#18-cookie-and-authentication-strategy)
19. [Build Strategy: Build on Server vs Build Locally](#19-build-strategy-build-on-server-vs-build-locally)
20. [Cost Strategy](#20-cost-strategy)
21. [Security Strategy](#21-security-strategy)
22. [Operational Strategy](#22-operational-strategy)
23. [Common Beginner Mistakes](#23-common-beginner-mistakes)
24. [Recommended Deployment Roadmap](#24-recommended-deployment-roadmap)
25. [Architecture Checklist Before Starting Part 2](#25-architecture-checklist-before-starting-part-2)
26. [Conclusion](#26-conclusion)

---

## 1. Introduction

Building a full-stack application locally is one milestone. Deploying it properly is another.

Many developers can run a backend on `localhost`, start a frontend with `npm run dev`, and connect both using a local API URL. But when the same application goes to the internet, several new concerns appear:

- Where should the frontend run?
- Where should the backend run?
- Where should the database live?
- How should the frontend call the backend securely?
- How do we avoid exposing secrets?
- How do we make HTTPS work?
- How do we prevent CORS errors?
- How do we make the deployment affordable?
- How do we make it professional enough to show recruiters, clients, or users?

This blog focuses on a practical, low-cost, production-style deployment approach for a modern full-stack application.

The architecture uses:

- **Vercel** for frontend hosting
- **AWS Lightsail** for backend hosting
- **Docker** for packaging the backend
- **Docker Compose** for running backend and database services
- **MySQL** as the database
- **Nginx** as a reverse proxy
- **Let's Encrypt** for free HTTPS
- **A custom domain** for professional frontend and backend URLs

This setup is not the same as a large enterprise Kubernetes deployment, but it is a very strong MVP and portfolio architecture. It teaches practical cloud, Docker, networking, SSL, DNS, and deployment concepts without creating unnecessary cost.

---

## 2. What We Are Trying to Deploy

For this guide, imagine we have a generic full-stack application:

```text
Frontend: React / Vite / Next.js style application
Backend: Spring Boot / Node.js / Django / Express / any REST API
Database: MySQL / PostgreSQL
Authentication: JWT or cookie-based auth
Hosting goal: Publicly accessible frontend and backend
Budget goal: Low monthly cost
```

The exact business logic does not matter. It could be:

- Appointment booking system
- Marketplace platform
- Portfolio dashboard
- Admin panel
- E-commerce MVP
- CRM tool
- Inventory app
- SaaS prototype

The deployment principles remain similar.

Locally, your application may look like this:

```text
Browser
  ↓
Frontend running at http://localhost:5173
  ↓
Backend running at http://localhost:8080
  ↓
Database running locally or in Docker
```

In production, we want this:

```text
User Browser
  ↓
https://yourdomain.com
  ↓
Frontend hosted on Vercel
  ↓
https://api.yourdomain.com
  ↓
Nginx reverse proxy on AWS Lightsail
  ↓
Backend Docker container
  ↓
MySQL Docker container or managed database
```

That is the high-level target.

---

## 3. The Final Target Architecture

The final architecture looks like this:

```text
                         ┌──────────────────────────┐
                         │        User Browser       │
                         └─────────────┬────────────┘
                                       │
                                       │ HTTPS
                                       ▼
                         ┌──────────────────────────┐
                         │   Frontend Domain         │
                         │   https://app.example.com │
                         └─────────────┬────────────┘
                                       │
                                       │ Hosted by Vercel
                                       ▼
                         ┌──────────────────────────┐
                         │   Vercel Frontend         │
                         │   React / Vite / Next     │
                         └─────────────┬────────────┘
                                       │
                                       │ HTTPS API Call
                                       ▼
                         ┌──────────────────────────┐
                         │ Backend API Domain        │
                         │ https://api.example.com   │
                         └─────────────┬────────────┘
                                       │
                                       │ Port 443
                                       ▼
                         ┌──────────────────────────┐
                         │ Nginx Reverse Proxy       │
                         │ AWS Lightsail Ubuntu      │
                         └─────────────┬────────────┘
                                       │
                                       │ Proxy to localhost
                                       ▼
                         ┌──────────────────────────┐
                         │ Backend Docker Container  │
                         │ Internal Port 10000       │
                         └─────────────┬────────────┘
                                       │
                                       │ Docker Network
                                       ▼
                         ┌──────────────────────────┐
                         │ MySQL Docker Container    │
                         │ Persistent Docker Volume  │
                         └──────────────────────────┘
```

This setup gives us:

- A professional frontend URL
- A professional backend API URL
- HTTPS on both frontend and backend
- Dockerized backend deployment
- Persistent database storage
- Low monthly cost
- Simple troubleshooting
- Enough professionalism for portfolio, MVP, demo, and early-stage product use

---

## 4. Why Deployment Planning Matters

Deployment should not start with random commands. It should start with decisions.

A poor deployment usually happens when we do this:

```text
1. Buy a server
2. SSH into it
3. Randomly install packages
4. Copy code manually
5. Run app in terminal
6. Hope it keeps working
```

That may work for a few minutes, but it is not reliable.

A better deployment approach is:

```text
1. Decide frontend hosting
2. Decide backend hosting
3. Decide database strategy
4. Decide domain structure
5. Decide SSL strategy
6. Decide environment variable strategy
7. Decide build strategy
8. Decide restart/recovery strategy
9. Deploy step by step
10. Verify each layer independently
```

The key mindset is:

> Do not debug the whole system at once. Verify one layer at a time.

For example:

```text
Can backend respond locally on the server?
Can backend respond through public IP?
Can backend respond through Nginx over HTTP?
Can backend respond through HTTPS domain?
Can frontend read correct environment variable?
Can browser call backend without CORS error?
Can login cookies work across domains?
```

This layered verification approach saves hours of confusion.

---

## 5. Choosing the Frontend Hosting Platform

For a React, Vite, Next.js, or static frontend application, Vercel is a strong option.

### Why Vercel Works Well for Frontend

Vercel provides:

- Automatic builds from GitHub
- Free HTTPS
- CDN-backed hosting
- Custom domain support
- Environment variables
- Preview deployments
- Easy rollback
- Good developer experience

For frontend apps, Vercel removes a lot of operational complexity.

Instead of manually configuring Nginx for the frontend, you can simply push your code to GitHub and let Vercel build and deploy.

Typical frontend deployment flow:

```text
GitHub Repository
  ↓
Vercel Project
  ↓
Build Command: npm run build
  ↓
Output Directory: dist or build
  ↓
Production URL
```

### Common Frontend Environment Variable

A frontend usually needs to know the backend API base URL.

For a Vite app, this commonly looks like:

```env
VITE_API_BASE_URL=https://api.example.com/api
```

For a Next.js app, it may look like:

```env
NEXT_PUBLIC_API_BASE_URL=https://api.example.com/api
```

The important rule:

> Frontend environment variables are baked into the build. If you change them in Vercel, redeploy the frontend.

---

## 6. Choosing the Backend Hosting Platform

The backend needs a server or container platform.

There are many options:

| Option | Best For | Pros | Cons |
|---|---|---|---|
| AWS Lightsail | Low-cost VPS-style deployment | Simple, predictable cost | Manual server management |
| EC2 | Full AWS control | Flexible | More setup complexity |
| Render | Easy backend hosting | Built-in HTTPS | Cost can grow |
| Railway | Fast MVP deployment | Simple database + app hosting | Pricing may change |
| Fly.io | Global app hosting | Good for containers | Learning curve |
| ECS Fargate | Production scaling | Managed containers | More AWS complexity |
| Kubernetes | Large-scale systems | Powerful | Overkill for MVP |

For a low-cost MVP or portfolio deployment, Lightsail is a good middle ground.

It gives you:

- A Linux server
- Static IP support
- Simple networking
- Predictable pricing
- Enough control for Docker, Nginx, SSL, and database

---

## 7. Why AWS Lightsail Is a Good MVP Choice

AWS Lightsail is simpler than EC2 for beginners and independent developers.

With EC2, you usually need to think about:

- EC2 instance type
- Security groups
- Elastic IP
- VPC
- Subnets
- IAM roles
- Volumes
- Route tables
- Load balancers

With Lightsail, you get a simpler experience:

- Create instance
- Choose region
- Choose Ubuntu
- Choose plan
- Attach static IP
- Configure firewall
- SSH into server

That simplicity matters when the goal is to deploy quickly and learn the full flow.

### Good Use Cases for Lightsail

Lightsail is good for:

- Portfolio projects
- MVP apps
- Internal dashboards
- Small SaaS prototypes
- Demo environments
- Early client demos
- Learning cloud deployment

### Recommended Plan Selection

For a backend plus database on the same server, avoid the smallest RAM tier if the backend framework is heavy.

For example:

```text
Small static website: very small plan may be enough
Node.js API only: small plan may be okay
Spring Boot + MySQL: 2 GB RAM is safer
Spring Boot + Docker build on server: 2 GB+ strongly recommended
```

A common mistake is choosing the cheapest server and expecting it to build and run everything smoothly.

The runtime may be okay, but build processes can be much heavier than runtime.

---

## 8. Where Lightsail Is Not Enough

Lightsail is not the final answer for every scale.

For thousands of active users, high availability, auto-scaling, or enterprise-grade reliability, you would eventually move to something like:

```text
Frontend: Vercel or CloudFront
Backend: ECS Fargate / Kubernetes / EC2 Auto Scaling
Database: Amazon RDS Multi-AZ
Load Balancer: Application Load Balancer
Secrets: AWS Secrets Manager
Logs: CloudWatch
Monitoring: CloudWatch / Grafana / Datadog
```

Lightsail is excellent for:

```text
Learning + MVP + portfolio + early product validation
```

It is not ideal for:

```text
large-scale production + high traffic + strong uptime requirements
```

A professional engineer should understand both sides.

The key is not to over-engineer too early.

---

## 9. Why Docker Is Useful in This Architecture

Docker packages your application with its runtime environment.

Without Docker, server deployment often becomes:

```text
Install Java
Install Maven/Gradle
Install Node
Install MySQL
Set environment variables
Run app manually
Fix version mismatch
Debug missing dependencies
```

With Docker:

```text
Build image once
Run container anywhere
```

Docker helps with:

- Consistent runtime
- Easier deployment
- Cleaner server setup
- Faster rollback
- Repeatable builds
- Isolation between services

For a backend app, a Dockerfile may follow this pattern:

```dockerfile
# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY .. .
RUN ./gradlew clean build -x test

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
```

This is called a multi-stage Docker build.

The benefit:

- Build stage uses full JDK
- Runtime stage uses smaller JRE
- Final image does not need Gradle or source files

---

## 10. Why Docker Compose Is Useful

Docker Compose lets you define multiple containers in one file.

For this architecture, we need at least:

```text
backend container
mysql container
```

Instead of running many long `docker run` commands, we define services in `docker-compose.yml`.

Example structure:

```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: app_mysql
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: app_db
      MYSQL_USER: app_user
      MYSQL_PASSWORD: strong_password
      MYSQL_ROOT_PASSWORD: strong_root_password
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app_network

  backend:
    image: your-dockerhub-username/your-backend:latest
    container_name: app_backend
    restart: unless-stopped
    env_file:
      - .env.production
    ports:
      - "127.0.0.1:10000:10000"
    depends_on:
      - mysql
    networks:
      - app_network

volumes:
  mysql_data:

networks:
  app_network:
    driver: bridge
```

Important things here:

### `restart: unless-stopped`

This keeps containers running after server reboot.

### `volumes`

This keeps database data persistent.

### `networks`

This lets backend talk to database using service name like:

```text
mysql:3306
```

### `127.0.0.1:10000:10000`

This means the backend is exposed only locally on the server. Nginx will expose it publicly through HTTPS.

This is more secure than exposing the backend container directly to the internet.

---

## 11. Database Strategy: MySQL in Docker vs Managed Database

For low-cost deployment, MySQL in Docker is acceptable.

For real production, managed database is better.

### Option 1: MySQL in Docker

```text
Backend container
MySQL container
Same Lightsail server
Persistent Docker volume
```

Pros:

- Cheap
- Simple
- Full control
- Good for MVP
- Good for demos

Cons:

- You manage backups
- Same server failure affects app and DB
- Scaling is limited
- Manual recovery required

### Option 2: Managed Database

Examples:

- Amazon RDS MySQL
- Railway MySQL/PostgreSQL
- PlanetScale
- Neon PostgreSQL
- Supabase PostgreSQL

Pros:

- Automated backups
- Easier restore
- Better durability
- Better scaling
- Less operational burden

Cons:

- More cost
- More configuration
- Network/security setup needed

### Practical Recommendation

For portfolio/MVP:

```text
Docker MySQL with persistent volume is okay
```

For paying customers:

```text
Use managed database
```

---

## 12. Static IP Strategy

A cloud server usually gets a public IP.

But if the IP changes after restart, your DNS breaks.

That is why a static IP is important.

Without static IP:

```text
api.example.com → old IP → backend unreachable
```

With static IP:

```text
api.example.com → stable IP → backend reachable
```

In Lightsail, attach a static IP to your instance.

Then create DNS records pointing to that static IP.

Example:

```text
api.example.com  A  203.0.113.10
```

This means:

```text
api.example.com points to your backend server
```

---

## 13. Domain and Subdomain Strategy

A domain is not just a website name. It is your namespace on the internet.

If you own:

```text
example.com
```

You can create:

```text
app.example.com
api.example.com
admin.example.com
docs.example.com
blog.example.com
status.example.com
```

For a full-stack application, a clean structure is:

```text
app.example.com       → frontend
api.example.com       → backend API
admin.example.com     → admin frontend
docs.example.com      → documentation
status.example.com    → status page
```

For a simple setup:

```text
example.com           → frontend
api.example.com       → backend
```

This is professional and easy to understand.

### Recommended DNS Records

For frontend hosted on Vercel, Vercel usually provides the required DNS records.

For backend on Lightsail:

```text
Type: A
Name: api
Value: YOUR_STATIC_IP
TTL: 60 or automatic
```

This creates:

```text
api.example.com → your Lightsail server
```

---

## 14. Why HTTPS Is Mandatory

Modern browsers are strict.

If your frontend is hosted at:

```text
https://app.example.com
```

and it calls:

```text
http://api.example.com
```

then the browser may block the request as mixed content.

This is one of the most common deployment mistakes.

The rule is:

```text
HTTPS frontend must call HTTPS backend
```

So final API URL should be:

```text
https://api.example.com/api
```

not:

```text
http://YOUR_IP:10000/api
```

HTTPS is also required for secure cookies.

If your backend sets cookies like:

```text
Secure; HttpOnly; SameSite=None
```

then HTTPS is mandatory.

---

## 15. Reverse Proxy Strategy with Nginx

The backend application can run internally on port `10000`.

But users should not directly call:

```text
https://api.example.com:10000
```

Instead, they should call:

```text
https://api.example.com
```

Nginx receives HTTPS traffic on port `443` and forwards it to the backend running internally.

```text
User
  ↓ HTTPS 443
Nginx
  ↓ HTTP localhost:10000
Backend container
```

This is called a reverse proxy.

Basic Nginx reverse proxy idea:

```nginx
server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://127.0.0.1:10000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Then Certbot modifies this config to support HTTPS.

---

## 16. Environment Variable Strategy

Never hardcode production values inside source code.

Use environment variables for:

- Database URL
- Database username
- Database password
- JWT secret
- Allowed CORS origins
- Cookie settings
- API base URLs
- Mail settings
- Feature flags

Example backend `.env.production`:

```env
PORT=10000
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:mysql://mysql:3306/app_db
DATABASE_USERNAME=app_user
DATABASE_PASSWORD=replace_with_strong_password
JWT_SECRET=replace_with_long_random_secret
APP_CORS_ALLOWED_ORIGINS=https://app.example.com
COOKIE_SECURE=true
COOKIE_SAME_SITE=None
```

Example frontend environment variable:

```env
VITE_API_BASE_URL=https://api.example.com/api
```

Important:

```text
Backend env changes require backend restart.
Frontend env changes require frontend redeploy.
```

---

## 17. CORS Strategy

CORS means Cross-Origin Resource Sharing.

If frontend is:

```text
https://app.example.com
```

and backend is:

```text
https://api.example.com
```

then they are different origins.

The backend must explicitly allow the frontend origin.

Allowed origins should include:

```env
APP_CORS_ALLOWED_ORIGINS=https://app.example.com
```

For development, you may also allow:

```env
http://localhost:5173
```

A common production CORS config:

```env
APP_CORS_ALLOWED_ORIGINS=https://app.example.com,http://localhost:5173
```

### Common CORS Mistake

Wrong:

```env
APP_CORS_ALLOWED_ORIGINS=*
```

This is especially wrong if you use cookies.

For cookie-based auth, you need:

```text
Specific origin + credentials enabled
```

not wildcard.

---

## 18. Cookie and Authentication Strategy

If your backend uses cookie-based authentication, production requires careful configuration.

For cross-subdomain frontend/backend communication:

```text
Frontend: https://app.example.com
Backend:  https://api.example.com
```

cookies usually need:

```text
Secure
HttpOnly
SameSite=None
```

Why?

### `Secure`

Cookie is sent only over HTTPS.

### `HttpOnly`

JavaScript cannot read the cookie. This helps reduce XSS impact.

### `SameSite=None`

Allows cookie to be sent across related but different origins.

This is why HTTPS was not optional.

Without HTTPS, secure cookies will not work properly.

---

## 19. Build Strategy: Build on Server vs Build Locally

This is an important lesson.

There are two ways to deploy a Docker backend.

### Option 1: Build on Server

```bash
docker compose up -d --build
```

Pros:

- Simple
- No Docker registry needed initially

Cons:

- Slow on small servers
- Uses server RAM/CPU
- Can fail due to memory pressure
- Not ideal for production

### Option 2: Build Locally and Push Image

```text
Local machine / CI builds image
Push to Docker Hub or ECR
Server pulls image
Server only runs container
```

Pros:

- Faster server deployment
- More professional
- Lower server resource usage
- Better CI/CD path
- Repeatable image versions

Cons:

- Requires Docker registry
- Requires image tagging discipline

For small servers, this is usually better:

```bash
# Local machine
docker buildx build --platform linux/amd64 \
  -t your-dockerhub-username/your-backend:latest \
  --push .

# Server
docker compose pull backend
docker compose up -d backend
```

Professional recommendation:

```text
Build outside the server. Run on the server.
```

---

## 20. Cost Strategy

A beginner-friendly deployment should avoid unnecessary cost.

For an MVP:

| Layer | Low-Cost Choice |
|---|---|
| Frontend | Vercel |
| Backend | AWS Lightsail |
| Database | MySQL Docker volume |
| SSL | Let's Encrypt |
| Domain | Affordable domain registrar |
| Docker Registry | Docker Hub free tier |

This keeps cost predictable.

### Where Cost Can Increase

Cost increases when you add:

- Managed database
- Load balancer
- Multiple backend instances
- High traffic
- Monitoring platforms
- Paid email service
- CDN beyond frontend
- Backups and snapshots

### Practical Cost Mindset

For MVP:

```text
Keep it simple and predictable
```

For production:

```text
Pay for reliability where it matters most
```

Usually database reliability is the first thing worth paying for.

---

## 21. Security Strategy

Even for a low-cost deployment, you should follow basic security hygiene.

### Never Commit Secrets

Do not commit:

```text
.env.production
JWT secrets
Database passwords
API keys
Private keys
```

### Use Strong Passwords

Avoid:

```text
password123
admin123
projectname2024
```

Use long random secrets.

### Restrict Database Access

Do not expose MySQL port publicly.

The database should only be reachable inside Docker network.

### Use HTTPS

Always expose backend through HTTPS.

### Keep Server Updated

Use:

```bash
sudo apt update
sudo apt upgrade -y
```

### Check Running Containers

```bash
docker ps
```

### Check Logs

```bash
docker compose logs --tail=100 backend
```

### Backup Database

For Docker MySQL:

```bash
docker exec app_mysql mysqldump -uUSER -pPASSWORD DATABASE_NAME > backup.sql
```

Use placeholders, not real credentials, in documentation.

---

## 22. Operational Strategy

Deployment is not finished when the app starts once.

You should know how to:

- restart services
- check logs
- check memory
- check disk
- verify API health
- renew SSL
- pull new Docker image
- rollback if needed

Useful Linux commands:

```bash
# Check memory
free -h

# Check disk usage
df -h

# Check running containers
docker ps

# Check all containers
docker ps -a

# Check container logs
docker logs --tail=100 container_name

# Check Docker Compose logs
docker compose logs --tail=100 backend

# Restart containers
docker compose up -d

# Stop containers
docker compose down

# Check listening ports
sudo ss -tulpn

# Check Nginx status
sudo systemctl status nginx

# Test Nginx config
sudo nginx -t

# Reload Nginx
sudo systemctl reload nginx

# Test API
curl -i https://api.example.com/api/health
```

These commands are part of real deployment literacy.

---

## 23. Common Beginner Mistakes

### Mistake 1: Using HTTP backend with HTTPS frontend

Problem:

```text
https://frontend.com calls http://backend.com
```

Browser blocks it.

Fix:

```text
Use HTTPS backend
```

### Mistake 2: Forgetting CORS

Problem:

```text
Frontend domain not allowed by backend
```

Fix:

```text
Add frontend origin to backend CORS configuration
```

### Mistake 3: Not Using Static IP

Problem:

```text
Server IP changes, DNS breaks
```

Fix:

```text
Attach static IP
```

### Mistake 4: Exposing Backend Port Directly

Problem:

```text
api.example.com:10000
```

Fix:

```text
Use Nginx reverse proxy on 443
```

### Mistake 5: Building Heavy App on Tiny Server

Problem:

```text
Docker build consumes RAM and hangs
```

Fix:

```text
Build locally or in CI, push image, pull on server
```

### Mistake 6: Forgetting to Redeploy Frontend After Env Change

Problem:

```text
Vercel env changed, but app still uses old API URL
```

Fix:

```text
Redeploy frontend
```

### Mistake 7: Not Opening Firewall Ports

Problem:

```text
Nginx works locally but domain times out
```

Fix:

```text
Open port 80 and 443 in cloud firewall
```

---

## 24. Recommended Deployment Roadmap

A good roadmap looks like this:

### Phase 1: Local Readiness

- Backend builds locally
- Frontend builds locally
- Database schema is ready
- Dockerfile works locally
- Docker Compose works locally

### Phase 2: Server Setup

- Create Lightsail instance
- Attach static IP
- Configure firewall
- Install Docker
- Install Docker Compose
- Clone repository or prepare deployment folder

### Phase 3: Database Setup

- Start MySQL container
- Use persistent volume
- Load schema
- Verify tables
- Verify backend can connect

### Phase 4: Backend Setup

- Build/push backend Docker image
- Pull image on server
- Start backend container
- Test backend locally on server
- Test backend through public IP

### Phase 5: Domain Setup

- Buy domain
- Configure frontend domain
- Configure backend subdomain
- Verify DNS propagation

### Phase 6: Nginx + SSL

- Install Nginx
- Configure reverse proxy
- Test HTTP
- Install Certbot
- Enable HTTPS
- Test HTTPS

### Phase 7: Frontend Integration

- Set frontend API base URL
- Redeploy frontend
- Verify network requests
- Fix CORS if needed
- Test login/register/dashboard flows

### Phase 8: Hardening

- Disable unnecessary ports
- Keep only 22, 80, 443 open if possible
- Backup database
- Document commands
- Add monitoring later

---

## 25. Architecture Checklist Before Starting Part 2

Before starting backend deployment, make sure you have answers for these:

### Application

- [ ] Does backend build locally?
- [ ] Does frontend build locally?
- [ ] Do you know backend port?
- [ ] Do you know database type?
- [ ] Do you know required environment variables?

### Cloud

- [ ] Which region will you deploy to?
- [ ] Which Lightsail plan will you choose?
- [ ] Do you need 1 GB or 2 GB RAM?
- [ ] Will database run in Docker or managed service?

### Domain

- [ ] What will be frontend domain?
- [ ] What will be backend domain?
- [ ] Will backend use `api.example.com`?

### Security

- [ ] Are secrets outside Git?
- [ ] Is HTTPS planned?
- [ ] Are cookies configured for production?
- [ ] Are CORS origins known?

### Operations

- [ ] Do you know how to SSH?
- [ ] Do you know how to check logs?
- [ ] Do you know how to restart containers?
- [ ] Do you know how to verify DNS?

---

## 26. Conclusion

A good deployment is not just about getting the app online. It is about understanding the system around the app.

In this architecture, the frontend is hosted on Vercel because it is fast, globally distributed, and easy to manage. The backend is hosted on AWS Lightsail because it gives a low-cost Linux server with enough control to run Docker, MySQL, Nginx, and SSL. Docker makes the backend portable. Nginx makes the backend professional. Let's Encrypt makes HTTPS free. A custom domain makes the deployment credible.

The most important lessons are:

```text
Do not expose raw backend ports publicly as your final API URL.
Do not use HTTP backend with HTTPS frontend.
Do not hardcode secrets.
Do not build heavy Docker images on tiny servers if avoidable.
Do not ignore CORS.
Do not skip HTTPS.
```

A clean MVP deployment should be simple, affordable, and understandable.

This architecture gives exactly that.

In Part 2, we will move from architecture to implementation and deploy the backend on AWS Lightsail using Ubuntu, Docker, Docker Compose, MySQL, environment variables, static IP, and verification commands.

---

## Next Part

**Part 2 — Backend Deployment on AWS Lightsail**

In the next part, we will cover:

- Creating a Lightsail instance
- Choosing the correct plan
- Connecting with SSH
- Installing Docker
- Creating environment files
- Running MySQL with Docker Compose
- Loading schema
- Running backend container
- Debugging memory and build issues
- Testing backend with curl
- Preparing backend for Nginx and SSL

