---
title: "Microservices Interview Questions"
date: 2024-10-10
slug: "microservices-interview-questions"
tags: [ "Microservices", "Spring Boot", "Distributed Systems", "APIs", "Containerization" ]
summary: "A comprehensive guide to common microservices interview questions and answers, covering architecture, design patterns, deployment strategies, and best practices."
categories: Microservices
readTime: 18
---

# 1. What is a Microservice?
- A microservice is a **small, independently deployable service** that implements a specific business capability.
- It owns its own codebase, database, and deployment lifecycle.
- Communicates with other services through APIs or messaging.
- Example: Product Service, Order Service, Payment Service.
  **One-line answer:**

> A microservice is an independently deployable service focused on a single business capability with its own data and lifecycle.

---

# 2. What is Monolithic Architecture?
- Entire application is built and deployed as a single unit.
- UI, business logic, and data access layers are packaged together.
- Usually shares a single database.
- Any change requires redeploying the entire application.
  **One-line answer:**

> A monolith is an application where all modules are developed and deployed as a single unit.

---

# 3. Difference Between Monolith and Microservices?
| Monolith | Microservices |
| ----- | ----- |
| Single deployment | Multiple deployments |
| Usually one database | Database per service |
| Tight coupling | Loose coupling |
| Scale whole application | Scale individual services |
| Simpler initially | More complex initially |
| Technology lock-in | Technology flexibility |
**Interview answer:**

> Monolith is a single deployable application, whereas microservices split business capabilities into independently deployable services with separate ownership and scaling.

---

# 4. Why Did Microservices Become Popular?
- Large monoliths became difficult to maintain.
- Faster independent deployments.
- Better scalability.
- Smaller teams can own services independently.
- Cloud and container technologies made deployment easier.
  **Interview answer:**

> Microservices became popular because they improve scalability, deployment speed, team autonomy, and maintainability for large applications.

---

# 5. What Problems Do Microservices Solve?
- Large codebase management issues.
- Slow deployment cycles.
- Scaling entire application unnecessarily.
- Team coordination bottlenecks.
- Technology lock-in.
  **Example:**
  If only Payment receives high traffic:

- Monolith → scale entire application.
- Microservices → scale only Payment Service.
  **Interview answer:**

> Microservices solve scalability, maintainability, deployment, and team ownership challenges found in large monolithic systems.

---

# 6. What Are the Disadvantages of Microservices?
- Distributed system complexity.
- Network latency.
- Service-to-service communication failures.
- Distributed transactions become difficult.
- Monitoring and debugging are harder.
- More infrastructure overhead.
  **Interview answer:**

> Microservices introduce distributed system challenges such as network failures, data consistency issues, observability complexity, and higher operational overhead.

---

# 7. What Are the Characteristics of a Good Microservice?
- Single business responsibility.
- Independently deployable.
- Owns its data.
- Loosely coupled.
- Highly cohesive.
- Fault isolated.
- Scalable independently.
  **Interview answer:**

> A good microservice is focused on one business capability, owns its data, is loosely coupled, highly cohesive, and can be deployed and scaled independently.

---

# 8. What is Loose Coupling?
- Services know as little as possible about each other.
- Internal implementation changes should not affect consumers.
- Interaction happens through contracts/APIs.
### Bad Example
Order Service directly accesses Payment database.

### Good Example
Order Service calls Payment API.

**Interview answer:**

> Loose coupling means services depend only on exposed contracts and not on each other's internal implementation.

---

# 9. What is High Cohesion?
- Related functionality stays together.
- Service should have a single business purpose.
- Avoid mixing unrelated responsibilities.
### Bad Example
User Service:

- Login
- Payments
- Inventory
### Good Example
User Service:

- Registration
- Authentication
- Profile Management
  **Interview answer:**

> High cohesion means all functionality within a service is closely related to a single business responsibility.

---

# 10. What is Service Autonomy?
- Service can operate independently.
- Own deployment pipeline.
- Own database.
- Own scaling strategy.
- Minimal dependency on other teams.
### Example
Payment team deploys Payment Service without waiting for Inventory team.

**Interview answer:**

> Service autonomy means a service can be developed, deployed, scaled, and maintained independently of other services.

A service should have **maximum control over its own logic, data, and deployment** without depending heavily on other services.

---

# 11. What is Domain-Driven Design (DDD)?
- DDD is an approach to designing software around **business domains**.
- Focuses on understanding business problems before designing technical solutions.
- Helps identify microservice boundaries.
- Introduces concepts like:
    - Domain
    - Subdomain
    - Bounded Context
    - Ubiquitous Language

### Example
E-commerce domain:

- Order Management
- Inventory
- Payment
- Shipping
  Each can become a separate microservice.

**Interview answer:**

> DDD is a design approach that models software around business domains and helps identify clear service boundaries using concepts like bounded contexts.

---

# 12. Why Should a Microservice Own Its Database?
- Prevents tight coupling.
- Maintains service autonomy.
- Allows independent schema changes.
- Enables independent deployments.
- Avoids direct data dependencies.
### Good Example
- Order Service → Order DB
- Payment Service → Payment DB
  **Interview answer:**

> A microservice should own its database to ensure autonomy, loose coupling, independent schema evolution, and independent deployments.

---

# 13. Can Multiple Microservices Share the Same Database?
### Technically?
- Yes.
### Recommended?
- No.
### Problems
- Tight coupling.
- One service can break another by schema changes.
- Independent deployment becomes difficult.
- Hard to enforce ownership.
### Example
Order Service and Payment Service using same table.

Payment team changes a column →
Order Service breaks.

**Interview answer:**

> Multiple services can share a database, but it is considered an anti-pattern because it creates tight coupling and reduces service autonomy.

---

# 14. What is Independent Deployment?
- A service can be deployed without deploying other services.
- Teams release changes independently.
- Reduces deployment risk.
- Enables faster delivery.
### Example
Payment Service v2 deployed.

No need to redeploy:

- Order Service
- Product Service
- Inventory Service
  **Interview answer:**

