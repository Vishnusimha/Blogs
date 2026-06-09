---
title: "Part 3: Frontend Deployment, Domains, SSL, and Troubleshooting"
date: 2026-05-30
slug: "aws-lightsail-deployment-part-3-frontend-ssl"
tags: [ "Vercel", "Domains", "SSL", "HTTPS", "CORS", "DNS" ]
summary: "Complete guide to connecting Vercel frontend to Lightsail backend: set up custom domains, configure DNS, enable HTTPS with Let's Encrypt, fix CORS and SSL issues."
categories: AWS
readTime: 23
---

# Part 3: Frontend Deployment, Domains, SSL, and Troubleshooting

## Connecting a Vercel Frontend to an AWS Lightsail Backend With a Custom Domain, HTTPS, Nginx, and Production-Ready CORS

> This is Part 3 of a practical deployment series for developers who want to deploy a full-stack application in a cost-conscious but professional way.  
> In Part 1, we planned the architecture.  
> In Part 2, we deployed the backend on AWS Lightsail with Docker, MySQL, and a static IP.  
> In this part, we connect the public frontend, domain, backend subdomain, HTTPS, and browser security rules properly.

---

## Who This Article Is For

This article is written for developers who have already built a frontend and backend application and now want to make them work together publicly.

It is especially useful if your stack looks like this:

```text
Frontend: React / Vite / Next.js / Angular / Vue
Frontend hosting: Vercel
Backend: Spring Boot / Node.js / Django / Laravel / Go / Express
Backend hosting: AWS Lightsail / EC2 / VPS
Database: MySQL / PostgreSQL
Deployment style: Docker / Docker Compose
Domain: custom domain such as example.com
```

The goal is simple:

```text
https://app.example.com        -> frontend on Vercel
https://api.example.com        -> backend on AWS Lightsail through Nginx + SSL
```

This removes temporary tunnel URLs, raw IP addresses, HTTP limitations, and browser security issues.

---

## Table of Contents

