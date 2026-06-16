# System Design One-Shot Course — Part 1

## Engineer-Focused Tutorial Notes in My Style

**Source context:** These notes are based on the YouTube transcript you extracted from the Telusko system design one-shot course, but this file is **not a transcript**. It is a rewritten learning guide that explains each first-half section in a clearer, interview-ready, mid-level-engineer style.

**Part 1 coverage:** Chapters 1–24, from **Introduction** to **Cache**.

**Part 2 should start from:** Cache Strategies, Cache Eviction Policies, Load Balancer, Distributed Databases, Replication, Partitioning, CAP Theorem, Message Queue, Fault Tolerance, Monitoring, and Streaming Application Design.

---

# How to Study This File

Do not read system design like a list of definitions. Read it like an engineering decision-making playbook.

For every topic, ask four questions:

1. **What problem does this solve?**
2. **What breaks if we do not use it?**
3. **What trade-off does it introduce?**
4. **How would I explain this in an interview?**

A mid-level engineer should not only say, “Use cache” or “Use load balancer.” A stronger answer sounds like this:

> “The bottleneck is repeated database reads for mostly stable data. I would add a cache with a short TTL, measure cache-hit ratio, and define invalidation rules so we do not serve stale critical data.”

That is the level this guide aims to build.

---

# Part 1 Table of Contents

1. Introduction
2. Index / Course Map
3. Hello System Design
4. Designing — Alien Bank
5. Components of System Design
6. Types of Applications
7. Data Intensive Application
8. Compute Intensive Application
9. Types of Requirement
10. Functional Requirement
11. Non-Functional Requirement
12. Domain Name Service \(DNS\)
13. DNS System Architecture
14. Application Programming Interface \(API\)
15. Types of APIs
16. RESTful Request
17. RESTful Response
18. Database
19. Relational Database — SQL
20. DB Constraints
21. DB Joins
22. Non-Relational Database — NoSQL
23. Types of NoSQL Databases
24. Cache

---

# 1. Introduction

## What this section is really saying

System design is the skill of making software work beyond the happy path.

Writing code that works for one user is normal development. Designing a system that works for millions of users, under failures, with latency constraints, with safe data handling, is system design.

The mindset shift is important:

```text
Coding question mindset:
Can I make this feature work?

System design mindset:
Can I make this feature work reliably, securely, and efficiently when real users, failures, and growth enter the picture?
```

A beginner may think system design is about learning buzzwords like Redis, Kafka, Kubernetes, sharding, CDN, or load balancer. A better engineer understands that these tools exist because real systems face pressure.

## Why companies ask system design

Companies ask system design because production software is not only about syntax. They want to know whether you can think about:

- traffic growth,
- data safety,
- latency,
- failures,
- scaling,
- operational visibility,
- cost,
- maintainability,
- trade-offs.

For example, assume you build an inventory app. It works well locally. But now imagine:

- 50,000 users open it during a sale,
- multiple users edit the same stock item,
- the database becomes slow,
- notification sending fails,
- one server crashes,
- users complain that data is inconsistent.

System design is how you proactively think about these issues before production chaos forces you to.

## Mid-level engineer takeaway

At a mid-level, you are not expected to design Netflix perfectly from memory. But you are expected to reason clearly:

- identify core requirements,
- choose simple architecture first,
- identify bottlenecks,
- scale only where necessary,
- explain trade-offs.

## Interview framing

A strong answer starts like this:

```text
Before designing, I would clarify the functional and non-functional requirements.
Then I would estimate scale, identify read/write patterns, decide storage, define APIs,
and then discuss scalability, reliability, caching, queues, and observability.
```

That alone makes you sound structured.

---

# 2. Index / Course Map

## What this section is really doing

The course index gives the architecture of learning system design. It moves in this order:

```text
Foundations
  ↓
Communication
  ↓
Data storage
  ↓
Caching and scaling
  ↓
Distributed systems
  ↓
Reliability and operations
  ↓
Case study
```

This is the correct order because you cannot understand distributed databases before understanding what a database does. You cannot understand caching strategies before understanding why database calls become expensive. You cannot understand CAP theorem before understanding consistency and network partitions.

## Learning map

### Foundation layer

This includes:

- what system design is,
- components of a system,
- application types,
- requirements.

This layer helps you understand **what you are designing**.

### Communication layer

This includes:

- DNS,
- APIs,
- REST,
- SOAP,
- GraphQL,
- gRPC,
- WebSockets.

This layer helps you understand **how systems talk**.

### Data layer

This includes:

- SQL,
- NoSQL,
- database constraints,
- joins,
- storage modeling.

This layer helps you understand **where the system truth lives**.

### Performance layer

This starts with cache. Later it expands into load balancing, replication, partitioning, queues, and distributed patterns.

This layer helps you understand **how the system survives scale**.

## Mid-level engineer takeaway

Do not learn system design randomly. Learn it like a stack:

```text
User action
  → DNS resolution
  → API call
  → Load balancer
  → Backend service
  → Cache check
  → Database query
  → Async queue if needed
  → Response
  → Logs and metrics
```

Once this flow becomes natural, most system design interviews become easier.

---

# 3. Hello System Design

## What is a system?

A system is a collection of components working together toward a goal.

In software, those components may be:

- mobile app,
- browser frontend,
- backend API,
- database,
- cache,
- queue,
- storage service,
- monitoring service,
- authentication service,
- payment gateway.

The common goal may be something like:

- allow users to send messages,
- allow customers to place orders,
- allow viewers to watch videos,
- allow bank users to transfer money,
- allow developers to deploy code safely.

## What is design?

Design is the set of choices you make about how those components are arranged and how they behave.

Design includes decisions like:

- Should this be a monolith or microservices?
- Should we use SQL or NoSQL?
- Should writes be synchronous or asynchronous?
- Should we cache this data?
- What happens when a dependency fails?
- How do we monitor the system?
- What consistency level do we need?

## A useful definition

```text
System = components + interactions + goal
System design = choosing those components and interactions based on requirements and trade-offs
```

## Why real systems are adjustable

Unlike biological systems, software systems are built by engineers. That means we can modify them when the load changes.

For example:

- add another server,
- introduce a cache,
- split a database,
- add a queue,
- move static files to CDN,
- introduce service boundaries,
- add monitoring,
- change deployment strategy.

That flexibility is powerful, but also dangerous. Every new component adds complexity.

## Mid-level engineer takeaway

A good system design is not the most complex architecture. It is the architecture that meets the requirement with the least unnecessary complexity.

A senior-style sentence:

```text
I would start simple and add distributed components only when the requirement justifies the operational cost.
```

---

# 4. Designing — Alien Bank

## Why this example is useful

The Alien Bank example is a metaphor for scaling a system. Instead of saying “server,” “load balancer,” and “database” immediately, it maps them to something familiar:

```text
Customer        = incoming request
Queue           = traffic waiting to be processed
Cashier         = application code
Cash counter    = server
Central records = database
Middleman       = load balancer
```

This example is powerful because it shows how system design evolves from real pain points.

