# 🚢 Docker Overview for Beginners and Beyond

> 📎 [View with Images on GitHub](https://github.com/Vishnusimha/Blogs/blob/main/Spring/Docker.md)

## 🌟 Introduction

Docker is a powerful tool designed to make it easier to create, deploy, and run applications using containers. Containers let you package up an application with everything it needs—code, runtime, libraries, and dependencies—so it runs quickly and reliably in any environment.

Think of it this way: Just as a shipping container standardizes how goods are transported across land and sea, Docker standardizes how software is built, shipped, and run—across local machines, servers, or the cloud.

Whether you're a developer, DevOps engineer, or simply curious about modern software delivery, understanding Docker is essential for working in today's tech landscape.

---

## 🧠 Key Concepts for Beginners

Before we dive deep, let's break down some important terms:

* **Image**: A snapshot of your app with all its dependencies (like a JAR file in Java).
* **Container**: A running instance of an image (like an object created from a class).
* **Dockerfile**: A script that defines how to build your image.
* **Docker CLI**: Command-line tool to manage Docker containers, images, volumes, and networks.
* **Docker Compose**: Tool for running multi-container apps with a single command.
* **Docker Hub**: A registry where Docker images can be stored and shared.

---

## 📦 Comparing Images, Containers, and Runtimes

### 🔹 Image (Container Image / Docker Image)

An **image** is a read-only template with instructions for creating a Docker container. It contains:

* A minimal operating system (e.g., Alpine Linux)
* Application binaries and dependencies
* Environment configurations
* Startup command

**Analogy (Java):**
Just like Java code is compiled into JAR files for distribution, Docker images are built and pushed to registries like Docker Hub.

---

### 🔹 Container

A **container** is a live, running instance of an image.

**Analogy (Java):**
Like how a Java class becomes usable only after instantiation (i.e., creating an object), an image becomes a running container when you start it.

---

### 🔹 Container Runtime (e.g., Docker Engine)

This is the software that runs containers on your system. It fetches images, sets up networking, mounts volumes, and manages the lifecycle of containers.

**Analogy (Java):**
Think of it as the Java Runtime Environment (JRE) that runs your compiled JARs.

---

## 📝 Docker Concepts Illustrated

📌 *Note: The original blog contains helpful images located at paths like `./dockerimages/36.png`. To view them properly, check the [original GitHub document](https://github.com/Vishnusimha/Blogs/blob/main/Spring/Docker.md).*

---

## 🧪 Quiz

<div align="center">
  <img alt="dockerimages" src="./dockerimages/36.png" />
</div>

<img alt="dockerimages" src="./dockerimages/4.png" />
<img alt="dockerimages" src="./dockerimages/5.png" />
<img alt="dockerimages" src="./dockerimages/6.png" />
<img alt="dockerimages" src="./dockerimages/11.png" />
<img alt="dockerimages" src="./dockerimages/23.png" />
<img alt="dockerimages" src="./dockerimages/24.png" />
<img alt="dockerimages" src="./dockerimages/29.png" />

---

## 🚀 Introduction to Docker

Visual walkthrough of Docker basics using:

* Architecture diagrams
* Practical use cases
* Beginner-friendly best practices

<img alt="Intro" src="./dockerimages/30.png" />
<img alt="Intro" src="./dockerimages/22.png" />
<img alt="Intro" src="./dockerimages/13.png" />
<img alt="Intro" src="./dockerimages/1.png" />
<img alt="Intro" src="./dockerimages/2.png" />
<img alt="Intro" src="./dockerimages/3.png" />

<img alt="best practice" src="./dockerimages/17.png" />
<img alt="dockerimages" src="./dockerimages/32.png" />
<img alt="dockerimages" src="./dockerimages/33.png" />
<img alt="dockerimages" src="./dockerimages/34.png" />
<img alt="dockerimages" src="./dockerimages/35.png" />

---

## 🛠️ Writing Dockerfiles

A **Dockerfile** defines the steps to build an image.

Example:

```Dockerfile
FROM openjdk:17-jdk-slim
COPY target/myapp.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

<img alt="Writingdockerfiles" src="./dockerimages/36.png" />
<img alt="Writingdockerfiles" src="./dockerimages/21.png" />

---

## 🖥️ Docker CLI Overview

Common Docker CLI commands organized by use case.

---

### 🔸 Image Management

```bash
docker build -t myapp .
docker images
docker rmi myapp
```

---

### 🔸 Container Management

```bash
docker run -d -p 8080:80 myapp
docker ps -a
docker logs <container_id>
```

---

### 🔸 Volume & Network Management

```bash
docker volume create mydata
docker network create mynet
```

---

## 🧱 Understanding Docker Layers

Docker builds images in layers—each instruction in a Dockerfile creates a new layer.

Benefits:

* Faster builds
* Layer caching
* Smaller updates

<img alt="LayeringDocker" src="./dockerimages/7.png" />
<img alt="LayeringDocker" src="./dockerimages/8.png" />
<img alt="LayeringDocker" src="./dockerimages/31.png" />

---

## 📦 Volumes and Ports Best Practices

* Mount volumes for persistent data
* Expose only necessary ports
* Avoid hardcoding secrets

<img alt="VolumesAndPorts" src="./dockerimages/9.png" />
<img alt="VolumesAndPorts" src="./dockerimages/12.png" />
<img alt="VolumesAndPorts" src="./dockerimages/10.png" />

---

## 🏗️ Docker Registry & Tagging

Think of Docker registries like Maven repositories.

```bash
docker tag alpine:latest myusername/alpine:custom
docker push myusername/alpine:custom
```

<img alt="DockerRegistry" src="./dockerimages/14.png" />
<img alt="DockerRegistry" src="./dockerimages/15.png" />

---

## 🧩 Docker Orchestration

### 🔸 Manual Orchestration (The Hard Way)

Steps include:

* Creating custom networks
* Manually starting containers
* Managing dependencies using `docker run`
* Logging and health checks via CLI

---

### 🔸 Docker Compose (The Easy Way)

A `docker-compose.yml` file helps automate orchestration.

```yaml
version: '3'
services:
  db:
    image: postgres
    environment:
      POSTGRES_DB: blogdemo
      POSTGRES_PASSWORD: lmnop
    volumes:
      - postgres_data:/var/lib/postgresql/data
  app:
    build: ./blog-backend
    ports:
      - "8080:8080"
    depends_on:
      - db
volumes:
  postgres_data:
```

Run it with:

```bash
docker-compose up
```

<img alt="Orchestration" src="./dockerimages/18.png" />
<img alt="Orchestration" src="./dockerimages/19.png" />
<img alt="Orchestration" src="./dockerimages/20.png" />

---

## 🛡️ Docker Alternatives

* **Podman**: Rootless, Docker-compatible
* **Buildah**: Image builder without a daemon
* **Kubernetes**: Industry-standard container orchestration
* **LXC**: Linux container management

<img alt="DockerAlternatives" src="./dockerimages/25.png" />
<img alt="DockerAlternatives" src="./dockerimages/26.png" />
<img alt="DockerAlternatives" src="./dockerimages/27.png" />
<img alt="DockerAlternatives" src="./dockerimages/28.png" />

---

## 📘 Summary

| Concept        | Analogy                    | Description                    |
| -------------- | -------------------------- | ------------------------------ |
| Image          | Java JAR                   | Blueprint for containers       |
| Container      | Java Object                | Running instance of an image   |
| Dockerfile     | Build Script               | Defines how to build an image  |
| Docker CLI     | Terminal Commands          | Interface to manage Docker     |
| Docker Hub     | Maven Repository           | Stores/publishes images        |
| Docker Compose | Dev tool for orchestration | Manages multi-container setups |

---

## 📚 Learn More

* [LinkedIn Learning - Docker for Java Developers](https://github.com/LinkedInLearning/docker-for-java-developers-2452212/tree/02_05)
* [Official Docker Docs](https://docs.docker.com/)
* [My Docker Cheatsheet Repo](https://github.com/Vishnusimha/Blogs/blob/main/Spring/Docker.md)

---