> Independent deployment means a service can be released and updated without requiring deployment of other services.

---

# 15. What is Scalability in Microservices?
- Ability to handle increasing load.
- Add resources when traffic grows.
- Scale only the services that need it.
### Example
During a sale:

Traffic:

- Product Service = High
- Payment Service = Medium
- Notification Service = Low
  Only Product Service is scaled.

**Interview answer:**

> Scalability is the ability of a system to handle increased workload by adding resources or instances as demand grows.

---

# 16. What is Horizontal Scaling?
- Add more instances of a service.
- Load balancer distributes requests.
- Most common scaling method in cloud environments.
### Example
Before:

```
Payment Service → 1 instance
```
After:

```
Payment Service → 5 instances
```
**Interview answer:**

> Horizontal scaling increases capacity by adding more service instances instead of increasing machine size.

---

# 17. What is Vertical Scaling?
- Increase resources of the same machine.
- Add CPU, RAM, Storage.
### Example
Before:

```
4 CPU
8 GB RAM
```
After:

```
16 CPU
64 GB RAM
```
### Limitation
- Hardware limits eventually reached.
- Often requires downtime.
  **Interview answer:**

> Vertical scaling increases the resources of an existing server, such as CPU and memory.

---

# 18. What is Fault Isolation?
- Failure in one service should not crash the entire system.
- Contain failures within service boundaries.
- Improve system resilience.
### Example
Payment Service Down

Still Working:

- Product Service
- Search Service
- Inventory Service
  Only payment functionality affected.

**Interview answer:**

> Fault isolation ensures that a failure in one microservice does not propagate and bring down the entire system.

---

# 19. When Should You NOT Use Microservices?
### Avoid when:
- Small application.
- Small team (2-5 developers).
- Simple business domain.
- Low traffic system.
- No DevOps or cloud expertise.
- Early-stage startup validating an idea.
### Better Choice
Start with a monolith and split later if needed.

**Interview answer:**

> Microservices should be avoided for small, simple applications where the operational complexity outweighs the benefits.

---

# 20. How Do Microservices Communicate?
Two primary ways:

## 1. Synchronous Communication
Request-response model.

Examples:

- REST API
- gRPC
```
Order Service
      |
      v
Payment Service
```
Order waits for response.

### Pros
- Simple
- Immediate response
### Cons
- Tight runtime dependency
---

## 2. Asynchronous Communication
Message/Event based.

Examples:

- Kafka
- RabbitMQ
- AWS SQS
```
Order Service
      |
      v
Kafka Topic
      |
      v
Payment Service
```
Producer doesn't wait.

### Pros
- Loose coupling
- Better scalability
- Better fault tolerance
### Cons
- More complex
- Eventual consistency
  **Interview answer:**

> Microservices communicate either synchronously using REST/gRPC or asynchronously using messaging systems like Kafka or RabbitMQ, depending on consistency and coupling requirements.

---

## Bonus Statement
If an interviewer asks:

**"Which communication style do you prefer?"**

Answer:

> For real-time request-response scenarios, I prefer REST or gRPC. For high scalability, loose coupling, and event-driven workflows, I prefer asynchronous communication using Kafka. Most enterprise systems use a combination of both.



# 21. Difference Between Synchronous and Asynchronous Communication?
| Synchronous | Asynchronous |
| ----- | ----- |
| Request-Response | Event/Message Based |
| Caller waits | Caller doesn't wait |
| Tight runtime dependency | Loose coupling |
| Immediate response | Eventual processing |
| REST/gRPC | Kafka/RabbitMQ |
### Interview Answer
> In synchronous communication, the caller waits for a response. In asynchronous communication, the caller sends a message/event and continues processing without waiting.

---

# 22. What is REST?
- REST = Representational State Transfer.
- Architectural style for building APIs.
- Uses HTTP protocol.
- Resources identified using URLs.
- Stateless communication.
### Example
```text
GET /orders/123
```
### Interview Answer
> REST is an architectural style for building stateless web services using standard HTTP methods and resource-based URLs.

---

# 23. What Are REST Principles?
### 1. Client-Server
Client and server are separate.

### 2. Stateless
Server stores no client session.

### 3. Resource-Based
Everything is treated as a resource.

### 4. Uniform Interface
Standard HTTP methods.

### 5. Cacheable
Responses may be cached.

### Interview Answer
> REST is based on stateless communication, resource-based URLs, client-server separation, uniform interfaces, and cacheability.

---

# 24. What is Idempotency?
- The same request executed multiple times produces the same result.
- Prevents duplicate operations.
### Idempotent
```text
PUT /users/1
```
Updating same data 10 times → same result.

### Non-Idempotent
```text
POST /orders
```
10 requests → 10 orders.

### Interview Answer
> An operation is idempotent if executing it multiple times produces the same outcome as executing it once.

---

# 25. Difference Between PUT and POST?
| PUT | POST |
| ----- | ----- |
| Update/Create | Create |
| Idempotent | Non-idempotent |
| Client knows resource ID | Server generates ID |
| Same request = same result | Same request = multiple resources |
### Example
PUT

```text
PUT /users/101
```
POST

```text
POST /users
```
### Interview Answer
> PUT is idempotent and typically updates a known resource, while POST is used to create resources and is generally non-idempotent.

---

# 26. What is gRPC (Remote Procedure Calls)?
- High-performance communication protocol.
- Developed by Google.
- Uses HTTP/2.
- Uses Protocol Buffers instead of JSON.
- Faster than REST.
### Interview Answer
> gRPC is a high-performance RPC framework that uses HTTP/2 and Protocol Buffers for efficient service-to-service communication.

---

# 27. REST vs gRPC?
| REST | gRPC |
| ----- | ----- |
| JSON | Protocol Buffers |
| HTTP/1.1 | HTTP/2 |
| Human-readable | Binary |
| Easy for external APIs | Better for internal services |
| Slower | Faster |
### Interview Answer
> REST is simple and widely adopted, while gRPC offers better performance, smaller payloads, and efficient internal service communication.

---