## Initial system

Alien Bank starts with one cashier and one counter.

```text
Customers → Cashier/Counter → Receipt
```

This is like a simple application:

```text
Client → One backend server → Local data or one database
```

It works when the traffic is small.

## Issue 1: Process is slow

The cashier takes too much time per customer.

Software equivalent:

- inefficient code,
- slow algorithms,
- poor database queries,
- blocking operations,
- unnecessary loops,
- bad serialization,
- heavy response payloads.

First fix: improve the existing code.

This is where low-level design, clean code, indexing, algorithmic improvement, and profiling matter.

### Engineering lesson

Before scaling infrastructure, check whether your code is wasteful.

Do not add 10 servers to hide a bad query if a simple index can fix the issue.

## Issue 2: One counter is improved but still limited

The cashier is trained. The desk is upgraded. A cash-counting machine is added.

Software equivalent: vertical scaling.

```text
Small server → Bigger server
2 CPU / 4 GB RAM → 8 CPU / 32 GB RAM
```

Vertical scaling is simple because the architecture does not change much. But it has a ceiling.

### Vertical scaling pros

- easy to apply,
- fewer architecture changes,
- useful for early-stage systems,
- lower coordination complexity.

### Vertical scaling cons

- expensive at high end,
- hardware limit exists,
- single point of failure may remain,
- does not always solve throughput problems.

## Issue 3: Customers still wait

Even if one counter becomes faster, one counter cannot handle unlimited customers.

Software equivalent: horizontal scaling.

```text
Client traffic → Server 1
               → Server 2
               → Server 3
```

Horizontal scaling means adding more servers instead of making one server bigger.

### Horizontal scaling pros

- better throughput,
- can survive one server failure,
- easier to scale gradually,
- common in cloud deployments.

### Horizontal scaling cons

- requires traffic distribution,
- shared state becomes harder,
- sessions must be handled carefully,
- data consistency becomes important,
- deployment and monitoring become more complex.

## Issue 4: Two counters have separate records

If each counter keeps its own records, the bank can produce inconsistent data.

Example problem:

- Counter 1 thinks user has ₹10,000.
- User withdraws ₹5,000 at Counter 1.
- Counter 2 is not updated.
- User withdraws ₹10,000 again from Counter 2.

This is a consistency failure.

Software equivalent:

```text
Server 1 local data ≠ Server 2 local data
```

Fix: use a shared database or synchronized data system.

```text
Server 1 ┐
         ├── Central Database
Server 2 ┘
```

## Issue 5: One counter gets overloaded while another is idle

If users naturally go to the nearest counter, load becomes uneven.

Software equivalent: some servers receive too many requests while others sit idle.

Fix: add a load balancer.

```text
Requests → Load Balancer → Server 1
                         → Server 2
                         → Server 3
```

The load balancer checks:

- which server is healthy,
- which server has less load,
- which routing strategy should be used.

## Final mapping

| Alien Bank Concept | System Design Concept |
|---|---|
| Customer | Request |
| Queue of customers | Traffic / backlog |
| Cashier | Application code |
| Cash counter | Server |
| Central record book | Database |
| Middleman | Load balancer |
| Faster cashier | Code optimization |
| Bigger counter | Vertical scaling |
| More counters | Horizontal scaling |

## Mid-level engineer takeaway

System design is usually an evolution:

```text
1. Optimize code.
2. Scale vertically.
3. Scale horizontally.
4. Centralize or synchronize data.
5. Balance traffic.
6. Monitor everything.
```

This progression is far more realistic than jumping directly to microservices.

---

# 5. Components of System Design

## Big picture

Most applications are built from a few recurring components:

```text
Client Application
  ↓
Backend Server / Application Code
  ↓
Database
```

As the system grows, more components appear:

```text
Client
  ↓
Load Balancer
  ↓
Backend Services
  ↓       ↓
Cache    Database
  ↓
Message Queue
  ↓
Worker Services

Monitoring and Logs observe everything.
```

## Component 1: Client application

The client is where the user interacts with the system.

Examples:

- Android app,
- iOS app,
- browser web app,
- desktop app,
- ATM machine,
- third-party integration.

For you as an Android/backend engineer, client-side concerns include:

- API contracts,
- request retries,
- authentication tokens,
- offline behavior,
- local caching,
- pagination,
- error handling,
- loading states,
- response parsing.

## Component 2: Backend server

The backend hides complexity from the client.

A user should not need to know SQL joins or database schema. The backend exposes clean APIs.

Example:

```http
GET /api/v1/products
POST /api/v1/orders
GET /api/v1/users/{id}
```

The backend is responsible for:

- validation,
- business rules,
- authorization,
- database access,
- calling other services,
- caching decisions,
- queue publishing,
- logging and monitoring.

## Component 3: Database

The database stores durable data.

Without a database, data may disappear when the server restarts.

Databases handle:

- users,
- orders,
- transactions,
- messages,
- inventory,
- payments,
- profile data,
- metadata.

## Component 4: Cache

Cache stores frequently accessed data in faster storage.

It reduces repeated expensive database calls.

Example:

```text
Without cache:
Every homepage request → DB query

With cache:
Most homepage requests → Cache
Only missing/expired data → DB
```

## Component 5: Load balancer

A load balancer distributes traffic across multiple backend servers.

It improves:

- scalability,
- availability,
- fault isolation,
- server utilization.

## Component 6: Message queue

A message queue helps process tasks asynchronously.

Example order flow:

```text
User places order
  ↓
Order API confirms order
  ↓
Queue receives background tasks
  ↓
Email worker sends email
Invoice worker creates invoice
Notification worker sends push notification
```

The user does not need to wait for every background task before getting a response.

## Component 7: Monitoring and logs

Monitoring tells you whether the system is healthy. Logs tell you what happened.

Important things to monitor:

- error rate,
- latency,
- request count,
- CPU usage,
- memory usage,
- database query time,
- queue length,
- cache hit ratio.

## Mid-level engineer takeaway

When you design any system, start with this checklist:

```text
Who are the clients?
What APIs are needed?
Where does data live?
Which data is hot and cacheable?
How do we scale backend traffic?
What can be processed asynchronously?
How do we monitor failures?
```

---

# 6. Types of Applications

## Why classification matters

Before choosing tools, identify what kind of workload the application has.

Many engineers jump into solutions too early:

- “Use Redis.”
- “Use Kafka.”
- “Use MongoDB.”
- “Use microservices.”

But the better question is:

```text
Where is the system spending time?
```

Is it spending time moving data around, or doing heavy computation?

That gives two broad categories:

1. **Data-intensive applications**
2. **Compute-intensive applications**

## Why this matters financially

Wrong diagnosis wastes money.

If your bottleneck is database reads and network movement, buying more GPU will not help.

If your bottleneck is video rendering, adding a bigger SQL database will not help.

## Simple diagnosis

```text
If time is lost in reading, writing, moving, or storing data → data-intensive.
If time is lost in calculations, transformations, rendering, or ML processing → compute-intensive.
```

