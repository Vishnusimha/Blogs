# [Docker Overview](https://github.com/Vishnusimha/Blogs/blob/main/Spring/Docker.md)

This document provides an overview of Docker concepts, comparing images to containers, and detailing various Docker commands for managing images, containers, networks, volumes, and services. It also touches upon Docker orchestration with Docker Compose and alternatives to Docker.

**Note:** This document originally contained several images that illustrate the concepts. These images are referenced by local paths (e.g., `./dockerimages/36.png`) and sometimes cannot be displayed directly within this website. To view the original document with all images, please refer to the [Github Link](https://github.com/Vishnusimha/Blogs/blob/main/Spring/Docker.md). The content is also related to a [LinkedIn Learning course](https://github.com/LinkedInLearning/docker-for-java-developers-2452212/tree/02_05) Docker for Java Developers.

---

### Comparing Images to Containers

The text explains three main containerization concepts:

**Image (Container Image/Docker Image)**
An immutable artifact containing:

* Operating system
* Environment settings
* Application
* Run command

Draws a parallel with Java's JAR files:

* Just as Java source code needs to be compiled into JARs
* JARs go to Maven repositories
* Similarly, container images go to registries (local or remote like Docker Hub)

**Container Runtime (Container Engine)**
Platform that runs container images
Comparable to Java Runtime Environment (JRE)
Docker Container Engine is mentioned as an example

**Container (Docker Container/Running Container)**
The actual running instance of an image.
Analogy: Just like an object is an instantiation of a Java class, a container is an instantiation of an image.
This is essentially explaining the basic building blocks of containerization while making it relatable to Java developers by drawing parallels with familiar Java concepts.

---

### Quiz

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

### Intro to Docker

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

### Writing Dockerfiles

<img alt="Writingdockerfiles" src="./dockerimages/36.png" />
<img alt="Writingdockerfiles" src="./dockerimages/21.png" />
---

## Docker CLI

### Layering Docker

<img alt="LayeringDocker" src="./dockerimages/7.png" />
<img alt="LayeringDocker" src="./dockerimages/8.png" />
<img alt="LayeringDocker" src="./dockerimages/31.png" />

Layering Docker is important to get low-memory Docker images.

---

### Layering Docker: Best Practices: Volumes and Ports

<img alt="VolumesAndPorts" src="./dockerimages/9.png" />
<img alt="VolumesAndPorts" src="./dockerimages/12.png" />
<img alt="VolumesAndPorts" src="./dockerimages/10.png" />
---

### Layering Docker: Best Practices: Docker Registry

<img alt="DockerRegistry" src="./dockerimages/14.png" />

Pulling an image from a Docker registry is similar to how Maven downloads libraries by specifying them in Gradle files.
Notice the last line there, docker.io is the URL of the registry, library is the namespace, alpine is the repository, and latest is the tag.
Now, what if I want to share my images I create? All I need to do is retag it and send it to a namespace. You may have noticed on Docker Hub it showed that I was logged into my free account called mellenbowman. I want to make a copy of Alpine latest, and upload it to my namespace. So first I have to tag it. (typing) So I'm going to change the tag of docker.io/library/alpine\:latest and retag it to docker.io/mellenbowman/alpine. And I'm going to give it a tag of me. Beautiful now, I want to push that up to my account.

<img alt="DockerRegistry" src="./dockerimages/15.png" />

```
docker push docker.io/mellenbowman/alpine:me
```
---

### Docker Orchestration: Startup

<img alt="Orchestration" src="./dockerimages/18.png" />
<img alt="Orchestration" src="./dockerimages/19.png" />
<img alt="Orchestration" src="./dockerimages/20.png" />
---

### Container orchestration with Docker Compose

In Docker Hub, there is a trove of images ready for usage, such as one for Postgres, a database. This section explains how containers like web apps interact with other containers, using a Spring Boot web app example that acts as a backend for a message blog.
The example Spring Boot application is a JPA entity with a Blog class (message and auto-generated ID) and a Spring Data repository interface for data access. The Application class also serves as a Spring Data MVC REST controller, exposing endpoints to create and view blog messages (e.g., list all blogs, create a blog, and a "silly" API to stop the application). The application's properties file contains settings for accessing the Postgres database (URLs, passwords, username). A Dockerfile builds this code into `blogdemo.jar`.

---

### Manual Orchestration (The Hard Way):

* **Create a network**: blog-network allows containers within it to refer to each other by name.
* **Create images**: An image for blog-backend and then run the Postgres image to have a Postgres container.

    * The `-e` flag passes environment variables at runtime.
    * A volume is used for Postgres to persist data.
* **Run the blog-backend image as an app**: Named `app` and included in blog-network, allowing it to refer to the Postgres host as `db`. Environment variables for blogdemo database name and lmnop password are set.
* **Monitoring**: Manually using `docker logs` on db or app containers, or curl to `actuator/health`.
* **Tear down**: Manually using `docker stop`, `docker remove`, `docker rmi` (for images), and `docker remove network`.
  This manual process is cumbersome.

---

### Automated Orchestration with Docker Compose

Docker Compose automates orchestration using a `docker-compose.yml` file (default name, YAML syntax).

* **Start**: `docker-compose up` starts all declared containers.
* **Stop**: `docker-compose down` tears them down.
* **Monitoring**: The Docker Compose daemon provides out-of-the-box monitoring.

---

#### Example `docker-compose.yml` breakdown:

**db service (Postgres):**

* image: postgres
* container\_name: db
* Volume setup.
* Environment variables for db name and password.
* Exposes port 5432.
* `restart: always` (monitoring: if the container goes down, Docker Compose restarts it automatically).

**app service (Spring Boot backend):**

* `build: ./blog-backend` (builds from the specified directory).
* Exposes port 8080.
* Environment variables.
* `depends_on: db` (allows `POSTGRES_HOST=db`).
* `restart: always`.
* Declaration of needed volumes.

---

#### Demo of Docker Compose:

The demo shows:

* Running `docker-compose up` to start the services.
* Verifying the app's health with `curl localhost:8080/actuator/health`.
* Listing existing blogs with `curl localhost:8080/blogs`.
* Hitting a "stop" endpoint, which causes the web app to shut down.
* Observing Docker Compose automatically restarting the app due to `restart: always`.
* Verifying data is still accessible after restart.
* Tearing down all services with `docker-compose down`.

---

**Docker Compose** is a simple orchestration tool, primarily used as a time-saver in developer machines.
For production, more popular tools include Kubernetes or Docker Swarm, as well as cloud offerings from Amazon AWS, Google Cloud, and Microsoft Azure.

---

### Docker Alternatives

<img alt="DockerAlternatives" src="./dockerimages/25.png" />
<img alt="DockerAlternatives" src="./dockerimages/26.png" />
<img alt="DockerAlternatives" src="./dockerimages/27.png" />
<img alt="DockerAlternatives" src="./dockerimages/28.png" />
---

### Docker CLI Commands

Here is a list of common Docker commands, categorized by their primary functions such as building images, managing containers, and deploying services:

---

#### Image Management

* **Building Images**
  `docker build [OPTIONS] PATH | URL | -`
  *Example:* `docker build -t myapp:latest .`

* **Tagging Images**
  `docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]`
  *Example:* `docker tag myapp:latest myrepo/myapp:latest`

* **Listing Images**
  `docker images [OPTIONS] [REPOSITORY[:TAG]]`
  *Example:* `docker images`

* **Removing Images**
  `docker rmi [OPTIONS] IMAGE [IMAGE...]`
  *Example:* `docker rmi myapp:latest`

* **Pulling Images**
  `docker pull [OPTIONS] NAME[:TAG|@DIGEST]`
  *Example:* `docker pull ubuntu:latest`

* **Pushing Images**
  `docker push [OPTIONS] NAME[:TAG]`
  *Example:* `docker push myrepo/myapp:latest`

---

#### Container Management

* **Creating Containers**
  `docker create [OPTIONS] IMAGE [COMMAND] [ARG...]`
  *Example:* `docker create --name mycontainer ubuntu`

* **Running Containers**
  `docker run [OPTIONS] IMAGE [COMMAND] [ARG...]`
  *Example:* `docker run -d -p 80:80 myapp:latest`

* **Starting/Stopping/Restarting Containers**
  `docker start`, `docker stop`, `docker restart`
  *Examples:*
  `docker start mycontainer`
  `docker stop mycontainer`
  `docker restart mycontainer`

* **Removing Containers**
  `docker rm [OPTIONS] CONTAINER [CONTAINER...]`
  *Example:* `docker rm mycontainer`

* **Viewing Container Logs**
  `docker logs [OPTIONS] CONTAINER`
  *Example:* `docker logs mycontainer`

* **Listing Containers**
  `docker ps [OPTIONS]`
  *Example:* `docker ps -a`

* **Executing Commands in a Running Container**
  `docker exec [OPTIONS] CONTAINER COMMAND [ARG...]`
  *Example:* `docker exec -it mycontainer bash`

* **Inspecting Containers**
  `docker inspect [OPTIONS] NAME|ID [NAME|ID...]`
  *Example:* `docker inspect mycontainer`

* **Copying Files To/From Containers**
  `docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|-`
  *Example:* `docker cp mycontainer:/path/to/file /local/path`
  `docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH`
  *Example:* `docker cp /local/path mycontainer:/path/to/file`

---

#### Network Management

* **Creating Networks**
  `docker network create [OPTIONS] NETWORK`
  *Example:* `docker network create mynetwork`

* **Listing Networks**
  `docker network ls`
  *Example:* `docker network ls`

* **Removing Networks**
  `docker network rm NETWORK [NETWORK...]`
  *Example:* `docker network rm mynetwork`

* **Connecting a Container to a Network**
  `docker network connect [OPTIONS] NETWORK CONTAINER`
  *Example:* `docker network connect mynetwork mycontainer`

* **Disconnecting a Container from a Network**
  `docker network disconnect [OPTIONS] NETWORK CONTAINER`
  *Example:* `docker network disconnect mynetwork mycontainer`

---

#### Volume Management

* **Creating Volumes**
  `docker volume create [OPTIONS] [VOLUME]`
  *Example:* `docker volume create myvolume`

* **Listing Volumes**
  `docker volume ls`
  *Example:* `docker volume ls`

* **Inspecting Volumes**
  `docker volume inspect VOLUME [VOLUME...]`
  *Example:* `docker inspect myvolume`

* **Removing Volumes**
  `docker volume rm VOLUME [VOLUME...]`
  *Example:* `docker volume rm myvolume`

---

#### Swarm and Service Management

* **Initializing a Swarm**
  `docker swarm init [OPTIONS]`
  *Example:* `docker swarm init`

* **Joining a Swarm**
  `docker swarm join [OPTIONS] [TOKEN] [ADDR...]`
  *Example:* `docker swarm join --token SWMTKN-1-xxx 192.168.1.1:2377`

* **Listing Nodes**
  `docker node ls`
  *Example:* `docker node ls`

* **Deploying a Service**
  `docker service create [OPTIONS] IMAGE [COMMAND] [ARG...]`
  *Example:* `docker service create --name myservice myapp:latest`

* **Listing Services**
  `docker service ls`
  *Example:* `docker service ls`

* **Inspecting Services**
  `docker service inspect [OPTIONS] SERVICE [SERVICE...]`
  *Example:* `docker service inspect myservice`

* **Scaling Services**
  `docker service scale SERVICE=REPLICAS [SERVICE=REPLICAS...]`
  *Example:* `docker service scale myservice=3`

* **Updating Services**
  `docker service update [OPTIONS] SERVICE`
  *Example:* `docker service update --image myapp:latest myservice`

* **Removing Services**
  `docker service rm SERVICE [SERVICE...]`
  *Example:* `docker service rm myservice`

---

#### Compose (Multi-Container Applications)

* **Starting Services**
  `docker-compose up [OPTIONS] [SERVICE...]`
  *Example:* `docker-compose up -d`

* **Stopping Services**
  `docker-compose down [OPTIONS]`
  *Example:* `docker-compose down`

* **Building Services**
  `docker-compose build [OPTIONS] [SERVICE...]`
  *Example:* `docker-compose build`

* **Viewing Logs**
  `docker-compose logs [OPTIONS] [SERVICE...]`
  *Example:* `docker-compose logs`

* **Scaling Services**
  `docker-compose scale SERVICE=NUM [SERVICE=NUM...]`
  *Example:* `docker-compose scale web=3`

* **Listing Containers**
  `docker-compose ps [OPTIONS] [SERVICE...]`
  *Example:* `docker-compose ps`

---

These commands cover most of the basic and advanced operations you might need when working with Docker to build, run, manage, and deploy containerized applications.