# 28. What is Message-Driven Architecture?
- Services communicate using messages.
- Producer sends message.
- Consumer processes message.
- Usually uses a broker.
### Example
```text
Order Service
      |
      v
Message Queue
      |
      v
Payment Service
```
### Interview Answer
> Message-driven architecture enables services to communicate through messages instead of direct API calls, improving decoupling and reliability.

---

# 29. What is Event-Driven Architecture?
- Services publish events.
- Other services subscribe to events.
- Producer doesn't know consumers.
### Example
```text
OrderCreated Event
```
Consumed by:

- Payment Service
- Inventory Service
- Notification Service
### Interview Answer
> Event-driven architecture allows services to react to business events asynchronously through publishers and subscribers.



| Aspect | Event-Driven Architecture (EDA) | **Message-Driven Architecture (MDA)** | Domain-Driven Design (DDD) |
| ----- | ----- | ----- | ----- |
| **What is it?** | An architectural style where **events **trigger actions. | <p>An architectural style where </p><p>**messages**</p><p> are exchanged between systems/services.</p> | <p>A software design approach focused on modeling the </p><p>**business domain**.</p> |
| **Focus** | Events | Messages | Business logic |
| **Purpose** | React to something that happened. | Enable communication between services. | Organize code around business concepts. |
| **Communication** | Event publishing and subscribing. | Sending and receiving messages via queues/topics. | Not about communication; it's about code and design. |
| **Typical Technologies** | Apache Kafka, Apache Pulsar | RabbitMQ, ActiveMQ, Kafka | Spring Boot, Java, aggregates, repositories, entities (DDD concepts) |
| **Data Flow** | "Something happened." | "Please do this." | Business rules and domain models. |
| **Coupling** | Loosely coupled | Loosely coupled | Organizes business logic for maintainability |
| **Main Goal** | Notify interested services about events. | Reliably deliver work between services. | Build software that matches the business domain. |
---

# 30. What is a Message Broker?
- Middleware that transfers messages between services.
- Stores and routes messages.
- Improves reliability.
### Examples
- Apache Kafka
- RabbitMQ
- ActiveMQ
### Interview Answer
> A message broker acts as an intermediary that stores, routes, and delivers messages between distributed services.

---

# 31. Why Use Asynchronous Communication?
- Loose coupling.
- Better scalability.
- Better fault tolerance.
- Reduced waiting time.
- Improved system resilience.
### Interview Answer
> Asynchronous communication improves scalability and resilience by allowing services to operate independently without waiting for immediate responses.

---

# 32. What Happens if Service B is Down When Service A Calls It?
### Without Protection
```text
A --> B
```
B Down → A fails.

### Risks
- Timeout
- Cascading failure
- User-facing errors
### Solutions
- Retry
- Circuit Breaker
- Fallback
- Queue/Event
### Interview Answer
> If Service B is unavailable, Service A may experience timeouts or failures unless resilience mechanisms like retries, circuit breakers, or fallbacks are implemented.

---

# 33. What is Request Chaining?
Multiple services calling each other in sequence.

```text
API Gateway
      |
      v
Order
      |
      v
Payment
      |
      v
Inventory
      |
      v
Notification
```
### Interview Answer
> Request chaining occurs when one service call triggers additional service calls in a sequence to complete a request.

---

# 34. Why is Request Chaining Dangerous?
### Problems
- Increased latency.
- More network calls.
- Higher failure probability.
- Difficult debugging.
- Cascading failures.
### Example
If Inventory fails:

```text
Gateway -> Order -> Payment -> Inventory
```
Entire request may fail.

### Interview Answer
> Request chaining increases latency and failure risk because every additional service dependency becomes a potential point of failure.



The key idea is:

> **Don't keep the user's request waiting while every downstream service completes its work.** Instead, do only the work that's immediately required, and let the rest happen asynchronously where possible.

## The bad design (Request Chaining)
```
Client
   |
Gateway
   |
Order Service
   |
Payment Service
   |
Inventory Service
   |
Notification Service
```
Problems:

- The client waits for every service.
- If one service is slow, the whole request is slow.
- If one service fails, the entire request may fail.
- One outage can cascade through the system.
---

## A better design: Event-Driven Architecture
```
Client
   |
Gateway
   |
Order Service
   |
   | Save Order
   |
Publish "OrderCreated"
        |
-------------------------------
|             |               |
Payment    Inventory     Notification
Service     Service        Service
```
Now:

1. Order Service saves the order.
2. It publishes an `OrderCreated`  event.
3. Payment, Inventory, and Notification process it independently.
   Advantages:

- No long synchronous chain.
- Services are loosely coupled.
- Each service can retry independently if it fails.
- Faster response to the client.
---

## But what if payment must happen before confirming the order?
Sometimes synchronous communication is necessary.

A good approach is to keep the synchronous path as short as possible.

```
Client
   |
Order Service
   |
Payment Service
```
Once payment succeeds:

```
Payment Success
      |
Publish PaymentCompleted
      |
-------------------------
|                       |
Inventory          Notification
Service             Service
```
The client doesn't wait for inventory updates or emails.

---

## Another technique: The Saga Pattern
In distributed systems, you can't wrap multiple microservices in a single database transaction.

Instead:

```
Create Order
      |
Charge Payment
      |
Reserve Inventory
      |
Ship Order
```
If inventory reservation fails:

```
Create Order ✓
Charge Payment ✓
Reserve Inventory ✗
↓
Refund Payment
Cancel Order
```
Instead of rolling back a database transaction, each completed step has a **compensating action**.

---

## Use retries instead of blocking
Suppose Inventory is temporarily down.

Instead of failing immediately:

```
OrderCreated Event
      |
Inventory Service
      |
Retry
Retry
Retry
Success
```
The client already received a response, and the system recovers automatically.

---

## Use a message broker
Instead of:

```
Order -> Payment -> Inventory -> Email
```
Use:

```
Order
   |
Kafka/RabbitMQ
   |
-------------------------
|          |            |
Payment  Inventory   Email
```
If Email is down:

- Payment still succeeds.
- Inventory still updates.
- The email can be retried later.
  One service failing doesn't block the others.

---

## Interview answer
If asked, "How would you avoid request chaining?", a strong answer is:

> "I keep synchronous calls only for operations that must complete before responding to the client. For non-critical or independent work, I publish events through a message broker so downstream services process them asynchronously. This reduces latency, prevents cascading failures, improves scalability, and allows each service to retry independently if needed."

### Rule of thumb
| If the operation... | Preferred approach |
| ----- | ----- |
| Must finish before the user gets a response (e.g., authentication, payment authorization) | Synchronous API call |
| Can happen after the response (e.g., email, analytics, notifications, cache updates) | Asynchronous event/message |
| Needs multiple services to complete a business process | Use a Saga Pattern rather than a long synchronous request chain |
The goal isn't to eliminate request chaining entirely—it's to **minimize synchronous dependencies** and use asynchronous communication wherever the business requirements allow.

---

# 35. What is an API Gateway?
- Single entry point for clients.
- Routes requests to services.
- Hides internal architecture.
- Centralized cross-cutting concerns.
### Example
```text
Client
   |
   v
API Gateway
   |
   +--> Order Service
   +--> Product Service
   +--> Payment Service
```
### Interview Answer
> An API Gateway acts as a centralized entry point that routes client requests to appropriate microservices.

---

# 36. Why Not Expose All Services Directly?
### Problems
- Security risk.
- Too many endpoints.
- Client complexity.
- Service locations exposed.
- Difficult version management.
### Interview Answer
> Direct exposure increases security and maintenance challenges, while an API Gateway provides a controlled and unified access layer.

---

# 37. What Responsibilities Belong in an API Gateway?
### Common Responsibilities
- Routing
- Authentication
- Authorization
- SSL termination
- Rate limiting
- Logging
- Request aggregation
### Interview Answer
> API Gateway responsibilities include routing, authentication, authorization, rate limiting, logging, and request aggregation.

---

# 38. What Should NOT Be Implemented in an API Gateway?
### Avoid
- Business logic
- Database access
- Complex workflows
- Domain validation
### Bad Example
```text
Gateway calculates discount
Gateway creates orders
```
### Interview Answer
> The API Gateway should not contain business logic or data access because those responsibilities belong to the microservices.



---

# 39. Difference Between API Gateway and Load Balancer?
| API Gateway | Load Balancer |
| ----- | ----- |
| Application Layer | Network/Application Layer |
| Routes by API path | Routes by server |
| Authentication | Usually no authentication |
| Rate Limiting | No rate limiting |
| Aggregation | No aggregation |
### Interview Answer
> A load balancer distributes traffic across instances, whereas an API Gateway provides routing, security, and API management capabilities.

---

# 40. What is Service Discovery?
When Service A wants to call Service B, it needs to know:

-  Where is Service B running?
-  What is its IP and port?


Since microservice instances are created and destroyed dynamically (especially in Kubernetes/EKS), hardcoding IPs is not practical. This is where **service discovery** comes in.

- Mechanism for locating service instances dynamically.
- Required because service IPs change frequently.
- Common in cloud and Kubernetes environments.
### Example
Instead of:

```text
http://10.10.1.25:8080
```
Call:

```text
http://payment-service
```
Discovery system resolves actual instances.

### Examples
- Netflix Eureka
- Consul
- Kubernetes Service Discovery
### Interview Answer
> Service discovery enables services to dynamically find and communicate with other service instances without hardcoding network locations.



# 41. Why Do Microservices Need Service Discovery?
- Service instances are dynamic.
- IP addresses change frequently.
- Containers and Pods are recreated.
- Avoid hardcoded URLs.
### Interview Answer
> Service discovery enables services to dynamically locate other services without hardcoding network addresses.

---

# 42. What is Client-Side Discovery?
In client-side discovery, **the client itself asks the service registry for service locations and chooses an instance to call.**

### 
- In client-side discovery, the client itself asks the service registry for service locations and chooses an instance to call.Client asks discovery server for service location.
- Client chooses an instance.
- Client calls the service directly.
  What happens:

1.  Payment Service registers with Eureka.
2.  Order Service asks Eureka for PAYMENT-SERVICE instances.
3.  Order Service selects one instance.
4.  Request is sent directly.
### Example
```text
Order Service
      |
      v
Eureka
      |
      v
Payment Service Instance
```
### Interview Answer
> In client-side discovery, the client queries the service registry and selects the service instance itself.

---

# 43. What is Server-Side Discovery?
In server-side discovery, **the client does not know about the registry.**

A load balancer, API gateway, or platform component performs discovery.

- Client calls a load balancer.
- Load balancer queries service registry.
- Load balancer forwards request.
### Example 1
```text
Client
   |
   v
Load Balancer
   |
   v
Payment Service
```
### Example 2
```
Order Service
      |
      v
payment-service.default.svc.cluster.local
      |
      v
Kubernetes Service
      |
      v
Payment Pod 1
Payment Pod 2
Payment Pod 3
```
The Order Service simply calls:

```
http://payment-service
```
Kubernetes handles:

-  Service discovery
-  Load balancing
-  Pod selection
   The client doesn't care which pod receives the request.

### Interview Answer
> In server-side discovery, a load balancer or gateway resolves service locations and routes requests on behalf of the client.



Comparision

| Feature | Client-Side Discovery | Server-Side Discovery |
| ----- | ----- | ----- |
| Who finds service instances? | Client | Load Balancer/Gateway/Platform |
| Registry awareness | Client knows registry | Client doesn't know registry |
| Load balancing | Client | Server/Gateway |
| Client complexity | Higher | Lower |
| Infrastructure complexity | Lower | Higher |
| Common with | Eureka + Ribbon | Kubernetes, Service Mesh, API Gateway |
---

# 44. Do We Still Need Eureka in Kubernetes/EKS?
### Usually No.
Because Kubernetes already provides:

- Built-in Service Discovery
- DNS Resolution
- Load Balancing
- Health Checks
### Example
```text
http://payment-service
```
Kubernetes resolves the endpoint automatically.

### Interview Answer
> In Kubernetes/EKS environments, Eureka is generally unnecessary because Kubernetes already provides native service discovery and load balancing.

---

# 45. What is Spring Cloud?
- Collection of tools for distributed systems.
- Extends Spring Boot.
- Helps build cloud-native microservices.
### Features
- Config Server
- Gateway
- OpenFeign
- Circuit Breaker
- Service Discovery
### Interview Answer
> Spring Cloud provides infrastructure components that simplify the development of distributed microservice architectures.

---

# 46. What is Spring Cloud Config?
Spring Cloud Config is a centralized configuration management solution for microservices. It stores application configuration in a central repository, usually Git, and exposes it through a Config Server. Microservices act as Config Clients and fetch their configuration at runtime, enabling centralized management, version control, environment-specific settings, and easier configuration updates.

- Externalized configuration management.
- Central location for application properties.
- Configurations stored outside applications.
### Example
```text
application.yml
```
: Stored in Git repository.

### Interview Answer
> Spring Cloud Config provides centralized management of application configuration across multiple services.

---

# 47. What is Centralized Configuration?
Instead of:

```text
Order Service -> config
Payment Service -> config
Inventory Service -> config
```
Use:

```text
Config Server
|
+--> Order
+--> Payment
+--> Inventory
```
### Benefits
- Single source of truth.
- Easier maintenance.
- Consistent configuration.
### Interview Answer
> Centralized configuration stores application settings in a shared location that multiple services can access.

---

# 48. What is Config Server?
- Central server that provides configuration.
- Typically backed by Git.
- Supplies configuration to microservices at startup.
### Interview Answer
> Config Server is a centralized service that serves configuration properties to client applications.

---

# 49. What Happens if Config Server is Unavailable?
### Possible Outcomes
- Service startup may fail.
- Existing running services continue working.
- Cached configuration may be used.
### Common Solutions
- Fail-fast disabled.
- Local fallback config.
- Config caching.
### Interview Answer
> If Config Server is unavailable, new services may fail to start, while already running services continue using their loaded configuration.

---

# 50. What is Spring Cloud Gateway?
- API Gateway implementation from Spring.
- Replaces Netflix Zuul.
- Reactive and non-blocking.
### Responsibilities
- Routing
- Authentication
- Rate Limiting
- Filters
### Interview Answer
> Spring Cloud Gateway is a reactive API Gateway that provides routing, filtering, and security capabilities for microservices.

---

# 51. What is OpenFeign?
## What is OpenFeign?
**OpenFeign** is a **declarative HTTP client** used in Spring Boot microservices to call other services via REST APIs.

Instead of writing code using `RestTemplate` or `WebClient`, you simply define a Java interface and OpenFeign generates the implementation automatically.

---

## Problem Without OpenFeign
Suppose Order Service needs to call Payment Service.

Using `RestTemplate`:

```java
@RestController
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order")
    public String createOrder() {
        return restTemplate.getForObject(
            "http://payment-service/payment/1",
            String.class
        );
    }
}
```
You must handle:

- URL construction
- Request execution
- Response mapping
---

## Solution: OpenFeign
Define an interface:

```java
@FeignClient(name = "payment-service")
public interface PaymentClient {

    @GetMapping("/payment/{id}")
    String getPayment(@PathVariable Long id);
}
```
Use it:

```java
@RestController
public class OrderController {

    @Autowired
    private PaymentClient paymentClient;

    @GetMapping("/order")
    public String createOrder() {
        return paymentClient.getPayment(1L);
    }
}
```
OpenFeign creates the implementation at runtime.

---

## How OpenFeign Works
```text
Order Service
      |
      v
Feign Client Interface
      |
      v
Generated HTTP Request
      |
      v
Payment Service
```
The developer writes only the interface.

---

## Interview Answer (Short)
> **OpenFeign is a declarative HTTP client provided by Spring Cloud. It allows microservices to communicate through REST APIs by defining Java interfaces annotated with** `**@FeignClient**`**. OpenFeign generates the HTTP client implementation automatically, reducing boilerplate code and integrating easily with service discovery, load balancing, and fault-tolerance mechanisms.**

---

# 52. Feign Client vs RestTemplate vs WebClient?
| Feature | RestTemplate | Feign | WebClient |
| ----- | ----- | ----- | ----- |
| Style | Imperative | Declarative | Reactive |
| Status | Legacy | Active | Guided |
| Async Support | No | Limited | Yes |
| Boilerplate | High | Low | Medium |
### Interview Answer
> Feign simplifies REST calls, RestTemplate is legacy synchronous communication, and WebClient is the modern non-blocking HTTP client.

---

# 53. Why is WebClient Preferred in Modern Applications?
### Advantages
- Non-blocking.
- Reactive.
- Better scalability.
- Efficient resource utilization.
- Supports streaming.
### Interview Answer
> WebClient is preferred because it supports non-blocking I/O, enabling higher scalability and better resource utilization.

---

# 54. What is Fault Tolerance?
- Ability to continue operating despite failures.
- Prevents complete system outages.
- Improves reliability.
### Interview Answer
> Fault tolerance is the ability of a system to continue functioning even when some components fail.

---

# 55. What is a Circuit Breaker?
- Prevents repeated calls to failing services.
- Stops unnecessary load.
- Improves resilience.
### Interview Answer
> A circuit breaker detects repeated failures and temporarily blocks requests to a failing service.

---

# 56. Why Do We Need a Circuit Breaker?
Without a Circuit Breaker:

```text
A ---> B (fails)
A ---> B (fails)
A ---> B (fails)
```
Resources get devoured.

### Interview Answer
> Circuit breakers prevent cascading failures and resource exhaustion caused by repeatedly calling unavailable services.

---

# 57. What are Closed, Open, and Half-Open States?
This refers to the **Circuit Breaker Pattern**, commonly implemented using Resilience4j in Spring Boot microservices.