## Mid-level engineer takeaway

Before proposing architecture, say:

```text
I would first identify whether this workload is data-heavy or compute-heavy, because the scaling strategy changes depending on the bottleneck.
```

That is a strong engineering move.

---

# 7. Data Intensive Application

## What it means

A data-intensive application spends most of its effort handling data.

This includes:

- storing data,
- reading data,
- updating data,
- moving data between services,
- indexing data,
- caching data,
- keeping data consistent,
- serving data to many users.

The core challenge is not heavy math. The challenge is data volume, data movement, and data correctness.

## Examples

- Instagram feed,
- WhatsApp messages,
- banking transactions,
- analytics dashboards,
- log processing systems,
- e-commerce catalog browsing,
- notification history,
- inventory management.

## Instagram-style example

Instagram feed is mostly data-intensive.

The system needs to:

- fetch posts,
- load images/videos,
- show captions,
- show likes/comments,
- sort by relevance/time,
- serve millions of users,
- handle new uploads continuously.

The hard part is not calculating 2 + 2. The hard part is moving huge amounts of media and metadata quickly.

A simplified flow:

```text
User opens feed
  ↓
Feed service fetches post metadata
  ↓
Media URLs point to CDN
  ↓
Client loads images/videos from CDN
  ↓
Likes/comments may come from separate services
```

## Common bottlenecks

### Database bottleneck

Queries may become slow because:

- too much data,
- missing indexes,
- expensive joins,
- high write volume,
- poor schema design,
- too many concurrent reads.

### Network bottleneck

Even if the database is fast, data movement can be slow because:

- response payload is too large,
- client downloads too much data,
- APIs are too chatty,
- media files are served from origin instead of CDN,
- servers are far from users.

### Consistency bottleneck

When many users update data at the same time, the system must avoid incorrect state.

Banking example:

```text
Two withdrawals at the same time must not allow the balance to go negative incorrectly.
```

## Solutions often used

- indexing,
- caching,
- database replication,
- database sharding,
- pagination,
- CDN,
- read replicas,
- asynchronous processing,
- event-driven architecture,
- data partitioning,
- consistency controls.

## Mid-level engineer takeaway

For data-intensive apps, think in this order:

```text
Data model → access patterns → indexes → cache → replication → partitioning → consistency trade-offs
```

## Interview framing

A polished answer:

```text
This looks data-intensive because the main challenge is serving and updating large volumes of user data. I would focus on schema design, indexes, caching, read replicas, pagination, CDN for media, and clear consistency requirements for critical writes.
```

---

# 8. Compute Intensive Application

## What it means

A compute-intensive application spends most of its effort doing heavy calculations or transformations.

Here, the data size may be manageable, but processing that data is expensive.

## Examples

- video rendering,
- image processing,
- machine learning training,
- simulations,
- cryptography,
- compression,
- recommendation model execution,
- 3D rendering,
- scientific computation.

## Video rendering example

Suppose a user uploads a 4K video. The system may need to:

- decode the uploaded file,
- create different resolutions,
- compress output,
- generate thumbnails,
- extract metadata,
- detect unsafe content,
- store processed versions.

This is compute-heavy.

A simplified pipeline:

```text
Upload video
  ↓
Store original file
  ↓
Send job to processing queue
  ↓
Worker picks job
  ↓
Transcode to 1080p / 720p / 480p
  ↓
Store processed files
  ↓
Notify user when ready
```

## CPU-bound vs GPU-bound

### CPU-bound

CPU-bound workloads need strong general-purpose processing.

Examples:

- encryption,
- compression,
- backend calculations,
- some simulations,
- rule engines.

### GPU-bound

GPU-bound workloads benefit from parallel processing.

Examples:

- machine learning,
- video rendering,
- image transformation,
- graphics simulation.

## Solutions often used

- parallel processing,
- worker pools,
- GPU instances,
- batch processing,
- job queues,
- autoscaling workers,
- optimized algorithms,
- precomputation,
- caching computed results.

## Common mistake

Do not solve compute problems with only database tricks.

If rendering is slow, adding a database index will not fix rendering. You need better compute strategy.

## Mid-level engineer takeaway

For compute-intensive apps, think in this order:

```text
Algorithm → parallelism → worker architecture → CPU/GPU choice → queueing → cost optimization → result storage
```

## Interview framing

```text
This workload is compute-intensive because the expensive part is processing, not data retrieval. I would use asynchronous jobs, worker pools, parallel processing, and possibly GPU-backed workers depending on the computation type.
```

---

# 9. Types of Requirement

## Why requirements matter

System design should start with requirements, not tools.

There are two major requirement categories:

1. **Functional requirements**
2. **Non-functional requirements**

```text
Functional = what the system should do.
Non-functional = how well the system should do it.
```

## Example

For a food delivery app:

Functional requirements:

- user can search restaurants,
- user can place order,
- restaurant can accept order,
- delivery partner can update status,
- user can pay online.

Non-functional requirements:

- search should respond within 200 ms,
- payment should be highly consistent,
- order tracking should update near real time,
- system should handle lunch/dinner peak traffic,
- service should be available even if notification service fails.

## Why this distinction matters

Functional requirements define features. Non-functional requirements drive architecture.

For example:

- “User can upload video” is functional.
- “Uploaded video should be processed within 2 minutes for 95% of uploads” is non-functional.

The second statement changes your architecture because now you may need queues, workers, autoscaling, monitoring, and retry logic.

## Mid-level engineer takeaway

In interviews, always clarify both.

A good opening:

```text
Let me first clarify the functional requirements and then define the non-functional requirements like scale, latency, availability, consistency, and security. Those will drive the architecture.
```

---

# 10. Functional Requirement

## What functional requirements are

Functional requirements describe user-visible capabilities.

They answer:

```text
What actions should users or systems be able to perform?
```

## E-commerce example

For an Amazon-like system, functional requirements may include:

- user can register,
- user can log in,
- user can search products,
- user can filter products,
- user can view product details,
- user can add item to cart,
- user can apply coupon,
- user can place order,
- user can pay,
- user can track order,
- seller can update inventory,
- admin can manage catalog.

## How to avoid scope explosion

In interviews, you should not design the entire Amazon system in one answer. Scope it.

Example:

```text
For this design, I will focus on product search, cart, order placement, and payment confirmation. I will keep reviews, recommendations, and returns out of scope unless needed.
```

This is important because system design interviews reward prioritization.

## How to write functional requirements well

Weak:

```text
Build an e-commerce app.
```

Strong:

```text
Users should be able to search products, view details, add products to cart, place orders, and track order status.
```

Even stronger:

```text
Users should be able to search products by keyword/category, view product details with price and availability, add available products to cart, place an order, pay online, and track fulfillment status.
```

## Mid-level engineer takeaway

Functional requirements define API boundaries.

Example:

```text
Search products        → GET /products?query=phone
View product details   → GET /products/{id}
Add to cart            → POST /cart/items
Place order            → POST /orders
Track order            → GET /orders/{id}/status
```