1. [What We Are Building in This Part](#1-what-we-are-building-in-this-part)
2. [Why the Frontend Worked Locally but Failed on Vercel](#2-why-the-frontend-worked-locally-but-failed-on-vercel)
3. [Understanding Domains and Subdomains](#3-understanding-domains-and-subdomains)
4. [Recommended Domain Structure](#4-recommended-domain-structure)
5. [Step 1: Buy or Use a Domain](#5-step-1-buy-or-use-a-domain)
6. [Step 2: Attach the Frontend Domain to Vercel](#6-step-2-attach-the-frontend-domain-to-vercel)
7. [Step 3: Create a Backend Subdomain](#7-step-3-create-a-backend-subdomain)
8. [Step 4: Verify DNS Propagation](#8-step-4-verify-dns-propagation)
9. [Step 5: Install Nginx on the Backend Server](#9-step-5-install-nginx-on-the-backend-server)
10. [Step 6: Configure Nginx as a Reverse Proxy](#10-step-6-configure-nginx-as-a-reverse-proxy)
11. [Step 7: Test HTTP Through the Domain](#11-step-7-test-http-through-the-domain)
12. [Step 8: Open HTTPS Port in the Cloud Firewall](#12-step-8-open-https-port-in-the-cloud-firewall)
13. [Step 9: Install HTTPS With Let's Encrypt and Certbot](#13-step-9-install-https-with-lets-encrypt-and-certbot)
14. [Step 10: Test HTTPS Backend API](#14-step-10-test-https-backend-api)
15. [Step 11: Update Backend CORS](#15-step-11-update-backend-cors)
16. [Step 12: Update Frontend Environment Variables in Vercel](#16-step-12-update-frontend-environment-variables-in-vercel)
17. [Step 13: Redeploy the Frontend](#17-step-13-redeploy-the-frontend)
18. [Step 14: Verify Everything From the Browser](#18-step-14-verify-everything-from-the-browser)
19. [Handling Authentication Cookies in Production](#19-handling-authentication-cookies-in-production)
20. [Why Temporary Tunnels Are Not a Long-Term Solution](#20-why-temporary-tunnels-are-not-a-long-term-solution)
21. [Troubleshooting Guide](#21-troubleshooting-guide)
22. [Security Checklist](#22-security-checklist)
23. [Deployment Checklist](#23-deployment-checklist)
24. [Final Architecture](#24-final-architecture)
25. [Lessons Learned](#25-lessons-learned)
26. [What I Would Improve Next](#26-what-i-would-improve-next)
27. [Conclusion](#27-conclusion)

---

## 1. What We Are Building in This Part

At this stage, the backend is already deployed on a cloud server.

It may be reachable using something like:

```text
http://SERVER_PUBLIC_IP:BACKEND_PORT
```

For example:

```text
http://203.0.113.10:10000
```

That is useful for quick testing, but it is not a good final setup.

A production-style deployment should look like this:

```text
User Browser
    |
    | HTTPS
    v
Frontend Domain
https://app.example.com
    |
    | API calls over HTTPS
    v
Backend API Domain
https://api.example.com
    |
    v
Nginx Reverse Proxy
    |
    v
Backend Container
http://127.0.0.1:10000
    |
    v
Database Container / Managed Database
```

The key improvements are:

- no raw IP address in frontend code
- no exposed backend port in frontend configuration
- no HTTP backend for a HTTPS frontend
- stable domain name
- HTTPS certificate
- reverse proxy
- clean CORS configuration
- professional frontend-backend separation

---

## 2. Why the Frontend Worked Locally but Failed on Vercel

One of the most common deployment surprises is this:

```text
The frontend works locally, but after deploying to Vercel, API calls fail with Network Error.
```

This usually happens because of browser security rules.

### Local development setup

During local development, you may have:

```text
Frontend: http://localhost:5173
Backend:  http://localhost:10000
```

or:

```text
Frontend: http://localhost:5173
Backend:  http://SERVER_IP:10000
```

This often works because both are using HTTP or because localhost is treated specially by browsers.

### Production frontend setup

When deployed to Vercel, the frontend becomes:

```text
https://your-frontend.vercel.app
```

or:

```text
https://app.example.com
```

Now the browser is running a secure HTTPS website.

If this HTTPS website tries to call:

```text
http://SERVER_IP:10000
```

the browser blocks it because it is mixed content.

### What is mixed content?

Mixed content means:

```text
HTTPS page trying to call HTTP resource
```

Example:

```text
Frontend: https://app.example.com
Backend:  http://api.example.com
```

The browser sees this as insecure.

So the correct solution is:

```text
Frontend: https://app.example.com
Backend:  https://api.example.com
```

This is why adding a domain and HTTPS to the backend is not optional for a serious public deployment.

---

## 3. Understanding Domains and Subdomains

When you buy a domain like:

```text
example.com
```

you are not just buying one website address. You are buying a namespace.

That means you can create many subdomains under it.

Examples:

```text
example.com
www.example.com
app.example.com
api.example.com
admin.example.com
docs.example.com
blog.example.com
status.example.com
```

Each subdomain can point to a completely different place.

For example:

| Domain | Purpose | Hosting |
|---|---|---|
| `example.com` | company landing page | Vercel |
| `app.example.com` | frontend application | Vercel |
| `api.example.com` | backend API | AWS Lightsail |
| `admin.example.com` | admin panel | Vercel |
| `docs.example.com` | documentation | GitHub Pages |
| `status.example.com` | status page | external provider |

This is how real companies organize their infrastructure.

---

## 4. Recommended Domain Structure

For a full-stack application, I recommend this structure:

```text
example.com              -> main landing page or portfolio
app.example.com          -> frontend application
api.example.com          -> backend API
admin.example.com        -> admin dashboard, if separate
docs.example.com         -> documentation
staging.example.com      -> staging frontend
api-staging.example.com  -> staging backend
```

For a small project, you can keep it simple:

```text
app.example.com  -> frontend
api.example.com  -> backend
```

If the root domain itself is the frontend, then:

```text
example.com      -> frontend
api.example.com  -> backend
```

This is clean, scalable, and easy to understand.

---

## 5. Step 1: Buy or Use a Domain

You can buy a domain from many providers:

- Vercel Domains
- Porkbun
- Namecheap
- Cloudflare Registrar
- GoDaddy
- Google-like registrar alternatives depending on region

The registrar is where you own the domain. Hosting is separate.

For example:

```text
Domain registrar: Vercel / Porkbun / Namecheap
Frontend hosting: Vercel
Backend hosting: AWS Lightsail
Database: MySQL
```

You can buy the domain from one company and host the app somewhere else.

### Things to check before buying a domain

| Check | Why it matters |
|---|---|
| First-year price | Some domains are very cheap initially |
| Renewal price | The real yearly cost after the first year |
| WHOIS privacy | Protects personal contact details |
| DNS management | Required to create subdomains |
| Auto-renew | Prevents losing the domain accidentally |
| Transfer policy | Useful if you move registrars later |

### Which extension should you choose?

Common domain extensions:

```text
.com
.dev
.app
.xyz
.tech
.site
.online
.cloud
.in
```

For a developer portfolio or startup-style project, these are usually acceptable:

```text
.dev
.app
.xyz
.tech
.cloud
```

For a business brand, `.com` is still the most familiar, but it is often more expensive.

For a cost-conscious project, `.xyz` is commonly used and usually affordable.

---

## 6. Step 2: Attach the Frontend Domain to Vercel

If your frontend is hosted on Vercel, go to:

```text
Vercel Dashboard
    -> Project
        -> Settings
            -> Domains
```

Add your domain:

```text
app.example.com
```

or:

```text
example.com
```

Vercel will show you the DNS records required.

If the domain is also registered through Vercel, most of this may be configured automatically.

If the domain is bought elsewhere, you usually need to add records like:

```text
Type: CNAME
Name: app
Value: cname.vercel-dns.com
```

or for root domain:

```text
Type: A
Name: @
Value: Vercel-provided IP
```

Vercel normally guides this clearly in the domain settings page.

### Important

Do not confuse:

```text
Project Domains
```

with:

```text
Domain DNS Records
```

Project domain settings attach a domain to one Vercel project.

DNS records control where subdomains point globally.

---

## 7. Step 3: Create a Backend Subdomain

For the backend, create a subdomain such as:

```text
api.example.com
```

This should point to the public static IP of the backend server.

In your domain DNS settings, add:

```text
Type: A
Name: api
Value: YOUR_SERVER_STATIC_IP
TTL: 60 or default
```

Example:

```text
Type: A
Name: api
Value: 203.0.113.10
TTL: 60
```

This means:

```text
api.example.com -> 203.0.113.10
```

If using AWS Lightsail, use the static IP attached to the instance, not the temporary public IP.

### Why static IP matters

Cloud servers can change public IPs if stopped or recreated.

A static IP gives you a stable address.

Without it, DNS can break when your server IP changes.

---

## 8. Step 4: Verify DNS Propagation

After adding the DNS record, verify it.

From the server or your local machine:

```bash
nslookup api.example.com
```

Expected output:

```text
Name: api.example.com
Address: YOUR_SERVER_STATIC_IP
```

You can also use:

```bash
dig api.example.com
```

or:

```bash
host api.example.com
```

Do not rely only on `ping`.

Some servers block ICMP ping.

So this may fail:

```bash
ping api.example.com
```

but HTTP/HTTPS may still work perfectly.

Better test:

```bash
curl -i http://api.example.com:BACKEND_PORT/health
```

or:

```bash
curl -i http://api.example.com:BACKEND_PORT/api/some-public-endpoint
```

---

## 9. Step 5: Install Nginx on the Backend Server

Nginx will sit in front of the backend container.

Why use Nginx?

- It receives public traffic on ports 80 and 443.
- It forwards traffic to the backend running on localhost.
- It handles HTTPS certificates.
- It hides the internal backend port.
- It gives a clean production architecture.

Install Nginx:

```bash
sudo apt update
sudo apt install nginx -y
```

Check status:

```bash
sudo systemctl status nginx
```

Expected:

```text
active (running)
```

Useful commands:

```bash
sudo systemctl start nginx
sudo systemctl stop nginx
sudo systemctl restart nginx
sudo systemctl reload nginx
sudo nginx -t
```

`reload` is preferred after config changes because it applies changes without fully stopping Nginx.

---

## 10. Step 6: Configure Nginx as a Reverse Proxy

Create a new Nginx site configuration:

```bash
sudo nano /etc/nginx/sites-available/api.example.com
```

Paste a reverse proxy configuration:

```nginx
server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://127.0.0.1:BACKEND_PORT;
        proxy_http_version 1.1;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Replace:

```text
api.example.com
```

with your backend subdomain.

Replace:

```text
BACKEND_PORT
```

with your backend container port, such as:

```text
10000
```

So it may become:

```nginx
server {
    listen 80;
    server_name api.example.com;

    location / {
        proxy_pass http://127.0.0.1:10000;
        proxy_http_version 1.1;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable the site:

```bash
sudo ln -s /etc/nginx/sites-available/api.example.com /etc/nginx/sites-enabled/
```

Test Nginx config:

```bash
sudo nginx -t
```

If successful:

```bash
sudo systemctl reload nginx
```

---

## 11. Step 7: Test HTTP Through the Domain

Before adding HTTPS, test HTTP:

```bash
curl -i http://api.example.com/api/public-health-endpoint
```

or:

```bash
curl -i http://api.example.com/health
```

Use any safe public endpoint your backend exposes.

If the backend is configured with a context path such as `/api`, remember to include it.

Example:

```bash
curl -i http://api.example.com/api/v1/public/settings
```

A successful response looks like:

```text
HTTP/1.1 200 OK
Server: nginx
Content-Type: application/json
```

If this works, then:

```text
Domain -> Nginx -> Backend
```

is working over HTTP.

---

## 12. Step 8: Open HTTPS Port in the Cloud Firewall

Before running Certbot, make sure the cloud firewall allows HTTPS.

For AWS Lightsail, go to:

```text
Lightsail Dashboard
    -> Instance
        -> Networking
            -> IPv4 Firewall
```

Open these ports:

| Purpose | Protocol | Port | Source |
|---|---|---|---|
| SSH | TCP | 22 | Your IP or Any IPv4 |
| HTTP | TCP | 80 | Any IPv4 |
| HTTPS | TCP | 443 | Any IPv4 |

During early testing, you may also have backend port exposed:

| Purpose | Protocol | Port | Source |
|---|---|---|---|
| Backend direct test | TCP | 10000 | Any IPv4 or restricted IP |

But after Nginx and HTTPS are working, you can restrict or remove direct public access to the backend port.

For production-style security, Nginx should be the public entry point.

---

## 13. Step 9: Install HTTPS With Let's Encrypt and Certbot

Install Certbot and the Nginx plugin:

```bash
sudo apt install certbot python3-certbot-nginx -y
```

Generate a certificate:

```bash
sudo certbot --nginx -d api.example.com
```

Certbot will ask for:

- email address
- Terms of Service agreement
- optional email sharing preference

Use your real email for renewal notices.

If asked whether to redirect HTTP to HTTPS, choose redirect.

This means:

```text
http://api.example.com
```

will automatically redirect to:

```text
https://api.example.com
```

Certbot will update your Nginx config automatically and install SSL files under:

```text
/etc/letsencrypt/live/api.example.com/
```

Useful certificate commands:

```bash
sudo certbot certificates
sudo certbot renew --dry-run
sudo systemctl status certbot.timer
```

Certbot usually installs an automatic renewal timer.

---

## 14. Step 10: Test HTTPS Backend API

Now test HTTPS:

```bash
curl -i https://api.example.com/api/public-health-endpoint
```

or:

```bash
curl -i https://api.example.com/api/v1/public/settings
```

Expected:

```text
HTTP/1.1 200 OK
Server: nginx
Content-Type: application/json
```

Also test from your local machine, not only from the server:

```bash
curl -i https://api.example.com/api/public-health-endpoint
```

This confirms that the backend is publicly reachable over HTTPS.

### If HTTPS works locally on server but not from your laptop

Run on server:

```bash
sudo ss -tulpn | grep ':443'
```

If Nginx listens on 443:

```text
0.0.0.0:443
```

but your laptop times out, then the cloud firewall probably does not allow port 443.

Open HTTPS in the cloud firewall.

---

## 15. Step 11: Update Backend CORS

CORS is one of the most common frontend-backend deployment issues.

If your frontend runs on:

```text
https://app.example.com
```

and your backend runs on:

```text
https://api.example.com
```

these are different origins.

So the backend must explicitly allow the frontend origin.

Example environment variable:

```env
APP_CORS_ALLOWED_ORIGINS=https://app.example.com,https://example.com,http://localhost:5173
```

Then restart your backend container:

```bash
docker compose --env-file .env.production -f docker-compose.production.yml up -d
```

or:

```bash
docker compose up -d
```

depending on your file names.

### Test CORS preflight

Use an OPTIONS request:

```bash
curl -i -X OPTIONS "https://api.example.com/api/auth/login" \
  -H "Origin: https://app.example.com" \
  -H "Access-Control-Request-Method: POST"
```

Expected header:

```text
Access-Control-Allow-Origin: https://app.example.com
```

If this header is missing, the browser may block frontend requests.

### Common CORS mistake

This is wrong:

```env
APP_CORS_ALLOWED_ORIGINS=*
```

if using cookies or credentials.

For credentialed requests, the backend must return the exact frontend origin, not `*`.

Correct:

```env
APP_CORS_ALLOWED_ORIGINS=https://app.example.com
```

---

## 16. Step 12: Update Frontend Environment Variables in Vercel

Now update the frontend API base URL.

Go to:

```text
Vercel Dashboard
    -> Project
        -> Settings
            -> Environment Variables
```

Set:

```env
VITE_API_BASE_URL=https://api.example.com/api
```

or if your frontend code automatically appends `/api`, use:

```env
VITE_API_BASE_URL=https://api.example.com
```

This depends on how your frontend HTTP client is written.

### Important for Vite

Vite only exposes environment variables that start with:

```text
VITE_
```

So this works:

```env
VITE_API_BASE_URL=https://api.example.com/api
```

But this does not automatically work in frontend code:

```env
API_BASE_URL=https://api.example.com/api
```

### Production vs Preview vs Development

In Vercel, environment variables can be scoped to:

- Production
- Preview
- Development

For a production domain, apply to Production.

For pull request deployments, also apply to Preview if needed.

---

## 17. Step 13: Redeploy the Frontend

After changing Vercel environment variables, redeploy.

Why?

Because frontend environment variables are usually injected at build time.

For Vercel:

```text
Project
    -> Deployments
        -> Latest Deployment
            -> Redeploy
```

or push a new commit:

```bash
git add .
git commit -m "Update production API base URL"
git push
```

After redeployment, open the website and check browser DevTools.

---

## 18. Step 14: Verify Everything From the Browser

Open the frontend:

```text
https://app.example.com
```

Open browser DevTools:

```text
Right click -> Inspect -> Network
```

Trigger a frontend action that calls the backend.

Examples:

- login
- register
- fetch dashboard
- fetch public settings
- fetch marketplace data

Check that API calls go to:

```text
https://api.example.com/...
```

They should not go to:

```text
http://SERVER_IP:BACKEND_PORT
```

They should not go to:

```text
localhost
```

They should not go to:

```text
temporary-tunnel-url.trycloudflare.com
```

### What success looks like

In DevTools Network tab:

```text
Status: 200 / 201 / 204
Request URL: https://api.example.com/...
No CORS error
No Mixed Content error
No Network Error
```

---

## 19. Handling Authentication Cookies in Production

Authentication is where many deployments fail.

If your backend uses HttpOnly cookies, production settings matter.

For cross-subdomain frontend-backend architecture:

```text
Frontend: https://app.example.com
Backend:  https://api.example.com
```

cookies often need:

```text
Secure
HttpOnly
SameSite=None
```

### Why Secure?

`Secure` means the cookie is only sent over HTTPS.

This is required for safe production authentication.

### Why SameSite=None?

If the frontend and backend are on different subdomains, some flows may require cross-site cookie behavior.

For example:

```text
app.example.com -> api.example.com
```

Depending on browser rules and backend cookie domain/path settings, `SameSite=None` may be needed.

### Example environment variables

```env
SECURITY_AUTH_COOKIE_SECURE=true
SECURITY_AUTH_COOKIE_SAME_SITE=None
```

### Important

If you set:

```env
SECURITY_AUTH_COOKIE_SECURE=true
```

but call the backend over HTTP, cookies will not work correctly.

So this combination is required:

```text
HTTPS backend + Secure cookies
```

---

## 20. Why Temporary Tunnels Are Not a Long-Term Solution

During debugging, tools like Cloudflare Tunnel or ngrok can be useful.

They provide temporary HTTPS URLs such as:

```text
https://random-name.trycloudflare.com
```

This helps test:

```text
HTTPS frontend -> HTTPS backend
```

without buying or configuring a domain immediately.

But quick tunnels are not ideal long term because:

- URL may change
- terminal/session must keep running
- no uptime guarantee
- not suitable as a permanent recruiter/demo URL
- not clean for production environment variables

Tunnels are useful for:

- quick testing
- demos during development
- webhook testing
- debugging mixed content issues

For real deployment, use:

```text
api.example.com + Nginx + Let's Encrypt
```

---

## 21. Troubleshooting Guide

This section covers the most common issues and how to debug them.

---

### Issue 1: Vercel shows Network Error

Possible causes:

1. Frontend is HTTPS but backend is HTTP.
2. CORS does not allow the frontend origin.
3. Backend is down.
4. Wrong API base URL in Vercel.
5. Environment variable was changed but frontend was not redeployed.

Check frontend API URL in DevTools.

Expected:

```text
https://api.example.com/...
```

If it shows HTTP:

```text
http://SERVER_IP:10000/...
```

fix Vercel environment variable.

---

### Issue 2: Mixed Content Error

Browser console may show:

```text
Mixed Content: The page at https://app.example.com was loaded over HTTPS, but requested an insecure resource http://...
```

Fix:

```text
Use HTTPS backend URL
```

Update:

```env
VITE_API_BASE_URL=https://api.example.com/api
```

Redeploy frontend.

---

### Issue 3: CORS Error

Browser console may show:

```text
Access to XMLHttpRequest has been blocked by CORS policy
```

Fix backend CORS:

```env
APP_CORS_ALLOWED_ORIGINS=https://app.example.com,https://example.com,http://localhost:5173
```

Restart backend:

```bash
docker compose up -d
```

Test preflight:

```bash
curl -i -X OPTIONS "https://api.example.com/api/auth/login" \
  -H "Origin: https://app.example.com" \
  -H "Access-Control-Request-Method: POST"
```

Look for:

```text
Access-Control-Allow-Origin: https://app.example.com
```

---

### Issue 4: HTTPS certificate installed but public request times out

Check if Nginx is listening:

```bash
sudo ss -tulpn | grep ':443'
```

If Nginx listens locally but public request times out, open port 443 in the cloud firewall.

For AWS Lightsail:

```text
Networking -> IPv4 Firewall -> Add HTTPS TCP 443 Any IPv4
```

---

### Issue 5: HTTP works but HTTPS does not

Check Nginx config:

```bash
sudo nginx -t
```

Check Certbot certificates:

```bash
sudo certbot certificates
```

Check Nginx status:

```bash
sudo systemctl status nginx
```

Check local HTTPS:

```bash
curl -k -i https://127.0.0.1/api/public-health-endpoint
```

---

### Issue 6: DNS works on server but not on laptop

DNS propagation can take time.

Check:

```bash
nslookup api.example.com
```

```bash
dig api.example.com
```

Try from another network or use public DNS tools.

Also flush DNS cache if needed.

On macOS:

```bash
sudo dscacheutil -flushcache
sudo killall -HUP mDNSResponder
```

---

### Issue 7: Curl to `https://127.0.0.1` from Mac fails

This is expected if you run it on your Mac.

```text
127.0.0.1 means the current machine.
```

So from your Mac:

```text
https://127.0.0.1
```

means your Mac, not your server.

To test server localhost, run the command inside SSH on the server.

---

### Issue 8: Backend direct IP works but domain does not

Check DNS:

```bash
nslookup api.example.com
```

Check that it points to the server static IP.

Check Nginx config:

```bash
sudo cat /etc/nginx/sites-available/api.example.com
```

Check server name:

```nginx
server_name api.example.com;
```

Reload Nginx:

```bash
sudo nginx -t
sudo systemctl reload nginx
```

---

### Issue 9: Backend container is running but API is not responding

Check containers:

```bash
docker ps
```

Check backend logs:

```bash
docker compose logs --tail=100 backend
```

Check whether backend is listening:

```bash
curl -i http://127.0.0.1:BACKEND_PORT/health
```

Check memory:

```bash
free -h
docker stats --no-stream
```

---

### Issue 10: Secure cookies not working

Check the response headers from login/register:

```bash
curl -i https://api.example.com/api/auth/login
```

Look for:

```text
Set-Cookie: ... Secure; HttpOnly; SameSite=None
```

Also confirm frontend requests include credentials.

For Axios:

```javascript
axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true,
});
```

For fetch:

```javascript
fetch(url, {
  credentials: "include",
});
```

---

## 22. Security Checklist

Before considering the deployment complete, verify:

- [ ] Backend uses HTTPS.
- [ ] Frontend uses HTTPS.
- [ ] Backend API is behind Nginx.
- [ ] Direct backend port is not required publicly.
- [ ] Database port is not publicly exposed.
- [ ] Environment secrets are not committed to Git.
- [ ] JWT secrets or app secrets are strong.
- [ ] Cookies use `Secure` in production.
- [ ] Cookies use `HttpOnly` when possible.
- [ ] CORS only allows trusted frontend domains.
- [ ] SSL certificate auto-renewal is active.
- [ ] SSH key access is protected.
- [ ] Static IP is attached to the server.
- [ ] Backups/snapshots are configured.

---

## 23. Deployment Checklist

Use this as a final practical checklist.

### Domain and DNS

- [ ] Domain purchased.
- [ ] Frontend domain added to Vercel.
- [ ] Backend subdomain created.
- [ ] `api.example.com` points to backend static IP.
- [ ] DNS propagation verified with `nslookup`.

### Backend server

- [ ] Backend container running.
- [ ] Database running.
- [ ] Backend responds on localhost.
- [ ] Nginx installed.
- [ ] Nginx reverse proxy configured.
- [ ] HTTP domain test passes.
- [ ] HTTPS certificate installed.
- [ ] HTTPS domain test passes.

### Frontend

- [ ] Vercel environment variable updated.
- [ ] Frontend redeployed.
- [ ] Browser DevTools shows API calls to HTTPS backend.
- [ ] Login/register/data fetch works.

### CORS/Auth

- [ ] Backend allows frontend origin.
- [ ] Preflight OPTIONS request works.
- [ ] Cookies are secure.
- [ ] Credentials are included from frontend HTTP client.

---

## 24. Final Architecture

The final architecture looks like this:

```text
User Browser
    |
    | HTTPS
    v
Frontend Application
https://app.example.com
    |
    | HTTPS API calls
    v
Backend API Domain
https://api.example.com
    |
    v
Nginx Reverse Proxy
Ports: 80, 443
    |
    v
Backend Container
http://127.0.0.1:10000
    |
    v
Database
MySQL / PostgreSQL
```

A more detailed view:

```text
Vercel
  └── Frontend build
       └── Environment variable:
           VITE_API_BASE_URL=https://api.example.com/api

DNS
  ├── app.example.com  -> Vercel
  └── api.example.com  -> AWS Lightsail static IP

AWS Lightsail
  ├── Nginx
  │    ├── listens on 80
  │    ├── listens on 443
  │    └── proxies to 127.0.0.1:10000
  ├── Backend Docker container
  └── Database Docker container or managed database
```

This is a clean and professional low-cost deployment model.

---

## 25. Lessons Learned

A few key lessons from this deployment process:

### 1. A working backend IP is not enough

A raw backend IP over HTTP may pass curl tests, but fail in the browser when connected to a HTTPS frontend.

### 2. HTTPS is mandatory for real frontend-backend integration

Modern browsers enforce strict security. A public frontend on HTTPS needs a backend on HTTPS.

### 3. Domains are not just for branding

A domain enables:

- clean URLs
- SSL certificates
- professional frontend/backend separation
- stable API endpoints
- better cookie handling

### 4. Nginx is a simple but powerful production component

Nginx helps turn a raw backend container into a public production API.

### 5. CORS must be configured intentionally

CORS is not something to randomly disable. It should allow only trusted frontend origins.

### 6. Environment variables need redeployment

Changing a Vercel environment variable does not affect an already-built frontend until redeployment.

### 7. Temporary tunnels are useful, not permanent

Cloudflare Tunnel or ngrok are good for debugging, but a real domain + HTTPS is better for long-running demos.

---

## 26. What I Would Improve Next

This setup is strong for a portfolio, MVP, or low-traffic SaaS. But there are natural improvements.

### Short-term improvements

- Remove public access to backend container port.
- Keep only ports 80 and 443 public.
- Add a monitoring endpoint.
- Add server resource alerts.
- Add automatic database backups.
- Add log rotation.
- Add deployment scripts.

### Medium-term improvements

- Push backend Docker image to a registry.
- Pull images on the server instead of building there.
- Add GitHub Actions CI/CD.
- Add staging environment.
- Add `staging.example.com` and `api-staging.example.com`.

### Long-term improvements

- Move database to managed RDS.
- Move backend to ECS Fargate or Kubernetes.
- Add load balancer.
- Add autoscaling.
- Add centralized logging.
- Add secrets manager.
- Add infrastructure-as-code with Terraform.

The important thing is this: the current setup gives a strong foundation without over-engineering too early.

---

## 27. Conclusion

Deploying a full-stack application is not just about uploading code somewhere.

A real deployment involves:

- domain planning
- DNS records
- frontend hosting
- backend hosting
- reverse proxy configuration
- SSL certificates
- environment variables
- browser security
- CORS
- cookies
- troubleshooting

In this deployment model, the frontend runs on Vercel and the backend runs on a cloud server behind Nginx with HTTPS.

The final result is:

```text
Frontend: https://app.example.com
Backend:  https://api.example.com
```

This looks professional, works reliably, and teaches many real-world deployment concepts that are useful beyond one project.

For students and beginner developers, the biggest takeaway is this:

```text
A deployment is not finished when the server starts.
A deployment is finished when the frontend, backend, domain, HTTPS, CORS, cookies, and browser behavior all work together.
```

That is the difference between a local project and a real production-style application.

---

## Appendix: Useful Command Reference

### DNS

```bash
nslookup api.example.com
dig api.example.com
host api.example.com
```

### Nginx

```bash
sudo systemctl status nginx
sudo nginx -t
sudo systemctl reload nginx
sudo systemctl restart nginx
sudo ss -tulpn | grep nginx
```

### Certbot

```bash
sudo certbot --nginx -d api.example.com
sudo certbot certificates
sudo certbot renew --dry-run
sudo systemctl status certbot.timer
```

### Docker

```bash
docker ps
docker ps -a
docker compose up -d
docker compose down
docker compose logs --tail=100 backend
docker stats --no-stream
```

### HTTP Testing

```bash
curl -i http://api.example.com/api/public-health-endpoint
curl -i https://api.example.com/api/public-health-endpoint
curl -i --max-time 20 https://api.example.com/api/public-health-endpoint
```

### CORS Testing

```bash
curl -i -X OPTIONS "https://api.example.com/api/auth/login" \
  -H "Origin: https://app.example.com" \
  -H "Access-Control-Request-Method: POST"
```

### Server Resource Checks

```bash
free -h
df -h
top
htop
docker stats --no-stream
```

---

**End of Part 3.**