This question refers to the **Circuit Breaker Pattern**, commonly implemented using Resilience4j in Spring Boot microservices.

## Why Do We Need a Circuit Breaker?
Imagine:

```text
Order Service
      |
      v
Payment Service
```
If Payment Service becomes slow or unavailable, Order Service may keep sending requests and eventually exhaust its threads/resources.

A Circuit Breaker prevents this by temporarily stopping calls to the failing service.

---

## 1. Closed State (Normal Operation)
This is the **default state**.

```text
Order Service
      |
      v
Payment Service
```
- Requests are allowed.
- Circuit Breaker monitors success/failure rates.
- Everything works normally.
### Example
100 requests sent:

```text
Success = 98
Failure = 2
```
Failure rate is low, so the circuit remains **Closed**.

---

## 2. Open State (Service is Considered Unhealthy)
When failures exceed a configured threshold, the circuit breaker **opens**.

```text
Order Service
      |
      X
Circuit Open
      |
      X
Payment Service
```
- No requests are sent to Payment Service.
- Calls fail immediately.
- Fallback response is returned.
### Example
100 requests:

```text
Success = 20
Failure = 80
```
Failure rate = 80%

Configured threshold:

```text
50%
```
Since 80% > 50%, the circuit transitions to **Open**.

### Benefit
Instead of waiting for timeouts:

```text
Request
|
X Immediate Failure
```
the system responds quickly and protects resources.

---

## 3. Half-Open State (Testing Recovery)
After a configured wait time, the circuit breaker enters **Half-Open**.

```text
Order Service
      |
      v
Few Test Requests
      |
      v
Payment Service
```
- Only a limited number of requests are allowed.
- The goal is to check whether the service has recovered.
### Scenario A: Recovery Successful
```text
Test Request 1 -> Success
Test Request 2 -> Success
Test Request 3 -> Success
```
Circuit transitions:

```text
Half-Open -> Closed
```
Normal traffic resumes.

---

### Scenario B: Service Still Failing
```text
Test Request 1 -> Failure
```
Circuit transitions:

```text
Half-Open -> Open
```
Requests are blocked again.

---

## State Transition Diagram
```text
Failures exceed threshold
Closed  ----------------------> Open
  ^                               |
  |                               |
  |                               |
  | Successful test requests      | Wait duration elapsed
  |                               |
  +--------- Half-Open <----------+
                |
                |
                +---- Failure ----> Open
```
---

## Resilience4j Example
```yaml
resilience4j:
circuitbreaker:
  instances:
    paymentService:
      failureRateThreshold: 50
      waitDurationInOpenState: 30s
      permittedNumberOfCallsInHalfOpenState: 5
```
Meaning:

- Open circuit if failure rate exceeds 50%.
- Stay Open for 30 seconds.
- Allow 5 test requests in Half-Open state.
- Close again if those requests succeed.
---

## Real-World Example
Suppose:

1. Payment Service crashes.
2. Circuit Breaker detects many failures.
3. State changes:
```text
Closed -> Open
```
4. Order Service immediately returns:
```text
"Payment service temporarily unavailable"
```
5. After 30 seconds:
```text
Open -> Half-Open
```
6. A few test requests are sent.
   If successful:

```text
Half-Open -> Closed
```
and normal operation resumes.

---

## Interview Answer (Short)
> A Circuit Breaker has three main states:

> - **Closed**: Requests flow normally and failures are monitored.
- **Open**: Failure rate exceeds a threshold, so requests are blocked and fallbacks are returned.
- **Half-Open**: After a wait period, a small number of test requests are allowed to verify whether the downstream service has recovered. If successful, the circuit closes; otherwise, it reopens.


---

# 58. What is Timeout Handling?
- Stop waiting after a defined period.
- Prevent thread exhaustion.
### Example
```text
Timeout = 2 seconds
```
If service doesn't respond:

```text
Request fails
```
### Interview Answer
> Timeout handling limits how long a service waits for a response before aborting the request.

---

# 59. What is Retry?
- Automatic reattempt of failed requests.
- Useful for transient failures.
### Example
```text
Attempt 1 -> Fail
Attempt 2 -> Success
```
### Interview Answer
> Retry automatically re-executes failed operations to recover from temporary failures.

---

# 60. Why Can Retries Be Dangerous?
### Risks
- Increased traffic.
- Retry storms.
- Resource exhaustion.
- Duplicate processing.
### Interview Answer
> Excessive retries can amplify failures by generating additional load on already struggling systems.

---

# 61. What is Bulkhead Pattern?
The **Bulkhead Pattern** is a resilience pattern that **isolates resources** (threads, connection pools, etc.) so that a failure in one part of the system does not bring down the entire application.

The name comes from ships, where **bulkheads** are watertight compartments. If one compartment floods, the whole ship doesn't sink.



It allocates separate resources for different operations.

```
Payment Calls      --> Thread Pool A
Inventory Calls    --> Thread Pool B
Notification Calls --> Thread Pool C
```
If Payment Service becomes slow:

```
Thread Pool A = Full
```
Inventory and Notification continue working because they use different pools.



The Bulkhead Pattern isolates resources such as threads or connection pools so that a failure or slowdown in one component does not affect the rest of the system. It improves resilience by preventing resource exhaustion from spreading across the application. In Spring Boot microservices, Resilience4j provides Semaphore Bulkhead and Thread Pool Bulkhead implementations.

Circuit Breaker stops calls to a failing service, whereas Bulkhead prevents a failing service from consuming all application resources.

### Interview Answer
> Bulkhead pattern isolates resources so failures in one component do not impact others.

---

# 62. What is a Fallback Mechanism?
- Alternative response during failures.
- Maintains user experience.
### Example
```text
Price Service Down
```
Return:

```text
Price unavailable
```
Instead of crashing.

### Interview Answer
> A fallback provides an alternative response when a primary operation fails.

---

# 63. How Does Resilience4j Work?
Provides:

- Circuit Breaker
- Retry
- Timeout
- Rate Limiter
- Bulkhead
### Interview Answer
> Resilience4j is a lightweight fault-tolerance library that implements patterns such as circuit breakers, retries, bulkheads, and rate limiting.