A backend engineer should naturally connect features to APIs.

---

# 11. Non-Functional Requirement

## What non-functional requirements are

Non-functional requirements define quality attributes.

They answer:

```text
How should the system behave under real-world conditions?
```

Important NFRs:

- scalability,
- availability,
- reliability,
- performance,
- security,
- maintainability,
- observability,
- fault tolerance,
- cost efficiency.

## Scalability

Scalability means the system can handle growth.

Growth can be in:

- users,
- requests per second,
- data size,
- geographic regions,
- media files,
- background jobs,
- concurrent connections.

Scalability questions:

```text
Can the system handle 10x traffic during a sale?
Can we add more servers without rewriting everything?
Can the database handle more reads and writes?
```

## Availability

Availability means the system remains usable.

Common availability targets:

```text
99%      ≈ 3.65 days downtime/year
99.9%    ≈ 8.76 hours downtime/year
99.99%   ≈ 52.6 minutes downtime/year
99.999%  ≈ 5.26 minutes downtime/year
```

High availability usually needs redundancy.

## Reliability

Reliability means the system behaves correctly over time.

A reliable system should not silently lose data.

Examples:

- payment should not be lost,
- order should not disappear,
- inventory should not become negative incorrectly,
- message should eventually be delivered or marked failed.

## Performance

Performance includes latency and throughput.

- **Latency:** how long one request takes.
- **Throughput:** how many requests the system handles per second.

Useful metrics:

- p50 latency,
- p90 latency,
- p95 latency,
- p99 latency.

If p99 latency is bad, some users are having a very poor experience even if average latency looks fine.

## Security

Security includes:

- authentication,
- authorization,
- encryption in transit,
- encryption at rest,
- input validation,
- rate limiting,
- audit logs,
- secret management.

A mid-level engineer should never design a system with “security later.”

## Maintainability

Maintainability means engineers can safely change the system later.

It includes:

- clear code boundaries,
- modular services,
- API versioning,
- good tests,
- documentation,
- CI/CD,
- observability,
- migration discipline.

## Observability

Observability means you can understand what the system is doing from the outside.

It includes:

- logs,
- metrics,
- traces,
- dashboards,
- alerts.

## Mid-level engineer takeaway

Functional requirements tell you the feature list. Non-functional requirements tell you the engineering strategy.

In interviews, NFRs make your design mature.

---

# 12. Domain Name Service \(DNS\)

## What problem DNS solves

Humans use names. Machines use IP addresses.

You type:

```text
telusko.com
```

But the browser needs an IP address like:

```text
192.0.2.10
```

DNS converts domain names into IP addresses.

```text
Domain name → DNS → IP address
```

## Why not store all domain mappings in the browser?

Because there are hundreds of millions of domains and IP addresses can change.

Storing all mappings locally would create problems:

- huge storage requirement,
- updates would be impossible to coordinate,
- stale IPs would break websites,
- one local list cannot represent the live internet.

## Domain and subdomain

A domain:

```text
example.com
```

Subdomains:

```text
api.example.com
docs.example.com
learn.example.com
```

The root domain can have many subdomains.

## Why DNS matters in system design

DNS is not only a browser topic. It is also used in backend infrastructure.

Examples:

- API domain points to load balancer,
- CDN domain points to edge servers,
- database endpoint resolves to database host,
- service discovery may rely on DNS,
- failover can be done through DNS routing.

## Mid-level engineer takeaway

When a user says “the website is slow,” the request may have many stages before reaching your backend:

```text
Browser
  ↓
DNS resolution
  ↓
TCP/TLS connection
  ↓
Load balancer
  ↓
Backend
  ↓
Database/cache
```

DNS is the first lookup stage in that path.

---

# 13. DNS System Architecture

## High-level flow

When the browser needs the IP for a domain, DNS resolution happens through several layers.

```text
Browser / OS
  ↓
DNS Resolver
  ↓
Root Server
  ↓
TLD Server
  ↓
Authoritative Name Server
  ↓
IP Address Returned
```

## Step 1: Browser and OS cache

The browser first checks whether it already knows the IP.

If not, the OS may check its own DNS cache.

Why? Because DNS resolution takes time, and repeated lookups should be avoided.

## Step 2: DNS resolver

The DNS resolver is usually provided by:

- your ISP,
- Google DNS,
- Cloudflare DNS,
- enterprise network,
- router configuration.

The resolver does the heavy lifting for you.

## Step 3: Root server

The root server does not know the final IP for every website.

It knows where to find top-level domain servers.

For example:

```text
For .com domains, ask this TLD server.
For .org domains, ask this TLD server.
For .in domains, ask this TLD server.
```

## Step 4: TLD server

TLD means top-level domain.

Examples:

```text
.com
.org
.net
.in
.edu
```

The TLD server points the resolver to the authoritative name server for the domain.

## Step 5: Authoritative name server

The authoritative name server has the actual DNS records for the domain.

It can return records like:

- A record,
- AAAA record,
- CNAME,
- MX,
- TXT,
- NS.

For a website, it commonly returns the IP address or a CNAME that eventually resolves to the target.

## DNS caching

DNS records are cached at multiple levels:

- browser cache,
- OS cache,
- router cache,
- DNS resolver cache.

DNS records have TTL values.

TTL means:

```text
How long this DNS answer can be reused before checking again.
```

## Practical example

If you deploy a React frontend on Vercel and connect a custom domain, your DNS may point:

```text
vishnu.simhatech.xyz → Vercel edge infrastructure
```

Your users do not need to know Vercel IPs. DNS abstracts that.

## Interview framing

```text
DNS resolves a human-readable domain name into the network address needed to reach the server. The resolver checks cache first, then queries root, TLD, and authoritative name servers. DNS caching reduces lookup latency but can delay propagation when records change.
```

---

# 14. Application Programming Interface \(API\)

## What an API is

An API is a contract that allows one software system to use another system’s capability or data without knowing its internal implementation.

```text
Client does not need database access.
Client calls backend API.
Backend safely handles business logic and data access.
```

## Why APIs exist

Without APIs, every client would need to understand database structure, security rules, and business logic.

That would be dangerous and unmaintainable.

APIs help with:

- abstraction,
- security,
- reuse,
- integration,
- separation of frontend and backend,
- controlled data exposure.

## Example: Google Maps in another app

Apps like ride-sharing or food delivery do not build a global map system from scratch. They use map APIs.

The app asks:

```text
Give me route between pickup and destination.
```

The map provider responds with route data.

## API as a backend/frontend boundary

In your own app:

```text
React frontend → REST API → Spring Boot backend → Database
Android app    → REST API → Spring Boot backend → Database
```

The frontend does not care whether the backend is Java, Kotlin, Node.js, Python, or Go. It cares about API contract.

## Why not expose database directly?

Because direct database access causes:

- security risk,
- schema coupling,
- accidental data deletion,
- no business validation,
- no authorization control,
- difficulty changing schema,
- poor auditability.

## Mid-level engineer takeaway

APIs are not just endpoints. APIs are contracts.

