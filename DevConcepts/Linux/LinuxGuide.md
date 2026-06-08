---
title: "Linux Skills Every Android, Backend, and Cloud Developer Should Know"
description: "A practical developer-focused Linux guide covering essential commands, file management, permissions, processes, networking, logs, Bash scripting, SSH, Git, Docker, Spring Boot deployment, Android development workflows, and AWS cloud operations."
date: "2026-06-08"
tags: [ "Linux", "Android", "Spring Boot", "Backend Development", "AWS", "Cloud", "DevOps", "Bash", "Docker" ]
---

# Linux Skills Every Android, Backend, and Cloud Developer Should Know

Linux is one of those skills that quietly becomes important as you grow as a developer.

At the beginning, you may only use it to move around folders or run a few commands. Later, it becomes part of your daily workflow: checking logs, running builds, connecting to servers, debugging APIs, deploying backend applications, working with Docker, managing cloud instances, and understanding why something is failing in production.

For an Android developer, Linux helps when working with Gradle, SDK tools, emulators, CI/CD pipelines, and build servers.

For a Spring Boot backend developer, Linux is useful for running Java applications, checking logs, managing ports, configuring services, and deploying applications on servers.

For a developer working with AWS or cloud environments, Linux is almost unavoidable. Many EC2 instances, Docker containers, CI runners, and production servers run on Linux.

This guide is not written for full-time Linux administrators. It is written for developers who want to become comfortable with the Linux skills that actually matter in real projects.

---

## Table of Contents