---

# 64. Why Does Each Microservice Own Its Database?
- Service autonomy.
- Loose coupling.
- Independent deployments.
- Independent schema evolution.
### Interview Answer
> Database ownership ensures service autonomy and prevents tight coupling between services.

---

# 65. Database Per Service Pattern?
Every service owns its private database.

```text
Order Service --> Order DB
Payment Service --> Payment DB
Inventory Service --> Inventory DB
```
### Interview Answer
> Database per service is a pattern where each microservice exclusively owns and manages its data store.

---

# 66. What is Eventual Consistency?
- Data becomes consistent over time.
- Not immediately synchronized.
### Example
Order Created:

```text
Order DB updated immediately
Inventory updated later
```
### Interview Answer
> Eventual consistency means all systems will become consistent over time rather than immediately after a transaction.

---

# 67. What is Distributed Transaction?
- Transaction spanning multiple services/databases.
### Example
```text
Order DB
Payment DB
Inventory DB
```
All must succeed.

### Interview Answer
> A distributed transaction is a transaction that involves multiple independent services or databases.

---

# 68. Why Are Distributed Transactions Difficult?
### Challenges
- Network failures.
- Partial failures.
- Data consistency.
- High latency.
- Scalability concerns.
### Interview Answer
> Distributed transactions are difficult because coordinating multiple independent systems reliably is complex and failure-prone.

---

# 69. What is the Saga Pattern?
- Alternative to distributed transactions.
- Sequence of local transactions.
- Each step has a compensating action.
### Example
```text
Create Order
Reserve Inventory
Process Payment
```
Payment fails:

```text
Release Inventory
Cancel Order
```
### Interview Answer
> Saga Pattern manages distributed transactions through a sequence of local transactions and compensating actions.

---

# 70. Choreography Saga vs Orchestration Saga?
| Choreography | Orchestration |
| ----- | ----- |
| Event Driven | Central Coordinator |
| No Controller | Saga Orchestrator |
| More Decentralized | More Controlled |
| Harder to Debug | Easier to Track |
### Interview Answer
> Choreography uses events for coordination, while orchestration uses a central coordinator to manage the transaction flow.

---

# 71. What is CQRS?
CQRS = Command Query Responsibility Segregation

### Principle
Separate:

```text
Writes (Commands)
```
from

```text
Reads (Queries)
```
### Interview Answer
> CQRS separates read and write operations into different models to optimize performance and scalability.

---

# 72. What is Event Sourcing?
**Event Sourcing** is a design pattern where you **store every change to an application's state as a sequence of events**, rather than storing only the current state.

Instead of saving:

```
Account Balance = ₹10,000
```
you save all the events that led to that balance.

```
Account Created 
Money Deposited ₹5,000
Money Deposited ₹8,000
Money Withdrawn ₹3,000
```
The current balance is calculated by replaying these events.

Current state reconstructed from events.

### Interview Answer
> Event Sourcing stores all state changes as immutable events rather than storing only the current state.

---

# 73. Difference Between ACID and BASE?
| ACID | BASE |
| ----- | ----- |
| Strong Consistency | Eventual Consistency |
| Immediate Consistency | Delayed Consistency |
| Traditional RDBMS | Distributed Systems |
| Transaction Focused | Availability Focused |
### ACID
- Atomicity
- Consistency
- Isolation
- Durability
### BASE
- Basically Available
- Soft State
- Eventual Consistency
### Interview Answer
> ACID prioritizes strong consistency and transactional integrity, while BASE prioritizes availability and scalability with eventual consistency.



# 74. What is a Distributed System?
- Multiple independent machines work together as one system.
- Components communicate over a network.
- Users see it as a single application.
- Microservices architecture is a distributed system.
### Example
```text
Order Service  --> Server 1
Payment Service --> Server 2
Inventory Service --> Server 3
```
### Interview Answer
> A distributed system is a collection of independent computers that collaborate over a network to appear as a single system to users.

---

# 75. What Are the Challenges of Distributed Systems?
### Major Challenges
- Network failures
- Latency
- Partial failures
- Data consistency
- Distributed transactions
- Service discovery
- Monitoring and tracing
- Clock synchronization
- Scalability
### Interview Answer
> Distributed systems introduce challenges such as network failures, latency, data consistency, observability, and coordination between multiple services.

---

# 76. What is Network Latency?
- Time taken for data to travel between systems.
- Every remote call has a cost.
- Usually measured in milliseconds.
### Example
```text
Order Service ---> Payment Service
Response Time = 50ms
```
That 50ms is latency.

### Interview Answer
> Network latency is the delay between sending a request and receiving a response across a network.

---

# 77. What is Partial Failure?
- Some components fail while others continue working.
- Unique to distributed systems.
### Example
```text
Product Service     -> Working
Inventory Service  -> Working
Payment Service    -> Down
```
System is partially failed.

### Interview Answer
> Partial failure occurs when some components of a distributed system fail while others continue operating normally.

---

# 78. What is the CAP Theorem?
The CAP theorem states that during a network partition, a distributed system can guarantee only **two of the following three properties**:

CAP

### C = Consistency
### A = Availability
### P = Partition Tolerance
### Interview Answer
> CAP theorem states that in the presence of a network partition, a distributed system can guarantee either consistency or availability, but not both simultaneously.

---

# 79. Explain Consistency in CAP
### Meaning
All nodes see the same data at the same time.

### Example
User updates balance:

```text
Balance = 1000
```
Immediately after update:

```text
Node A = 1000
Node B = 1000
Node C = 1000
```
No stale data.

### Interview Answer
> Consistency means every read receives the most recent write or an error, ensuring all nodes return the same data.

---

# 80. Explain Availability in CAP
### Meaning
Every request receives a response.

### Important
Response may not contain the latest data.

### Example
```text
Node A = 1000
Node B = 900
```
Even if Node B has old data, it still responds.

### Interview Answer
> Availability means every request receives a response, even if the response may not contain the latest data.