A good API contract defines:

- request method,
- path,
- headers,
- authentication,
- request body,
- response body,
- status codes,
- error format,
- rate limits,
- versioning.

---

# 15. Types of APIs

## Overview

Common communication styles include:

1. REST
2. SOAP
3. GraphQL
4. gRPC
5. WebSockets

Each solves a different communication need.

---

## REST APIs

REST is widely used for web and mobile backend communication.

Typical style:

```http
GET /users/1
POST /orders
PATCH /profile
DELETE /cart/items/5
```

REST commonly uses JSON.

### When REST is good

- CRUD applications,
- mobile apps,
- web apps,
- public APIs,
- simple service boundaries,
- predictable resources.

### REST trade-off

REST can over-fetch or under-fetch data.

Example:

```text
Frontend needs user name and profile picture,
but API returns full user profile with 40 fields.
```

That is over-fetching.

---

## SOAP APIs

SOAP is older and uses XML.

It is common in legacy enterprise systems, banking, government, and older integrations.

### When SOAP appears

- legacy systems,
- strict enterprise contracts,
- older payment/finance integrations,
- systems requiring formal schemas.

### SOAP trade-off

SOAP is structured but heavier. XML payloads are more verbose than JSON.

---

## GraphQL

GraphQL exposes a single endpoint where the client asks for exactly the fields it needs.

Example idea:

```graphql
query {
  user(id: 1) {
    name
    profilePicture
  }
}
```

### When GraphQL is good

- complex frontend data needs,
- multiple clients with different data requirements,
- reducing over-fetching,
- dashboards,
- flexible UI composition.

### GraphQL trade-off

GraphQL can move complexity to the backend:

- query cost control,
- authorization per field,
- caching complexity,
- N+1 query problems.

---

## gRPC

gRPC is efficient service-to-service communication, often using Protocol Buffers.

It is common in microservices where internal communication must be fast and strongly typed.

### When gRPC is good

- internal microservices,
- low-latency communication,
- strongly typed contracts,
- streaming use cases,
- high-throughput backend systems.

### gRPC trade-off

It is less browser-friendly than REST and can be harder for public API consumers.

---

## WebSockets

WebSockets maintain a persistent two-way connection.

Unlike request-response APIs, both client and server can send messages after the connection is established.

### When WebSockets are good

- chat,
- live notifications,
- multiplayer games,
- trading dashboards,
- live quiz systems,
- real-time collaboration.

### WebSocket trade-off

Persistent connections are harder to scale than stateless HTTP requests.

You need to think about:

- connection state,
- reconnection,
- heartbeats,
- load balancing,
- horizontal scaling,
- message delivery guarantees.

## Decision table

| API Type | Best For | Payload Style | Main Trade-off |
|---|---|---|---|
| REST | General web/mobile APIs | JSON | Can over/under-fetch |
| SOAP | Legacy enterprise systems | XML | Verbose/heavy |
| GraphQL | Flexible frontend queries | JSON-like response | Backend complexity |
| gRPC | Internal microservices | Protobuf | Less simple for browsers |
| WebSocket | Real-time two-way updates | Custom/JSON/binary | Stateful scaling |

## Mid-level engineer takeaway

Do not say one API type is universally best. Say:

```text
For public/mobile APIs, REST is usually simple and effective. For internal low-latency microservice communication, gRPC can be better. For real-time updates, WebSockets are suitable. For flexible frontend-driven data fetching, GraphQL is useful.
```

---

# 16. RESTful Request

## REST request structure

A REST request usually has:

```text
HTTP method
Path
Headers
Query parameters
Request body
```

Example:

```http
POST /api/v1/users
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Vishnu",
  "email": "vishnu@example.com"
}
```

## JSON basics

JSON uses key-value pairs.

Supported values include:

- string,
- number,
- boolean,
- object,
- array,
- null.

Example:

```json
{
  "name": "Vishnu",
  "experienceYears": 3.5,
  "skills": ["Kotlin", "Java", "Spring Boot"],
  "available": true
}
```

## HTTP methods

### GET

Use GET to retrieve data.

```http
GET /users
GET /users/123
```

GET should not change server state.

### POST

Use POST to create a new resource or trigger an action.

```http
POST /users
POST /orders
```

### PUT

Use PUT to replace a resource.

```http
PUT /users/123
```

If you send partial data with PUT, missing fields may be treated as null/default depending on implementation. Use carefully.

### PATCH

Use PATCH for partial update.

```http
PATCH /users/123

{
  "displayName": "Vishnu Simha"
}
```

### DELETE

Use DELETE to delete a resource.

```http
DELETE /users/123
```

## Resource naming

Use nouns, not verbs.

Good:

```http
GET /users
POST /orders
PATCH /products/10
```

Weak:

```http
GET /getUsers
POST /createOrder
POST /updateProduct
```

The method already tells the action.

## Path parameters

Path parameters identify a specific resource.

```http
GET /users/{userId}
GET /orders/{orderId}
GET /blogs/{blogId}/comments
```

Use path parameters for hierarchy and identity.

## Query parameters

Query parameters are good for filtering, sorting, searching, and pagination.

```http
GET /products?category=phones&sort=price_asc&page=2&limit=20
GET /blogs?search=java
```

## Request body

Use body for complex or sensitive input.

Example login:

```http
POST /login

{
  "email": "user@example.com",
  "password": "secret"
}
```

Do not send passwords in query parameters.

Bad:

```http
GET /login?email=a@b.com&password=secret
```

Because URLs can appear in logs, browser history, proxies, and analytics tools.

## Nested resources

Use nesting when there is a clear ownership relationship.

```http
GET /blogs/{blogId}/comments
POST /blogs/{blogId}/comments
```

This reads naturally:

```text
Get comments belonging to this blog.
```

Do not over-nest too deeply.

Weak:

```http
/users/{userId}/blogs/{blogId}/comments/{commentId}/likes/{likeId}
```

Better:

```http
GET /comments/{commentId}/likes
```

## Filtering vs nesting

Use nesting for direct relationship:

```http
GET /blogs/{blogId}/comments
```

Use query parameters for filtering:

```http
GET /products?color=black&minPrice=1000&maxPrice=5000
```

## API versioning

Versioning avoids breaking old clients.

```http
/api/v1/users
/api/v2/users
```

For Android apps, versioning is important because old app versions may remain installed for months.

## Mid-level engineer takeaway

A good REST API is predictable.

Checklist:

```text
Use nouns for resources.
Use proper HTTP methods.
Use path params for identity.
Use query params for filtering/pagination.
Use body for complex input.
Use consistent response format.
Use clear status codes.
Version APIs when needed.
```

---

# 17. RESTful Response

## Response structure

A REST response usually includes:

```text
Status code
Headers
Response body
```