1. [Why Developers Should Learn Linux](#why-developers-should-learn-linux)
2. [Understanding the Linux File System](#understanding-the-linux-file-system)
3. [Basic Navigation Commands](#basic-navigation-commands)
4. [File and Directory Management](#file-and-directory-management)
5. [Reading and Editing Files](#reading-and-editing-files)
6. [Searching Files and Text](#searching-files-and-text)
7. [Permissions and Ownership](#permissions-and-ownership)
8. [Processes and Services](#processes-and-services)
9. [Networking Commands for Developers](#networking-commands-for-developers)
10. [Logs and Troubleshooting](#logs-and-troubleshooting)
11. [Package Management](#package-management)
12. [Environment Variables](#environment-variables)
13. [Bash Scripting Basics](#bash-scripting-basics)
14. [SSH and Remote Server Access](#ssh-and-remote-server-access)
15. [Linux for Android Developers](#linux-for-android-developers)
16. [Linux for Spring Boot Backend Developers](#linux-for-spring-boot-backend-developers)
17. [Linux for AWS and Cloud Developers](#linux-for-aws-and-cloud-developers)
18. [Docker Commands Developers Should Know](#docker-commands-developers-should-know)
19. [Git Commands on Linux](#git-commands-on-linux)
20. [A Practical Learning Path](#a-practical-learning-path)
21. [Final Thoughts](#final-thoughts)

---

## Why Developers Should Learn Linux

Linux is important because most modern development and deployment environments depend on it in some form.

You do not need to become a Linux expert on day one. But as a developer, you should be able to:

- Navigate the file system confidently.
- Read and edit configuration files.
- Understand file permissions.
- Check running processes and services.
- Debug port and network issues.
- Read application logs.
- Run scripts.
- Connect to remote servers using SSH.
- Work with Docker containers.
- Deploy or troubleshoot applications on cloud servers.

These are practical skills. They help you become independent when something breaks.

---

## Understanding the Linux File System

Linux follows a hierarchical file system. Everything starts from the root directory, represented by `/`.

Some important directories are:

| Directory | Purpose |
|---|---|
| `/home` | User home directories |
| `/root` | Home directory for the root user |
| `/etc` | System and application configuration files |
| `/var` | Logs, cache files, and variable application data |
| `/var/log` | System and application logs |
| `/tmp` | Temporary files |
| `/bin` | Essential user commands |
| `/sbin` | System administration commands |
| `/usr/bin` | User-installed command binaries |
| `/opt` | Optional third-party software |
| `/usr/local` | Locally installed software |

Example:

```bash
cd /
ls
```

This moves to the root directory and lists the folders inside it.

As a developer, you will commonly work inside project directories, `/var/log`, `/etc`, `/opt`, and your home directory.

---

## Basic Navigation Commands

These are the first commands every developer should know.

```bash
pwd
```

Shows the current directory.

```bash
ls
```

Lists files and folders.

```bash
ls -l
```

Shows files with details such as permissions, owner, size, and modified date.

```bash
ls -la
```

Shows all files, including hidden files.

```bash
cd /path/to/folder
```

Moves into a specific folder.

```bash
cd ..
```

Moves one level up.

```bash
cd ~
```

Moves to your home directory.

```bash
clear
```

Clears the terminal screen.

Useful example:

```bash
cd ~/projects/my-spring-app
ls -la
```

This is the kind of command you may use when opening a backend project on a Linux machine.

---

## File and Directory Management

Developers constantly create, copy, move, and delete files.

### Create Files and Folders

```bash
touch app.log
```

Creates an empty file.

```bash
mkdir logs
```

Creates a folder.

```bash
mkdir -p backend/src/main/resources
```

Creates nested folders if they do not already exist.

### Copy Files

```bash
cp application.properties application-dev.properties
```

Copies a file.

```bash
cp -r old-project new-project
```

Copies a folder recursively.

### Move or Rename Files

```bash
mv old-name.txt new-name.txt
```

Renames a file.

```bash
mv app.log logs/
```

Moves a file into another folder.

### Delete Files and Folders

```bash
rm old.log
```

Deletes a file.

```bash
rm -r old-folder
```

Deletes a folder and its contents.

```bash
rm -rf build/
```

Force deletes a folder.

Be careful with `rm -rf`. It does not ask for confirmation and can delete important files permanently.

---

## Reading and Editing Files

You do not always need a full editor. Many times, you just need to inspect a file quickly.

### Read Files

```bash
cat application.properties
```

Prints the full file content.

```bash
less application.log
```

Opens a large file in a scrollable view.

```bash
head app.log
```

Shows the first few lines.

```bash
tail app.log
```

Shows the last few lines.

```bash
tail -f app.log
```

Follows the file in real time. This is very useful for checking application logs while the app is running.

Example:

```bash
tail -f logs/spring-app.log
```

### Edit Files

Two common terminal editors are `nano` and `vim`.

```bash
nano application.properties
```

`nano` is beginner-friendly.

```bash
vim application.properties
```

`vim` is powerful but takes time to learn.

Basic `vim` commands:

| Command | Meaning |
|---|---|
| `i` | Insert mode |
| `Esc` | Exit insert mode |
| `:w` | Save |
| `:q` | Quit |
| `:wq` | Save and quit |
| `:q!` | Quit without saving |

---

## Searching Files and Text

Searching is one of the most useful Linux skills for developers.

### Find Files

```bash
find . -name "*.log"
```

Finds all `.log` files in the current folder.

```bash
find . -name "application*.properties"
```

Finds Spring Boot property files.

```bash
find . -type f -name "*.kt"
```

Finds Kotlin files.

```bash
find . -type f -name "*.java"
```

Finds Java files.

### Search Text Inside Files

```bash
grep "ERROR" app.log
```

Searches for `ERROR` inside a log file.

```bash
grep -i "failed" app.log
```

Case-insensitive search.

```bash
grep -r "TODO" .
```

Searches recursively inside the current project.

```bash
grep -rn "server.port" .
```

Searches recursively and shows line numbers.

Useful backend example:

```bash
grep -rn "spring.datasource" src/main/resources
```

Useful Android example:

```bash
grep -rn "minSdk" .
```

---

## Permissions and Ownership

Linux permissions decide who can read, write, or execute a file.

When you run:

```bash
ls -l
```

You may see something like this:

```bash
-rwxr-xr-- 1 deploy deploy 2048 Jun 8 10:30 start.sh
```

The permission part is:

```bash
rwxr-xr--
```

It means:

| Part | Meaning |
|---|---|
| `r` | Read |
| `w` | Write |
| `x` | Execute |
| First group | Owner permissions |
| Second group | Group permissions |
| Third group | Others permissions |

### Change Permissions

```bash
chmod +x start.sh
```

Makes a script executable.

```bash
chmod 644 application.properties
```

Owner can read/write. Others can read.

```bash
chmod 755 start.sh
```

Owner can read/write/execute. Others can read/execute.

### Change Ownership

```bash
sudo chown deploy:deploy app.jar
```

Changes the owner and group of a file.

```bash
sudo chown -R deploy:deploy /opt/myapp
```

Changes ownership recursively for a folder.

Developer tip: if your script says `Permission denied`, the first thing to check is whether it has execute permission.

```bash
chmod +x ./gradlew
```

This command is very common in Android and Spring Boot projects.

---

## Processes and Services

A process is a running program. As a developer, you often need to check whether your app is running, stop it, or find what is using a port.

### View Processes

```bash
ps aux
```

Shows running processes.

```bash
ps aux | grep java
```

Finds Java processes.

```bash
ps aux | grep gradle
```

Finds Gradle-related processes.

```bash
top
```

Shows live CPU and memory usage.

```bash
htop
```

A more user-friendly process viewer. It may need to be installed first.

### Stop Processes

```bash
kill 1234
```

Stops a process by process ID.

```bash
kill -9 1234
```

Force stops a process. Use this only when normal `kill` does not work.

### Manage Services

Linux servers usually manage applications through `systemd` services.

```bash
systemctl status nginx
```

Checks service status.

```bash
sudo systemctl start nginx
```

Starts a service.

```bash
sudo systemctl stop nginx
```

Stops a service.

```bash
sudo systemctl restart nginx
```

Restarts a service.

```bash
sudo systemctl enable nginx
```

Enables a service to start automatically after reboot.

Backend example:

```bash
sudo systemctl status my-spring-app
```

---

## Networking Commands for Developers

Networking commands are useful when APIs fail, servers are unreachable, or your app cannot connect to the backend.

### Check IP Address

```bash
ip a
```

Shows network interfaces and IP addresses.

```bash
hostname -I
```

Shows the machine IP address.

### Test Connectivity

```bash
ping google.com
```

Checks whether the network is reachable.

```bash
ping 8.8.8.8
```

Checks connectivity using an IP address.

If IP works but domain does not, it may be a DNS issue.

### DNS Lookup

```bash
nslookup example.com
```

Checks DNS resolution.

```bash
dig example.com
```

Shows detailed DNS information.

### Check Open Ports

```bash
ss -tuln
```

Shows listening TCP and UDP ports.

```bash
ss -tulnp
```

Shows listening ports with process information. You may need `sudo` for full details.

```bash
sudo lsof -i :8080
```

Shows which process is using port `8080`.

This is very useful for Spring Boot developers because `8080` is the default port.

### Test an API

```bash
curl http://localhost:8080/actuator/health
```

Calls a local API endpoint.

```bash
curl -I https://example.com
```

Shows response headers.

```bash
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{"name":"Milk","quantity":2}'
```

Sends a POST request with JSON data.

---

## Logs and Troubleshooting

Logs are one of the first places to look when something breaks.

Common log locations:

| Location | Purpose |
|---|---|
| `/var/log/syslog` | General logs on Ubuntu/Debian |
| `/var/log/messages` | General logs on some Red Hat-based systems |
| `/var/log/auth.log` | Authentication and SSH logs |
| `/var/log/nginx/` | Nginx logs |
| `/var/log/apache2/` | Apache logs |
| Application folder | Custom app logs |

Useful commands:

```bash
tail -f /var/log/syslog
```

Follows system logs.

```bash
grep "ERROR" app.log
```

Finds errors.

```bash
grep -i "exception" app.log
```

Finds exceptions without caring about uppercase or lowercase.

```bash
tail -n 200 app.log
```

Shows the last 200 lines.

```bash
journalctl -u my-spring-app
```

Shows logs for a systemd service.

```bash
journalctl -u my-spring-app -f
```

Follows logs for a service in real time.

```bash
journalctl -u my-spring-app --since "1 hour ago"
```

Shows logs from the last hour.

Practical troubleshooting flow:

1. Check whether the app is running.
2. Check whether the port is open.
3. Check the latest logs.
4. Search for errors or exceptions.
5. Restart the service only after understanding the issue.

---

## Package Management

Package managers help install and update software.

### Ubuntu/Debian

```bash
sudo apt update
```

Refreshes package information.

```bash
sudo apt upgrade
```

Upgrades installed packages.

```bash
sudo apt install nginx
```

Installs Nginx.

```bash
sudo apt install openjdk-21-jdk
```

Installs Java JDK 21.

```bash
sudo apt remove nginx
```

Removes a package.

### Red Hat/CentOS/Fedora/Amazon Linux

```bash
sudo dnf update
```

Updates packages on newer systems.

```bash
sudo dnf install nginx
```

Installs Nginx.

On some systems, you may see `yum` instead of `dnf`.

```bash
sudo yum install nginx
```

---

## Environment Variables

Environment variables are used to store configuration values such as API URLs, database passwords, profiles, and access keys.

```bash
printenv
```

Shows environment variables.

```bash
echo $JAVA_HOME
```

Prints the `JAVA_HOME` value.

```bash
export SPRING_PROFILES_ACTIVE=prod
```

Sets the active Spring profile for the current terminal session.

```bash
export DATABASE_URL="jdbc:mysql://localhost:3306/appdb"
```

Sets a database URL.

To make a variable permanent for your user, add it to `~/.bashrc` or `~/.zshrc`.

```bash
nano ~/.bashrc
```

Then add:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

Apply changes:

```bash
source ~/.bashrc
```

Developer tip: never hardcode secrets directly inside source code. Use environment variables, secret managers, or cloud-native configuration tools.

---

## Bash Scripting Basics

Bash scripting helps automate repetitive work.

### Simple Script

Create a file:

```bash
nano check-disk.sh
```

Add:

```bash
#!/bin/bash

df -h
```

Make it executable:

```bash
chmod +x check-disk.sh
```

Run it:

```bash
./check-disk.sh
```

### Variables

```bash
#!/bin/bash

APP_NAME="my-spring-app"
echo "Starting $APP_NAME"
```

### Conditions

```bash
#!/bin/bash

if systemctl is-active --quiet nginx; then
  echo "Nginx is running"
else
  echo "Nginx is not running"
fi
```

### Loops

```bash
#!/bin/bash

for file in *.log; do
  echo "Checking $file"
  grep "ERROR" "$file"
done
```

### Useful Developer Script: Check Port

```bash
#!/bin/bash

PORT=8080

if sudo lsof -i :$PORT > /dev/null; then
  echo "Port $PORT is already in use"
  sudo lsof -i :$PORT
else
  echo "Port $PORT is free"
fi
```

### Schedule Jobs with Cron

Open cron editor:

```bash
crontab -e
```

Run a script every day at midnight:

```bash
0 0 * * * /home/deploy/backup.sh
```

Run a script every 15 minutes:

```bash
*/15 * * * * /home/deploy/check-app.sh
```

---

## SSH and Remote Server Access

SSH is used to securely connect to remote Linux servers.

### Connect to a Server

```bash
ssh username@server-ip
```

Example:

```bash
ssh ubuntu@12.34.56.78
```

### Connect Using a Key File

```bash
ssh -i my-key.pem ubuntu@12.34.56.78
```

For AWS EC2, this is very common.

If the key permission is too open, fix it:

```bash
chmod 400 my-key.pem
```

### Copy Files to Server

```bash
scp app.jar ubuntu@12.34.56.78:/home/ubuntu/
```

Using a key:

```bash
scp -i my-key.pem app.jar ubuntu@12.34.56.78:/home/ubuntu/
```

### Copy Folder to Server

```bash
scp -r ./build ubuntu@12.34.56.78:/home/ubuntu/
```

### Secure SSH Practices

- Use key-based authentication.
- Avoid logging in as root directly.
- Keep private keys safe.
- Do not commit `.pem` files to Git.
- Restrict inbound SSH access in cloud security groups.

---

## Linux for Android Developers

Android developers may not deploy APKs directly to Linux servers every day, but Linux still appears in many parts of the Android workflow.

### Gradle Wrapper

Most Android projects use the Gradle wrapper.

```bash
./gradlew clean
```

Cleans the project.

```bash
./gradlew assembleDebug
```

Builds a debug APK.

```bash
./gradlew assembleRelease
```

Builds a release APK.

```bash
./gradlew bundleRelease
```

Builds a release AAB for Play Store upload.

```bash
./gradlew test
```

Runs unit tests.

```bash
./gradlew connectedAndroidTest
```

Runs instrumentation tests on a connected device or emulator.

If Gradle wrapper permission is missing:

```bash
chmod +x ./gradlew
```

### Android Debug Bridge

ADB is very useful when testing Android apps.

```bash
adb devices
```

Lists connected devices.

```bash
adb install app-debug.apk
```

Installs an APK.

```bash
adb uninstall com.example.app
```

Uninstalls an app.

```bash
adb logcat
```

Shows device logs.

```bash
adb logcat | grep "MyApp"
```

Filters logs.

```bash
adb shell pm list packages
```

Lists installed packages.

```bash
adb shell am force-stop com.example.app
```

Force stops an app.

```bash
adb shell pm clear com.example.app
```

Clears app data.

### Useful Android Build Server Checks

```bash
java -version
```

Checks Java version.

```bash
echo $ANDROID_HOME
```

Checks Android SDK path.

```bash
ls $ANDROID_HOME/platforms
```

Lists installed Android platforms.

```bash
ls $ANDROID_HOME/build-tools
```

Lists installed build tools.

For Android developers, Linux knowledge helps a lot when debugging CI/CD failures, Gradle permission issues, SDK setup problems, and emulator-related build errors.

---

## Linux for Spring Boot Backend Developers

Spring Boot developers often use Linux for running APIs, deploying JAR files, checking logs, and managing services.

### Build the Application

For Maven:

```bash
./mvnw clean package
```

For Gradle:

```bash
./gradlew bootJar
```

### Run a Spring Boot JAR

```bash
java -jar app.jar
```

Run with a specific profile:

```bash
java -jar app.jar --spring.profiles.active=prod
```

Run in the background:

```bash
nohup java -jar app.jar > app.log 2>&1 &
```

Check logs:

```bash
tail -f app.log
```

### Check Port 8080

```bash
sudo lsof -i :8080
```

Stop the process using port 8080:

```bash
kill <PID>
```

### Create a Systemd Service for Spring Boot

Create a service file:

```bash
sudo nano /etc/systemd/system/my-spring-app.service
```

Example service:

```ini
[Unit]
Description=My Spring Boot Application
After=network.target

[Service]
User=deploy
WorkingDirectory=/opt/my-spring-app
ExecStart=/usr/bin/java -jar /opt/my-spring-app/app.jar --spring.profiles.active=prod
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Reload systemd:

```bash
sudo systemctl daemon-reload
```

Start service:

```bash
sudo systemctl start my-spring-app
```

Enable service after reboot:

```bash
sudo systemctl enable my-spring-app
```

Check status:

```bash
sudo systemctl status my-spring-app
```

Check logs:

```bash
journalctl -u my-spring-app -f
```

This is one of the most practical Linux workflows for backend developers.

---

## Linux for AWS and Cloud Developers

If you work with AWS as a developer, Linux is useful for EC2, Docker, CI/CD, logs, deployments, and troubleshooting.

### EC2 Basics

Connect to an EC2 instance:

```bash
ssh -i my-key.pem ubuntu@ec2-public-ip
```

Update packages:

```bash
sudo apt update && sudo apt upgrade
```

Install Java:

```bash
sudo apt install openjdk-21-jdk
```

Check disk space:

```bash
df -h
```

Check memory:

```bash
free -h
```

Check CPU/load:

```bash
uptime
```

### AWS CLI Basics

Check AWS CLI version:

```bash
aws --version
```

Configure AWS CLI:

```bash
aws configure
```

List S3 buckets:

```bash
aws s3 ls
```

Copy file to S3:

```bash
aws s3 cp app.log s3://my-bucket/logs/app.log
```

Sync folder to S3:

```bash
aws s3 sync ./build s3://my-bucket/build
```

Check caller identity:

```bash
aws sts get-caller-identity
```

Developer tip: on production systems, prefer IAM roles over long-lived access keys whenever possible.

### Security Group Troubleshooting

If your backend is running but not reachable:

1. Check if the app is running.
2. Check if the app is listening on the correct port.
3. Check if the EC2 security group allows inbound traffic.
4. Check if the application binds to `0.0.0.0` instead of only `localhost`.
5. Check logs.

Useful command:

```bash
ss -tulnp
```

If your Spring Boot app only listens on `127.0.0.1`, external traffic may not reach it.

---

## Docker Commands Developers Should Know

Docker is common in backend and cloud development. Even Android developers may use Docker indirectly in CI/CD pipelines.

### Basic Docker Commands

```bash
docker --version
```

Checks Docker version.

```bash
docker ps
```

Shows running containers.

```bash
docker ps -a
```

Shows all containers.

```bash
docker images
```

Lists images.

```bash
docker pull nginx
```

Downloads an image.

```bash
docker run -d -p 8080:80 nginx
```

Runs Nginx and maps container port `80` to host port `8080`.

```bash
docker stop <container-id>
```

Stops a container.

```bash
docker rm <container-id>
```

Removes a container.

```bash
docker rmi <image-id>
```

Removes an image.

### Docker Logs and Debugging

```bash
docker logs <container-id>
```

Shows container logs.

```bash
docker logs -f <container-id>
```

Follows logs.

```bash
docker exec -it <container-id> bash
```

Opens a shell inside a container.

If `bash` is not available:

```bash
docker exec -it <container-id> sh
```

### Docker Compose

```bash
docker compose up
```

Starts services.

```bash
docker compose up -d
```

Starts services in the background.

```bash
docker compose down
```

Stops and removes services.

```bash
docker compose logs -f
```

Follows logs.

Backend developers commonly use Docker Compose for local databases like MySQL, PostgreSQL, Redis, or Kafka.

---

## Git Commands on Linux

Git is not Linux-specific, but many real development and deployment workflows use Git from the terminal.

```bash
git status
```

Shows changed files.

```bash
git branch
```

Shows branches.

```bash
git checkout -b feature/linux-guide
```

Creates and switches to a new branch.

```bash
git add .
```

Stages changes.

```bash
git commit -m "Add Linux guide"
```

Commits changes.

```bash
git pull origin main
```

Pulls latest changes.

```bash
git push origin feature/linux-guide
```

Pushes branch.

```bash
git log --oneline
```

Shows compact commit history.

```bash
git diff
```

Shows code differences.

Useful deployment-style command:

```bash
git pull && ./gradlew clean build
```

This pulls latest code and builds the project.

---

## A Practical Learning Path

You do not need to learn every Linux command at once. A better approach is to learn in layers.

### Stage 1: Daily Terminal Comfort

Focus on:

- `pwd`
- `ls`
- `cd`
- `mkdir`
- `touch`
- `cp`
- `mv`
- `rm`
- `cat`
- `less`
- `head`
- `tail`

Goal: move around the system and manage files without fear.

### Stage 2: Developer Debugging

Focus on:

- `grep`
- `find`
- `ps`
- `top`
- `kill`
- `lsof`
- `ss`
- `curl`
- `journalctl`

Goal: debug logs, ports, processes, and API issues.

### Stage 3: Backend and Cloud Workflow

Focus on:

- SSH
- SCP
- systemd
- environment variables
- package management
- Docker
- AWS CLI

Goal: deploy and troubleshoot applications on Linux servers.

### Stage 4: Automation

Focus on:

- Bash variables
- conditions
- loops
- functions
- cron jobs
- simple deployment scripts

Goal: automate repetitive developer tasks.

---

## Final Thoughts

Linux is not just an operating system skill. For developers, it is a productivity and troubleshooting skill.

You do not need to memorize every command. What matters is understanding what to use when something happens:

- If a build fails, check the environment, permissions, and logs.
- If an API is not reachable, check the process, port, firewall, and network.
- If a server is slow, check CPU, memory, disk, and running processes.
- If deployment fails, check service status and application logs.

For Android developers, Linux helps with Gradle, ADB, SDK setup, and CI/CD.

For Spring Boot developers, Linux helps with running JAR files, managing services, reading logs, and debugging ports.

For AWS and cloud developers, Linux helps with EC2, Docker, SSH, deployments, and production troubleshooting.

The best way to learn Linux is simple: use it in your real projects. Open the terminal, run commands, break small things safely, fix them, and slowly build confidence.

That is how Linux becomes a practical developer skill, not just a list of commands.