---

# 81. Explain Partition Tolerance in CAP
### Meaning
System continues operating despite network failures.

### Example
Network breaks between:

```text
Mumbai Data Center
       X
Delhi Data Center
```
Application still works.

### Reality
Network failures are unavoidable.

Therefore modern distributed systems must support partition tolerance.

### Interview Answer
> Partition tolerance means the system continues functioning despite communication failures between nodes.

---

# 82. Why Can't All Three CAP Properties Be Fully Achieved Simultaneously?
### Scenario
Suppose:

```text
Node A
   X
Node B
```
Network partition occurs.

Now two choices exist:

### Choice 1: Consistency
Node B refuses requests until synchronization.

Result:

```text
Consistency = Yes
Availability = No
```
---

### Choice 2: Availability
Node B continues serving requests.

Result:

```text
Availability = Yes
Consistency = No
```
Because nodes may return different data.

### Key Point
When a partition happens, you must sacrifice either:

- Consistency (AP system)
  OR
- Availability (CP system)
### Interview Answer
> During a network partition, a system must choose between serving potentially stale data (availability) or rejecting requests until consistency is restored, making it impossible to guarantee all three properties simultaneously.

---

# 83. Give Real-World Examples of CP and AP Systems
## CP Systems (Consistency + Partition Tolerance)
Prioritize correct data.

### Examples
- Apache ZooKeeper
- etcd
- Consul
### Behavior
If a node cannot guarantee latest data:

```text
Request Rejected
```
---

## AP Systems (Availability + Partition Tolerance)
Prioritize uptime.

### Examples
- Apache Cassandra
- Amazon DynamoDB
- Riak
### Behavior
Even during partition:

```text
Request Accepted
```
Data may synchronize later.

---

### Interview Answer
> CP systems such as etcd prioritize consistency and may reject requests during partitions, while AP systems such as Apache Cassandra prioritize availability and allow eventual consistency.



# 85.  How do you deploy microservices without downtime?
-  Use **Rolling Deployment** to replace instances gradually.
-  Keep old instances serving traffic while new instances start.
-  Use **Readiness Probes** so traffic goes only to healthy instances.
-  Use **Graceful Shutdown** to complete in-flight requests before stopping instances.
-  Ensure **backward-compatible API changes**.
-  Follow **expand-and-contract database migration** strategy.
-  Use **Blue-Green Deployment** for instant rollback.
-  Use **Canary Deployment** for gradual traffic shifting.
-  Use **Load Balancers** to route traffic only to healthy instances.
### Interview Answer
>  Zero-downtime deployment is achieved using rolling deployments, health checks, graceful shutdown, load balancing, backward-compatible changes, and deployment strategies such as blue-green or canary releases.

---

### Senior-Level Follow-up
**Q85: Which deployment strategy is most commonly used in Kubernetes?**

>  Rolling Deployment because Kubernetes supports it natively and it minimizes downtime while updating pods gradually.





































1.  What is monolithic architecture?
2.  Difference between monolith and microservices?
3.  Why did microservices become popular?
4.  What problems do microservices solve?
5.  What are the disadvantages of microservices?
6.  What are the characteristics of a good microservice?
7.  What is loose coupling?
8.  What is high cohesion?
9.  What is service autonomy?
10.  What is bounded context?
11.  What is domain-driven design (DDD)?
12.  Why should a microservice own its database?
13.  Can multiple microservices share the same database?
14.  What is independent deployment?
15.  What is scalability in microservices?
16.  What is horizontal scaling?
17.  What is vertical scaling?
18.  What is fault isolation?
19.  When should you NOT use microservices?
20. How do microservices communicate?
21.  Difference between synchronous and asynchronous communication?
22.  What is REST?
23.  What are REST principles?
24.  What is idempotency?
25.  Difference between PUT and POST?
26.  What is gRPC?
27.  REST vs gRPC?
28.  What is message-driven architecture?
29.  What is event-driven architecture?
30.  What is a message broker?
31.  Why use asynchronous communication?
32.  What happens if Service B is down when Service A calls it?
33.  What is request chaining?
34.  Why is request chaining dangerous?
35. What is an API Gateway?
36.  Why not expose all services directly?
37.  What responsibilities belong in an API Gateway?
38.  What should NOT be implemented in an API Gateway?
39.  Difference between API Gateway and Load Balancer?
40. What is service discovery?
41.  Why do microservices need service discovery?
42.  What is client-side discovery?
43.  What is server-side discovery?
44.  Do we still need Eureka in Kubernetes/EKS?
45. What is Spring Cloud?
46.  What is Spring Cloud Config?
47.  What is centralized configuration?
48.  What is Config Server?
49.  What happens if Config Server is unavailable?
50.  What is Spring Cloud Gateway?
51.  What is OpenFeign?
52.  Feign Client vs RestTemplate vs WebClient?
53.  Why is WebClient preferred in modern applications?
54. What is fault tolerance?
55.  What is a circuit breaker?
56.  Why do we need a circuit breaker?
57.  What are Closed, Open, and Half-Open states?
58.  What is timeout handling?
59.  What is retry?
60.  Why can retries be dangerous?
61.  What is bulkhead pattern?
62.  What is fallback mechanism?
63.  How does Resilience4j work?
64. Why does each microservice own its database?
65.  Database per service pattern?
66.  What is eventual consistency?
67.  What is distributed transaction?
68.  Why are distributed transactions difficult?
69.  What is the Saga Pattern?
70.  Choreography Saga vs Orchestration Saga?
71.  What is CQRS?
72.  What is Event Sourcing?
73.  Difference between ACID and BASE?
74. What is a distributed system?
75.  What are the challenges of distributed systems?
76.  What is network latency?
77.  What is partial failure?
78.  What is the CAP theorem?
79.  Explain Consistency in CAP.
80.  Explain Availability in CAP.
81.  Explain Partition Tolerance in CAP.
82.  Why can’t all three CAP properties be fully achieved simultaneously?
83.  Give real-world examples of CP and AP systems.