Example:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "user": {
    "id": 123,
    "name": "Vishnu"
  }
}
```

## Common status codes

| Code | Meaning | Use Case |
|---|---|---|
| 200 | OK | Successful read/update with response body |
| 201 | Created | New resource created |
| 204 | No Content | Success, but no response body |
| 301 | Permanent Redirect | Resource moved permanently |
| 302 | Temporary Redirect | Temporary redirect |
| 400 | Bad Request | Validation/request format problem |
| 401 | Unauthorized | Not authenticated |
| 403 | Forbidden | Authenticated but not allowed |
| 404 | Not Found | Resource does not exist |
| 500 | Internal Server Error | Unexpected backend failure |

## 401 vs 403

This is often confused.

```text
401 = Who are you? Please authenticate.
403 = I know who you are, but you do not have permission.
```

Example:

- No token → 401.
- Valid token but user is not admin → 403.

## 400 vs 500

```text
400 = client sent invalid data.
500 = server failed unexpectedly.
```

Example 400:

```json
{
  "error": "VALIDATION_ERROR",
  "message": "Email is required"
}
```

Example 500:

```json
{
  "error": "INTERNAL_SERVER_ERROR",
  "message": "Something went wrong"
}
```

Do not expose database stack traces to users.

## Response body best practice

Avoid returning a raw array as the top-level response.

Less flexible:

```json
[
  { "id": 1, "name": "A" },
  { "id": 2, "name": "B" }
]
```

Better:

```json
{
  "users": [
    { "id": 1, "name": "A" },
    { "id": 2, "name": "B" }
  ],
  "count": 2
}
```

Why better?

Because later you can add metadata without breaking clients:

```json
{
  "users": [],
  "count": 0,
  "page": 1,
  "totalPages": 5
}
```

## Error response consistency

A mature API uses a consistent error format.

Example:

```json
{
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product not found",
  "details": {
    "productId": "123"
  },
  "traceId": "abc-xyz-123"
}
```

The `traceId` helps backend teams find logs.

## Mapping requests to responses

### GET many

```http
GET /users
```

Response:

```http
200 OK
```

```json
{
  "users": [],
  "count": 0
}
```

### GET one

```http
GET /users/123
```

Possible responses:

- 200 if found,
- 404 if not found,
- 401/403 if access problem.

### POST create

```http
POST /users
```

Possible responses:

- 201 if created,
- 400 if validation fails,
- 409 if duplicate/conflict,
- 500 if backend fails.

### DELETE

```http
DELETE /users/123
```

Possible responses:

- 204 if deleted and no body,
- 404 if not found,
- 403 if not allowed.

## Mid-level engineer takeaway

Good API design improves frontend reliability. Android/web clients can handle errors cleanly when status codes and response formats are consistent.

Interview sentence:

```text
I would define consistent response envelopes, use proper HTTP status codes, avoid leaking internal errors, and include trace IDs for observability.
```

---

# 18. Database

## Why databases are needed

Server memory is temporary. If the server restarts, in-memory data is lost.

A database provides durable storage.

```text
Client → Backend → Database
```

The database is usually the source of truth for important application data.

## What kind of data lives in databases?

- users,
- accounts,
- orders,
- payments,
- products,
- inventory,
- messages,
- posts,
- comments,
- preferences,
- logs,
- audit records.

## Main database categories

1. SQL / relational databases
2. NoSQL / non-relational databases

## SQL vs NoSQL at a high level

SQL:

- structured schema,
- tables,
- rows,
- columns,
- relationships,
- transactions,
- strong consistency.

NoSQL:

- flexible schema,
- document/key-value/graph/column models,
- easier horizontal scaling in many cases,
- useful for unstructured or high-scale data,
- often trades strict consistency for availability/performance.

## Important principle

Database choice should follow access patterns.

Ask:

```text
How will data be read?
How will data be written?
How often does schema change?
Do we need transactions?
Do we need strong consistency?
How large will the data grow?
What queries are critical?
```

## Mid-level engineer takeaway

Do not choose MongoDB just because it sounds modern. Do not choose SQL just because it is familiar.

Choose based on the system’s data shape and consistency needs.

---

# 19. Relational Database — SQL

## What SQL databases are good at

SQL databases store data in tables with a defined schema.

Example users table:

| id | first_name | last_name | phone |
|---|---|---|---|
| 1 | Vishnu | Dussa | 9840xxxxxx |

Each row is a record. Each column is an attribute.

## Core strengths

SQL is strong for:

- structured data,
- relationships,
- transactions,
- constraints,
- consistency,
- reporting queries,
- financial systems,
- order systems,
- inventory systems.

## Example: order system

Tables may include:

```text
users
products
orders
order_items
payments
shipments
```

This structure is natural because orders relate to users, products, payments, and shipment status.

## ACID concept

SQL databases commonly support ACID transactions.

### Atomicity

All operations in a transaction succeed or fail together.

Payment example:

```text
Debit account A
Credit account B
```

You should not allow debit without credit.

### Consistency

Database moves from one valid state to another valid state.

### Isolation

Concurrent transactions do not corrupt each other.

### Durability

Once committed, data should survive crashes.

## When SQL is a strong choice

Choose SQL when:

- schema is clear,
- relationships matter,
- joins are needed,
- transactions are critical,
- data correctness matters,
- reporting/querying is important.

Examples:

- banking,
- payments,
- inventory,
- appointments,
- order management,
- enterprise admin systems.

## Mid-level engineer takeaway

For transactional systems, SQL is usually the default safe choice.

Interview framing:

```text
Since this system needs strong consistency and transactional guarantees, I would start with a relational database like PostgreSQL or MySQL, using proper constraints and indexes.
```

---

# 20. DB Constraints

## Why constraints matter

Constraints protect data quality at the database level.

Application validation is useful, but database constraints are the final guardrail.

If a bug bypasses backend validation, constraints can still prevent bad data.

## Unique constraint

Ensures a value is not duplicated.

Example:

```text
username must be unique
email must be unique
invite code must be unique
```

SQL idea:

```sql
email VARCHAR(255) UNIQUE
```

## Not null constraint

Ensures a field must have a value.

Example:

```text
User first name cannot be null.
Order status cannot be null.
Payment amount cannot be null.
```

## Primary key

Uniquely identifies a row.

Example:

```text
users.id
orders.id
products.id
```

A primary key is usually used to fetch one exact record.

## Foreign key

Connects one table to another.

Example:

```text
posts.author_id → users.id
orders.user_id → users.id
order_items.product_id → products.id
```

Foreign keys maintain referential integrity.

They prevent situations like:

```text
Order references a user that does not exist.
```

## Check constraint

Ensures a value follows a rule.

Examples:

```text
price >= 0
age >= 18
quantity > 0
status IN ('PENDING', 'PAID', 'CANCELLED')
```

## Default constraint

Provides a default value when none is supplied.

Examples:

```text
user role = 'STUDENT'
order status = 'PENDING'
subscription plan = 'FREE'
created_at = current timestamp
```

## Why DB constraints are production-grade

Imagine a backend bug accidentally sends negative quantity:

```json
{
  "quantity": -10
}
```

If the database has `CHECK (quantity >= 0)`, it blocks invalid data.

## Mid-level engineer takeaway

A mature system validates at multiple layers:

```text
Frontend validation → Backend validation → Database constraints
```

Do not rely only on frontend validation.

---

# 21. DB Joins

## What joins solve

Joins allow you to combine related data from multiple tables.

Example:

```text
users table + posts table → posts with author details
```

## One-to-many relationship

One user can write many posts.

```text
users
- id
- name

posts
- id
- title
- author_id
```

Relationship:

```text
One user → many posts
```

Example:

```sql
SELECT posts.title, users.name
FROM posts
JOIN users ON posts.author_id = users.id;
```

## Many-to-one relationship

This is the reverse view of one-to-many.

```text
Many posts → one author
```

The database structure is usually the same. The difference is how you describe the direction.

## Many-to-many relationship

Many students can enroll in many courses.

You need a junction table.

```text
students
- id
- name

courses
- id
- name

student_courses
- student_id
- course_id
```

This table connects both sides.

## One-to-one relationship

One record maps to exactly one related record.

Example:

```text
users
- id
- name

user_profiles
- user_id
- bio
- avatar_url
```

One user has one profile.

## Why not put everything in one table?

A giant table causes:

- duplicated data,
- null-heavy columns,
- difficult updates,
- poor maintainability,
- slower queries,
- unclear ownership.

Normalized design avoids duplication.

## But joins have a cost

Joins can become expensive at high scale.

This is why some systems denormalize read-heavy data.

Example:

```text
Store authorName inside post read model to avoid joining for every feed request.
```

Trade-off:

- faster reads,
- duplicated data,
- need update strategy when author name changes.

## Mid-level engineer takeaway

SQL joins are powerful, but every join is a runtime cost. Use normalized design for correctness, and denormalize carefully for read performance when required.

---

# 22. Non-Relational Database — NoSQL

## Why NoSQL exists

SQL is excellent for structured, relational, transactional data. But not all data fits neatly into tables.

NoSQL is useful when:

- schema changes often,
- data is semi-structured,
- horizontal scaling is a priority,
- high write volume is expected,
- relationships are simple or embedded,
- availability/performance matters more than strict relational consistency.

## SQL scaling challenge

Relational databases can scale vertically first:

```text
Bigger CPU, more RAM, faster disk
```

But eventually one machine reaches a limit.

Horizontal scaling with SQL is possible, but joins and transactions across partitions become complex.

## NoSQL flexibility

A document database can store flexible records.

Example:

```json
{
  "userId": 1,
  "contentType": "image",
  "url": "https://example.com/photo.jpg"
}
```

Another record in the same collection may look different:

```json
{
  "userId": 2,
  "contentType": "text",
  "title": "Java Notes",
  "description": "Introduction to Java"
}
```

This flexible structure is useful for content-heavy platforms.

## Embedded data

NoSQL often embeds related data together.

Example post with comments:

```json
{
  "postId": "p1",
  "content": "System design notes",
  "comments": [
    { "commentId": "c1", "text": "Great" },
    { "commentId": "c2", "text": "Helpful" }
  ]
}
```

This can make reads faster because data is retrieved together.

## Trade-off

Embedding can create problems if nested data grows too much.

Example:

```text
A viral post with 10 million comments should not store all comments inside one document.
```

So NoSQL still needs design discipline.

## SQL vs NoSQL decision

| Use Case | Better Starting Point |
|---|---|
| Payments | SQL |
| Banking ledger | SQL |
| Order management | SQL |
| Inventory correctness | SQL |
| User activity logs | NoSQL |
| Flexible profiles | NoSQL |
| Content metadata | NoSQL or hybrid |
| Chat messages at massive scale | NoSQL / distributed storage |
| Analytics events | NoSQL / columnar / warehouse |

## Mid-level engineer takeaway

NoSQL does not mean “no structure.” It means the structure is usually application-defined rather than rigidly table-defined.

Interview framing:

```text
I would choose SQL for transactional consistency and clear relationships. I would choose NoSQL when schema flexibility, horizontal scale, and high-volume reads/writes are more important than strict relational joins.
```

---

# 23. Types of NoSQL Databases

## NoSQL is not one database type

NoSQL is an umbrella category. Major types include:

1. Key-value databases
2. Document databases
3. Columnar databases
4. Graph databases

---

## Key-value databases

Data is stored as:

```text
key → value
```

Example:

```text
session:user:123 → { token, expiry, roles }
homepage:courses → cached homepage data
```

### Good for

- cache,
- sessions,
- feature flags,
- rate limiting counters,
- temporary tokens,
- fast lookups.

### Example tools

- Redis,
- DynamoDB can also be used in key-value style.

### Trade-off

You usually fetch by key. Complex querying is limited.

---

## Document databases

Data is stored as documents, commonly JSON-like.

Example:

```json
{
  "userId": "u1",
  "name": "Vishnu",
  "skills": ["Kotlin", "Spring Boot"],
  "projects": [
    { "name": "StocKeeper", "type": "Android" }
  ]
}
```

### Good for

- profiles,
- content systems,
- product catalogs,
- flexible records,
- logs,
- event payloads.

### Example tools

- MongoDB,
- CouchDB,
- Firestore.

### Trade-off

Relationships and joins are not as natural as SQL.

---

## Columnar databases

Columnar databases store/read data column-wise.

They are excellent for analytics because analytics often reads a few columns across many rows.

Example query:

```text
Calculate average order amount for the last 6 months.
```

You may only need:

```text
order_date
amount
region
```

A columnar database can read only those columns efficiently.

### Good for

- analytics,
- reporting,
- dashboards,
- data warehouses,
- aggregation-heavy workloads.

### Example tools

- BigQuery,
- Redshift,
- ClickHouse,
- Cassandra is wide-column oriented.

### Trade-off

Often less ideal for frequent transactional row updates.

---

## Graph databases

Graph databases model entities as nodes and relationships as edges.

Example:

```text
Person → follows → Person
User → bought → Product
Student → enrolled_in → Course
```

Both nodes and relationships can have properties.

Example:

```text
Student --enrolled_in {year: 2026, status: active}--> Course
```

### Good for

- social networks,
- recommendation systems,
- fraud detection,
- relationship-heavy queries,
- dependency graphs,
- knowledge graphs.

### Example tools

- Neo4j,
- Amazon Neptune.

### Trade-off

Graph traversal can become complex and requires specialized modeling.

## Decision table

| NoSQL Type | Best For | Example |
|---|---|---|
| Key-value | Fast lookup/cache/session | Redis |
| Document | Flexible JSON-like data | MongoDB / Firestore |
| Columnar | Analytics and aggregations | BigQuery / Redshift |
| Graph | Relationship-heavy data | Neo4j |

## Mid-level engineer takeaway

When someone says “use NoSQL,” ask:

```text
Which NoSQL model fits the access pattern?
```

That question separates a strong engineer from someone using buzzwords.

---

# 24. Cache

## What cache is

Cache is fast temporary storage used to avoid repeatedly doing expensive work.

Most commonly, cache stores frequently accessed data closer to where it is needed.

```text
Client → Backend → Cache → Database
```

If cache has the data, backend can respond quickly.

If cache does not have the data, backend fetches from database.

## Why cache is needed

Imagine a homepage shows the same six popular courses to every user.

Without cache:

```text
Every user → backend → multiple DB queries → response
```

With cache:

```text
Most users → backend → cache → response
```

This reduces:

- database load,
- response latency,
- infrastructure cost,
- risk of DB overload.

## Cache hit

A cache hit means requested data is found in cache.

```text
Request → Cache → Found → Fast response
```

## Cache miss

A cache miss means requested data is not found in cache.

```text
Request → Cache → Not found → Database → Response
```

Cache miss is slower because backend must query the original data source.

## TTL

TTL means Time To Live.

It defines how long cached data remains valid.

Example:

```text
homepage:courses TTL = 5 minutes
```

After 5 minutes, the cache entry expires and needs refresh.

## Why not cache everything?

Because cache is intentionally smaller than the database.

Cache is fast because:

- it stores limited hot data,
- it often lives in memory,
- lookups are optimized,
- data access is simpler.

If you copy the entire database into cache, you lose the benefit and increase cost/complexity.

## What data is good for caching?

Good candidates:

- homepage content,
- product catalog data,
- user profile summary,
- configuration values,
- frequently accessed course details,
- read-heavy data,
- expensive computed results.

Poor candidates:

- highly sensitive data,
- rapidly changing critical data,
- bank balance without strict invalidation,
- one-time data rarely reused,
- data that must always be strongly consistent.

## Client-side cache

Data cached on the client.

Examples:

- browser cache,
- Android Room cache,
- DataStore/shared preferences,
- image cache,
- HTTP cache.

Useful for:

- offline mode,
- faster UI loading,
- reducing network usage.

## Server-side cache

Data cached near backend services.

Examples:

- Redis,
- Memcached,
- in-memory application cache.

Useful for:

- shared hot data,
- repeated database query results,
- rate limiting,
- sessions,
- computed summaries.

## Database cache

Databases may internally cache query plans, pages, or frequently accessed data.

You may not directly manage this like Redis, but it affects performance.

## Cache invalidation

The hardest part of caching is not storing data. It is knowing when cached data becomes wrong.

Example:

```text
Product price changed in DB.
Cache still has old price.
User sees wrong price.
```

Possible solutions:

- short TTL,
- explicit invalidation after update,
- write-through strategy,
- event-based invalidation,
- versioned cache keys.

## Cache example for Spring Boot

Simple mental model:

```text
GET /courses/popular
  ↓
Check Redis key: popular_courses
  ↓
If found: return cached data
  ↓
If missing: query DB, store in Redis with TTL, return response
```

Pseudo-code:

```java
public List<Course> getPopularCourses() {
    List<Course> cached = redis.get("popular_courses");
    if (cached != null) {
        return cached;
    }

    List<Course> courses = courseRepository.findPopularCourses();
    redis.set("popular_courses", courses, Duration.ofMinutes(5));
    return courses;
}
```

## Cache example for Android

For Android apps, local cache improves UX.

Example:

```text
Open app
  ↓
Show last saved Room data immediately
  ↓
Fetch latest data from API
  ↓
Update local DB
  ↓
UI refreshes
```

This is common in offline-first or poor-network apps.

## Important metrics

- cache hit ratio,
- cache miss ratio,
- eviction count,
- memory usage,
- stale data incidents,
- average response time with cache,
- average response time without cache.

## Mid-level engineer takeaway

Cache is not just “make it faster.” Cache is a trade-off between speed and freshness.

Interview framing:

```text
I would cache read-heavy and relatively stable data using Redis with TTL. For critical data like payment status or account balance, I would be careful with stale reads and use explicit invalidation or avoid caching unless the consistency model is clear.
```

---

# Part 1 Revision Summary

## Core mental models you should remember

```text
System design = engineering trade-offs under scale and failure.
```

```text
Functional requirements define what to build.
Non-functional requirements define how well it must behave.
```

```text
Data-intensive systems scale around storage, reads, writes, indexing, caching, replication, and consistency.
```

```text
Compute-intensive systems scale around CPU/GPU, algorithms, parallelism, queues, and worker capacity.
```

```text
APIs are contracts, not just URLs.
```

```text
SQL is strong for structured, relational, transactional consistency.
NoSQL is strong for flexible, high-scale, access-pattern-specific data.
```

```text
Cache improves speed by storing hot data, but introduces freshness and invalidation challenges.
```

---

# Part 1 Interview Practice Questions

Use these after reading the file.

## Foundation

1. What is system design, and how is it different from writing code?
2. Explain vertical scaling and horizontal scaling using a real-world analogy.
3. Why is a load balancer needed after horizontal scaling?
4. What problem does a centralized/shared database solve?

## Requirements

5. What is the difference between functional and non-functional requirements?
6. Give functional and non-functional requirements for an e-commerce app.
7. Why do non-functional requirements influence architecture more heavily than feature lists?

## Data vs Compute

8. How do you identify if a workload is data-intensive?
9. How do you identify if a workload is compute-intensive?
10. Why is YouTube both data-intensive and compute-intensive depending on the feature?

## DNS and APIs

11. Explain DNS resolution step by step.
12. What is an API and why should clients not directly access the database?
13. Compare REST, GraphQL, gRPC, and WebSockets.
14. When would you use WebSockets instead of REST?

## REST

15. Difference between PUT and PATCH?
16. Difference between 401 and 403?
17. Why should API responses often be wrapped in an object instead of returning raw arrays?
18. When should you use query parameters vs path parameters?

## Databases

19. When would you choose SQL over NoSQL?
20. What are primary keys and foreign keys?
21. Explain one-to-many and many-to-many relationships.
22. Why do many-to-many relationships need a junction table?
23. What are the types of NoSQL databases?

## Cache

24. What is a cache hit and cache miss?
25. What is TTL?
26. Why should we not cache everything?
27. What type of data is safe to cache?
28. What are the risks of caching payment or bank balance data?

---

# Part 1 Mini Design Exercise

Design a simple **course learning platform homepage**.

Requirements:

- show popular courses,
- show course price,
- show discount price,
- allow user login,
- allow course search,
- handle 10,000 daily users,
- homepage should load quickly.

Suggested design:

```text
Browser / Android App
  ↓
REST API
  ↓
Backend Service
  ↓          ↓
Redis Cache  SQL Database
```

Reasoning:

- User/course/payment data can be SQL.
- Popular homepage course list can be cached.
- Search can use DB index or search engine later.
- Price changes need cache invalidation.
- Login should not expose password in URL.
- API responses should use proper status codes.

This exercise combines almost everything from Part 1.

---

# End of Part 1

You are now ready for Part 2 topics:

- Cache strategies,
- Cache eviction policies,
- Load balancing algorithms,
- Distributed databases,
- Replication,
- Partitioning / sharding,
- CAP theorem,
- Message queues,
- Pub/Sub,
- Fault tolerance,
- Monitoring,
- Streaming system design.
