# System Design Tutorial — Part 2

## Engineer-Explained Notes for Mid-Level Developers

This is **Part 2** of the two-part system design tutorial. It continues from **Cache Strategies** and moves into the more production-grade distributed-system topics: cache eviction, load balancing, replication, partitioning, CAP theorem, message queues, fault tolerance, observability, and a final streaming-application design.

This is **not a transcript**. It is a rewritten learning guide in an engineer-friendly style, designed to make you confident enough to discuss these topics in interviews and apply them in backend/mobile projects.

---

## How to use this Part 2 guide

Read this part with one mindset:

> System design is not about naming tools. It is about understanding pressure points and choosing trade-offs deliberately.

For every topic, ask:

1. What problem is this solving?
2. What happens if we do not use it?
3. What trade-off does it introduce?
4. Where would I use it in a real project?
5. How would I explain it in an interview?

For a mid-level engineer, confidence comes from being able to say:

> “I would choose this approach because the workload has this bottleneck, and I understand the downside.”

That is the level this guide targets.

---

# Part 2 Roadmap

This file covers:

| Chapter | Topic |
|---:|---|
| 25 | Cache Strategies |
| 26 | Cache Eviction Policies |
| 27 | Load Balancer |
| 28 | Load Balancing Algorithms |
| 29 | Load Balancer Health Checks |
| 30 | Distributed Databases |
| 31 | Replication |
| 32 | Replication Algorithms |
| 33 | Partitioning |
| 34 | Partition Strategies |
| 35 | CAP Theorem |
| 36 | Message Queue |
| 37 | Pub/Sub Model |
| 38 | Fault / Error |
| 39 | Monitoring and Observability |
| 40 | API Handling Monitoring |
| 41 | Machine Monitoring |
| 42 | System Design of Streaming Application |
| 43 | Thank You / Final Mindset |

---

# 25. Cache Strategies

## Core idea

A cache is a fast storage layer placed between your application and your slower source of truth, usually a database or remote service.

But just saying “use Redis” is not system design. The real question is:

> When should data enter the cache, when should it leave, and who is responsible for keeping the cache and database in sync?

That is what cache strategies answer.

A database is usually the **source of truth**. A cache is usually a **performance optimization**. The danger begins when people forget this and start treating cache like permanent storage without thinking about consistency.

---

## The basic cache flow

A typical read flow looks like this:

```text
Client
  ↓
Backend API
  ↓
Cache
  ↓ if cache miss
Database
```

A write flow depends on the strategy:

```text
Client
  ↓
Backend API
  ↓
Cache and/or Database
```

The hard part is writes, because writes create consistency concerns.

---

## Strategy 1: Read-Through Cache

### What it means

In read-through caching, the application asks the cache for data first. If the data is present, the cache returns it. If not, the cache fetches it from the database, stores it, and returns it.

```text
Read request
  ↓
Check cache
  ├── hit  → return data
  └── miss → fetch DB → store in cache → return data
```

### Where it fits

Use read-through cache when:

- reads are frequent,
- the same data is requested repeatedly,
- the data does not change every second,
- latency matters.

Examples:

- product details,
- user profile summary,
- course landing page content,
- blog post metadata,
- category lists,
- app configuration.

### Real backend example

In a Spring Boot app:

```java
public ProductDto getProduct(Long id) {
    String key = "product:" + id;

    ProductDto cached = redis.get(key, ProductDto.class);
    if (cached != null) {
        return cached;
    }

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found"));

    ProductDto dto = mapper.toDto(product);
    redis.set(key, dto, Duration.ofMinutes(10));
    return dto;
}
```

### Trade-off

Read-through improves read latency but can return stale data if the database changes and the cache is not invalidated.

### Interview wording

> “For read-heavy endpoints, I would use read-through caching. The API first checks Redis; on cache miss, it fetches from the DB, stores the result with a TTL, and returns it. I would also define invalidation rules for updates so stale data does not live too long.”

---

## Strategy 2: Write-Through Cache

### What it means

In write-through caching, every write goes to the cache and database synchronously.

```text
Write request
  ↓
Write to cache
  ↓
Write to database
  ↓
Return success only after both succeed
```

The important part is: the request is not considered successful until the database is also updated.

### Why this exists

It keeps cache and database aligned. If a user updates something, the next read from cache should already have the new value.

### Where it fits

Use write-through when:

- correctness is important,
- stale reads are risky,
- write volume is manageable,
- latency impact is acceptable.

Examples:

- account profile updates,
- subscription plan changes,
- stock item metadata,
- settings/preferences.

### Trade-off

Write-through gives better consistency but slower writes because every write waits for both cache and DB operations.

### Mistake to avoid

Do not use write-through blindly for extremely high write traffic. If thousands of write events are happening per second, waiting for database confirmation on every single update can become a throughput bottleneck.

### Interview wording

> “If read freshness matters and writes are not extremely high-volume, I would choose write-through cache. It keeps the cache updated whenever the DB is updated, but I would call out the write-latency cost.”

---

## Strategy 3: Write-Around Cache

### What it means

In write-around caching, writes go directly to the database and skip the cache.

```text
Write request
  ↓
Database only

Read request
  ↓
Cache
  └── miss → Database → populate cache
```

The cache is populated only during reads.

### Why this exists

Not every written value is read immediately. If you cache every write, the cache can get filled with data nobody asks for. Write-around keeps cache clean and focused on actually-read data.

### Where it fits

Use write-around when:

- there are many writes,
- only a fraction of written data is read later,
- cache pollution is a concern,
- read-after-write freshness is not critical.

Examples:

- activity logs,
- audit events,
- historical transaction records,
- analytics events,
- app telemetry.

### Trade-off

First read after write may be slower because the cache does not yet contain the updated data.

### Interview wording

> “For write-heavy data that may not be read immediately, I would use write-around caching. Writes go to DB directly, and the cache fills only on demand. This reduces cache pollution but makes the first read slower.”

---

## Strategy 4: Write-Back / Write-Behind Cache

### What it means

In write-back caching, the application writes to the cache first. The cache later writes to the database asynchronously.

```text
Write request
  ↓
Write to cache
  ↓
Return success quickly
  ↓ later
Async flush to database
```

This is very fast for users because the system does not wait for the database before responding.

### Where it fits

Use write-back when:

- write speed is critical,
- temporary inconsistency is acceptable,
- you can recover from delayed writes,
- the system has strong retry and durability mechanisms.

Examples:

- food delivery order status updates,
- ride tracking location updates,
- game score updates,
- live counters,
- analytics buffers,
- high-frequency inventory counters with eventual reconciliation.

### Why the course example matters

The course connects write-back with high-write traffic such as delivery apps where order status changes quickly. Instead of writing every tiny state change immediately to the database, the system can keep hot state in cache and persist asynchronously.

### The biggest risk

If the cache crashes before flushing to the database, data may be lost unless the cache is durable or backed by a queue/log.

### Better production pattern

For serious systems, combine write-back with a durable queue:

```text
API
  ↓
Cache update for fast reads
  ↓
Durable queue/event log
  ↓
Database writer worker
  ↓
Database
```

This way, even if a worker fails, the event can be replayed.

### Interview wording

> “For high-write workloads where small delays in persistence are acceptable, I would consider write-back caching. I would not rely only on volatile cache; I would pair it with a durable queue or log to avoid data loss.”

---

## Cache strategy comparison

| Strategy | Read speed | Write speed | Consistency | Best for |
|---|---:|---:|---:|---|
| Read-through | Fast after warm-up | Normal | Medium | Read-heavy data |
| Write-through | Fast | Slower | Stronger | Fresh reads after writes |
| Write-around | Fast after first read | Fast | Medium | Write-heavy data not always read |
| Write-back | Very fast | Very fast | Eventual | High-write workloads |

---

## How to choose in real life

Ask these questions:

1. Is this data read more than written?
2. Is stale data acceptable?
3. How much write latency can we tolerate?
4. Can we lose cached data?
5. Does the business require immediate consistency?

### Practical examples

| Scenario | Good strategy |
|---|---|
| Product listing page | Read-through |
| User profile update | Write-through or cache invalidation |
| Audit logs | Write-around |
| Delivery order live status | Write-back with durable queue |
| Course homepage content | Read-through with TTL |
| Payment ledger | Avoid cache as source of truth; DB-first |

---

# 26. Cache Eviction Policies

## Core idea

Cache has limited memory. You cannot store everything forever. Eviction policies decide what to remove when the cache is full.

The real business question is:

> Which data is least valuable to keep in memory right now?

---

## LRU — Least Recently Used

### Meaning

Remove the data that has not been accessed recently.

```text
Cache full → remove item with oldest access time
```

### Example

If users are searching mostly for the latest iPhone, old iPhone models may be evicted because they are not recently accessed.

### Best for

- product pages,
- blog pages,
- course pages,
- profile data,
- general web caching.

### Strength

Simple and effective for many real-world workloads.

### Weakness

A sudden one-time scan can pollute the cache. For example, if a crawler reads thousands of old pages once, LRU may evict actually useful hot data.

---

## LFU — Least Frequently Used

### Meaning

Remove the data accessed the least number of times.

```text
Cache full → remove item with lowest access count
```

### Best for

- content platforms,
- recommendation pages,
- repeated popular resources,
- frequently accessed configuration.

### Strength

Keeps consistently popular data.

### Weakness

Old popular data may stay too long even after it becomes irrelevant unless frequency counters decay over time.

---

## FIFO — First In, First Out

### Meaning

Remove the item that entered the cache first.

```text
Cache full → remove oldest inserted item
```

### Best for

- simple systems,
- predictable stream processing,
- low-complexity caching.

### Strength

Easy to implement.

### Weakness

It does not care whether the item is popular. A very useful item can be removed simply because it was inserted early.

---

## MRU — Most Recently Used

### Meaning

Remove the most recently used item.

At first this sounds strange. Why remove the item just used?

It works in specific workloads where recently accessed data is unlikely to be used again soon.

### Best for

- one-time sequential scans,
- certain database buffer workloads,
- workloads where old context is more useful than latest access.

### Weakness

Bad fit for normal web/API caching where recently used data is usually likely to be used again.

---

## LIFO — Last In, First Out

### Meaning

Remove the newest inserted item first.

### Best for

Rarely used in common application caching, but may appear in stack-like processing or temporary buffers.

### Weakness

Can remove fresh data before it has any chance to be reused.

---

## TTL vs eviction

TTL and eviction are different.

| Concept | Meaning |
|---|---|
| TTL | Remove data after time expires |
| Eviction | Remove data because cache needs space |

You often use both:

```text
Cache entry
  ├── expires after 10 minutes due to TTL
  └── may be removed earlier if memory is full
```

---

## Practical recommendation

For most backend projects, start with:

- Redis,
- TTL,
- LRU-like eviction,
- explicit invalidation on writes,
- metrics for hit ratio.

Do not over-engineer eviction before measuring workload.

---

## Interview wording

> “Cache eviction is required because cache memory is limited. For general web workloads, LRU with TTL is a strong default. If the workload has long-term popular items, LFU may work better. The choice depends on access pattern, not just tool preference.”

---

# 27. Load Balancer

## Core idea

A load balancer distributes incoming traffic across multiple backend servers.

Without a load balancer:

```text
Client → Server
```

With a load balancer:

```text
Client
  ↓
Load Balancer
  ├── Server 1
  ├── Server 2
  └── Server 3
```

The load balancer is the traffic manager of your system.

---

## Why load balancers are needed

You need a load balancer when:

- one server is not enough,
- you want high availability,
- you want zero-downtime deployments,
- some servers may fail,
- traffic must be distributed fairly,
- you want to route users to the closest region.

A load balancer does two major jobs:

1. **Distribution** — send requests to multiple servers.
2. **Health awareness** — avoid sending traffic to dead/unhealthy servers.

---

## Layer 4 vs Layer 7 load balancing

### Layer 4

Works at TCP/UDP level.

It routes based on:

- IP,
- port,
- connection-level data.

It is fast but less aware of application details.

### Layer 7

Works at HTTP/application level.

It can route based on:

- path,
- headers,
- cookies,
- host/domain,
- HTTP method.

Example:

```text
/api/users     → User Service
/api/orders    → Order Service
/api/payments  → Payment Service
```

### Interview wording

> “Layer 4 load balancers are faster and operate at transport level. Layer 7 load balancers understand HTTP and can route based on paths, headers, or hostnames. For microservices APIs, Layer 7 routing is often useful.”

---

## Load balancer as a reliability layer

The load balancer should know whether a server is healthy.

```text
If Server 2 fails:

Client
  ↓
Load Balancer
  ├── Server 1 ✅
  ├── Server 2 ❌ ignored
  └── Server 3 ✅
```

This is why health checks are critical.

---

## Real-world examples

- AWS Elastic Load Balancer,
- Nginx,
- HAProxy,
- Cloudflare Load Balancing,
- Kubernetes Service / Ingress,
- Envoy.

---

## Spring Boot deployment example

Imagine three Spring Boot instances:

```text
appointment-api-1:8080
appointment-api-2:8080
appointment-api-3:8080
```

Nginx can route requests:

```nginx
upstream appointment_api {
    server appointment-api-1:8080;
    server appointment-api-2:8080;
    server appointment-api-3:8080;
}

server {
    listen 80;

    location /api/ {
        proxy_pass http://appointment_api;
    }
}
```

The client only knows one URL. The load balancer hides the backend complexity.

---

# 28. Load Balancing Algorithms

## Core idea

A load balancer must decide:

> Which server should handle the next request?

That decision is made using a load balancing algorithm.

---

## 1. Round Robin

### Meaning

Send requests one by one to each server in order.

```text
Request 1 → Server A
Request 2 → Server B
Request 3 → Server C
Request 4 → Server A
```

### Best for

- servers with similar capacity,
- requests with similar cost,
- simple stateless services.

### Weakness

It assumes all requests and servers are equal. In reality, one request may be heavier than another, and one server may be weaker.

---

## 2. Least Connections

### Meaning

Send the next request to the server with the fewest active connections.

```text
Server A: 50 active connections
Server B: 20 active connections
Server C: 70 active connections

Next request → Server B
```

### Best for

- long-lived connections,
- WebSocket servers,
- streaming APIs,
- uneven request duration.

### Weakness

Connection count does not always equal CPU usage. A server with fewer connections might still be handling heavier work.

---

## 3. Weighted Round Robin

### Meaning

Give more traffic to stronger servers.

```text
Server A weight 5
Server B weight 3
Server C weight 1
```

Server A receives more traffic than Server C.

### Best for

- mixed server sizes,
- gradual migration,
- canary deployments,
- handling stronger instances differently.

### Example

If one instance has 8 CPU cores and another has 2 CPU cores, they should not receive equal traffic.

---

## 4. IP Hash

### Meaning

Hash the client IP and route the same client to the same server.

```text
hash(client_ip) % server_count → selected server
```

### Best for

- session affinity,
- stateful applications,
- apps where user context is stored locally.

### Weakness

If a server is added or removed, many users may get remapped unless consistent hashing is used.

### Better design

Prefer stateless servers and store session data in Redis/database when possible. Then you do not depend heavily on sticky sessions.

---

## 5. Geo-Based Routing

### Meaning

Route users to the closest region.

```text
India user     → Mumbai / Hyderabad region
Europe user    → Frankfurt / Ireland region
US user        → Virginia / Oregon region
```

### Best for

- global products,
- video streaming,
- low-latency APIs,
- content delivery,
- regional compliance.

### Weakness

Geo-routing introduces complexity:

- data replication across regions,
- compliance issues,
- region failover planning,
- consistency challenges.

---

## Algorithm selection table

| Algorithm | Best when | Avoid when |
|---|---|---|
| Round Robin | Equal servers, simple stateless APIs | Request cost varies heavily |
| Least Connections | Long connections, WebSockets | CPU cost is unrelated to connection count |
| Weighted Round Robin | Different server capacities | Weights are not maintained properly |
| IP Hash | Sticky sessions needed | You can build stateless servers |
| Geo Routing | Global users | Single-region app is enough |

---

## Interview wording

> “I would start with round robin for stateless equal-capacity services. If connections are long-lived, I would prefer least connections. If server capacity differs, I would use weighted round robin. For global traffic, I would add geo-based routing.”

---

# 29. Load Balancer Health Checks

## Core idea

A load balancer should not blindly send traffic to every registered server. It must continuously check whether each server is healthy.

---

## What is a health check?

A health check is a small request made by the load balancer to confirm that the server can handle traffic.

Example endpoint:

```http
GET /actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

---

## Basic health check vs deep health check

### Basic health check

Only checks if the process is alive.

```text
Is the server running? Yes/No
```

### Deep health check

Checks if the server can actually serve real traffic.

It may verify:

- database connectivity,
- Redis connectivity,
- message broker connectivity,
- disk space,
- dependency status,
- thread pool pressure.

---

## Liveness vs readiness

### Liveness

Answers:

> Is the application alive, or should it be restarted?

### Readiness

Answers:

> Is the application ready to receive traffic?

A service can be alive but not ready.

Example:

```text
Spring Boot app started
Database migration still running
Cache warming in progress

Alive: yes
Ready: no
```

---

## Why this matters in deployments

During deployment, you do not want users hitting a half-started server.

Good rollout:

```text
Start new instance
  ↓
Wait until readiness is healthy
  ↓
Add to load balancer
  ↓
Drain old instance
  ↓
Remove old instance
```

This enables zero-downtime deployment.

---

## Common mistake

Do not make health checks too heavy.

Bad health check:

```text
Runs 10 DB queries
Calls 5 third-party services
Performs expensive computation
```

This can create load just from monitoring.

Good health check:

```text
Fast, shallow enough, but meaningful.
```

---

## Spring Boot tip

Use Actuator:

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.probes.enabled=true
```

Then use:

```text
/actuator/health/liveness
/actuator/health/readiness
```

---

## Interview wording

> “Health checks help the load balancer avoid unhealthy instances. I would separate liveness and readiness. Liveness tells whether the process should be restarted, while readiness tells whether it should receive traffic.”

---

# 30. Distributed Databases

## Core idea

A distributed database stores data across multiple machines instead of one machine.

Why?

Because one database server eventually hits limits:

- storage limit,
- CPU limit,
- memory limit,
- network limit,
- availability risk.

---

## Single database problem

```text
App → One Database
```

This is easy, but it creates a single point of failure.

If the DB is down:

```text
App → DB ❌ → System fails
```

If data grows too large:

```text
DB becomes slow
Backups become slow
Indexes become huge
Queries become expensive
```

---

## Distributed database model

```text
App
 ↓
DB Cluster
 ├── Node A
 ├── Node B
 └── Node C
```

Each node may hold:

- a copy of the same data, or
- a subset of the data, or
- both.

That leads to two major ideas:

1. **Replication** — copy data.
2. **Partitioning/Sharding** — split data.

---

## Why distributed databases are hard

Once data is on multiple machines, you must think about:

- network failures,
- replication delay,
- stale reads,
- conflict resolution,
- distributed transactions,
- clock differences,
- consistency vs availability,
- operational complexity.

This is why distributed systems are not just “add more servers.”

---

## When to move to distributed databases

Do not start there unless needed.

Move when:

- one DB cannot handle traffic,
- data size is too large,
- high availability is required,
- global access latency matters,
- read replicas are needed,
- regulatory/geographic data placement matters.

---

## Interview wording

> “A distributed database helps scale storage and improve availability by spreading data across multiple nodes. But it adds complexity around replication, consistency, failures, and operational overhead. I would move there only when a single-node or primary-replica setup is no longer enough.”

---

# 31. Replication

## Core idea

Replication means keeping copies of data on multiple database nodes.

```text
Primary DB → Replica DB 1
           → Replica DB 2
```

The purpose is to improve:

- availability,
- read scalability,
- disaster recovery,
- geographic performance.

---

## Why replication exists

### 1. High availability

If one database node fails, another copy exists.

### 2. Read scalability

Reads can be served from replicas.

```text
Writes → Primary
Reads  → Replicas
```

### 3. Disaster recovery

If a region fails, another region may have a copy.

---

## Read replica example

```text
App write request
  ↓
Primary DB

App read request
  ↓
Read Replica
```

This reduces pressure on the primary DB.

---

## Replication lag

Replication is not always instant.

```text
Write happens on primary at 10:00:00
Replica receives update at 10:00:02
```

During those 2 seconds, users reading from replica may see old data.

This is called **replication lag**.

---

## Read-your-write problem

A user updates their profile and immediately refreshes the page.

If the read goes to a lagging replica, they may not see their update.

Solution options:

- read from primary immediately after user write,
- route user to same region/replica after write,
- use session consistency,
- show optimistic UI and reconcile,
- tolerate eventual consistency for non-critical data.

---

## Interview wording

> “Replication improves availability and read scalability, but introduces replication lag. For user-visible write-after-read cases, I would either read from primary briefly or design session consistency so users see their own updates.”

---

# 32. Replication Algorithms

## 1. Single-Leader Replication

### Meaning

One node accepts writes. Other nodes replicate from it.

```text
Writes → Leader
          ↓
       Followers

Reads → Leader or Followers
```

### Best for

- most traditional applications,
- relational databases,
- clear write authority,
- simpler conflict handling.

### Strength

Easy to reason about. Since all writes go through one leader, conflicts are easier to avoid.

### Weakness

The leader can become a bottleneck. If the leader fails, failover is needed.

### Example

Common primary-replica MySQL/PostgreSQL setups.

---

## 2. Multi-Leader Replication

### Meaning

Multiple nodes can accept writes. They replicate changes to each other.

```text
Region A Leader ↔ Region B Leader ↔ Region C Leader
```

### Best for

- multi-region applications,
- local writes in different geographic regions,
- offline-first or distributed environments.

### Strength

Users can write to a nearby region, reducing latency.

### Weakness

Conflicts can happen.

Example:

```text
User name changed in Region A to "Vishnu"
User name changed in Region B to "Vishnu S"
Both updates happen before sync
```

Now the system must decide which wins.

### Conflict handling options

- last write wins,
- version vectors,
- manual merge,
- field-level merge,
- business-rule-based merge.

---

## 3. Leaderless Replication

### Meaning

There is no single leader. Writes can go to multiple nodes, and reads also read from multiple nodes.

```text
Client writes to N nodes
Client reads from N nodes
System uses quorum to decide correctness
```

This is common in systems inspired by Dynamo-style architectures.

---

## Quorum

Quorum means the system waits for a minimum number of nodes to acknowledge reads/writes.

Variables:

```text
N = total replicas
W = number of write acknowledgements required
R = number of read acknowledgements required
```

A common rule:

```text
R + W > N
```

This increases the chance that reads see latest writes.

### Example

```text
N = 3
W = 2
R = 2

R + W = 4 > 3
```

This means at least one read node overlaps with a written node.

---

## Sync vs Async Replication

### Synchronous replication

Write is considered successful only after replicas confirm.

```text
Write → Primary → Replica confirms → Success
```

Stronger consistency, slower writes.

### Asynchronous replication

Primary returns success before replicas confirm.

```text
Write → Primary → Success
               ↓ later
             Replica
```

Faster writes, possible data loss/stale reads during failure.

---

## Replication strategy comparison

| Strategy | Strength | Weakness | Best for |
|---|---|---|---|
| Single leader | Simple, consistent writes | Leader bottleneck | Most CRUD apps |
| Multi leader | Lower write latency globally | Conflicts | Multi-region writes |
| Leaderless | High availability | Complex reads/conflicts | Distributed high-scale systems |
| Sync replication | Stronger durability | Higher latency | Critical writes |
| Async replication | Faster writes | Lag/data loss risk | Read scaling, non-critical flows |

---

## Interview wording

> “For most systems, I would start with single-leader replication because it is simpler and predictable. If we need multi-region writes, I would consider multi-leader replication but must handle conflicts. For highly available distributed databases, leaderless replication with quorum can work, but it adds operational complexity.”

---

# 33. Partitioning / Sharding

## Core idea

Partitioning means splitting a large dataset into smaller pieces and storing those pieces across different nodes.

```text
All users in one database ❌

Users A-F → Shard 1
Users G-M → Shard 2
Users N-Z → Shard 3
```

The goal is to scale storage and traffic.

---

## Replication vs partitioning

| Concept | Meaning |
|---|---|
| Replication | Copy same data to multiple nodes |
| Partitioning | Split data across nodes |

They are often used together.

```text
Shard 1 → Replica A, Replica B
Shard 2 → Replica A, Replica B
Shard 3 → Replica A, Replica B
```

---

## Why partitioning exists

One database may become too large:

- too many rows,
- indexes too large,
- writes too heavy,
- queries too slow,
- backups too expensive.

Partitioning reduces the blast radius by spreading data.

---

## Simple example

Suppose you have 100 million users.

Instead of one huge users table, you split by user ID:

```text
user_id 1-10M      → DB 1
user_id 10M-20M    → DB 2
user_id 20M-30M    → DB 3
```

Now each database handles a smaller slice.

---

## The routing problem

Once data is partitioned, the application must know where to find data.

```text
Request: get user 928372
Question: which shard has this user?
```

You need a routing strategy.

---

## Interview wording

> “Partitioning splits data across nodes so a single database does not carry all storage and traffic. It improves scale, but adds routing complexity, cross-shard query problems, and hotspot risk.”

---

# 34. Partition Strategies

## 1. Partition by Key / Range Partitioning

### Meaning

Split data based on key ranges.

```text
user_id 1-1000      → Partition A
user_id 1001-2000   → Partition B
user_id 2001-3000   → Partition C
```

### Strength

Easy to understand and query by range.

### Weakness

Can create hotspots.

Example:

If new user IDs always increase, the latest partition receives most writes.

```text
New users always go to highest ID range → last shard overloaded
```

---

## 2. Partition by Hash

### Meaning

Hash the key and use the hash to choose a partition.

```text
partition = hash(user_id) % number_of_partitions
```

### Strength

Distributes data more evenly.

### Weakness

Range queries become harder.

Example:

Finding users created between two dates may require checking many partitions.

---

## 3. Consistent Hashing

### Meaning

Consistent hashing reduces remapping when nodes are added or removed.

In normal modulo hashing:

```text
hash(key) % 3
```

If you add one more node:

```text
hash(key) % 4
```

Many keys move.

Consistent hashing tries to move only a smaller subset of keys.

### Best for

- distributed caches,
- NoSQL databases,
- systems where nodes are added/removed dynamically.

---

## 4. Secondary Index Partitioning

### Meaning

Primary data may be partitioned by one key, but queries may need another field.

Example:

```text
Users partitioned by user_id
But query asks: find users by city = Bangalore
```

Now you need secondary indexes.

### Challenge

The index itself may need partitioning.

Options:

1. Local secondary index — each shard indexes its own data.
2. Global secondary index — separate index across all shards.

### Trade-off

| Index type | Strength | Weakness |
|---|---|---|
| Local index | Easier writes | Query may hit all shards |
| Global index | Faster lookup | Harder writes/consistency |

---

## 5. Hotspots

A hotspot happens when one partition receives much more traffic than others.

Example:

```text
Celebrity user posts update
Millions of followers read it
One shard gets overloaded
```

Or:

```text
All latest orders go to one date-based partition
```

### Solutions

- hash-based distribution,
- random suffixes,
- split hot partitions,
- cache hot keys,
- replicate hot data,
- rate limit abusive access,
- precompute fanout for feeds.

---

## Choosing a partition key

A good partition key should:

- distribute traffic evenly,
- match common query patterns,
- avoid hotspots,
- avoid too many cross-partition joins,
- support future growth.

### Examples

| System | Possible partition key |
|---|---|
| Chat app | conversation_id |
| E-commerce orders | customer_id or order_id |
| Payment ledger | account_id |
| Video platform | video_id |
| Inventory app | owner_id / group_id |
| Notifications | user_id |

---

## Interview wording

> “I would choose the partition key based on access patterns. Hash partitioning gives even distribution, while range partitioning supports range queries but can create hotspots. I would also call out secondary indexes and cross-shard query complexity.”

---

# 35. CAP Theorem

## Core idea

CAP theorem says that in a distributed system, when a network partition happens, you must choose between consistency and availability.

CAP stands for:

- **C — Consistency**
- **A — Availability**
- **P — Partition Tolerance**

---

## First: understand partition tolerance

A partition means nodes cannot communicate due to network failure.

```text
Node A  ❌ network broken ❌  Node B
```

In distributed systems, network failure is not optional. It will happen.

So in practice, you do not choose whether to have partition tolerance. You must tolerate partitions.

The real trade-off becomes:

> During a partition, do we prefer consistency or availability?

---

## Consistency

Every read receives the latest correct data.

Example:

If account balance is updated from ₹10,000 to ₹8,000, every read should show ₹8,000.

During a partition, a consistency-focused system may block reads/writes until it can guarantee correctness.

### Good for

- banking,
- payments,
- inventory reservation,
- ticket booking,
- wallet balances,
- order payment state.

---

## Availability

Every request receives a response, even if it may not contain the latest data.

Example:

A social media like count may show 999 instead of 1000 for a few seconds.

### Good for

- feeds,
- likes,
- comments count,
- recommendations,
- analytics dashboards,
- video views.

---

## CAP examples

### Banking transfer

Prefer consistency.

```text
Do not allow double spending.
Do not show incorrect balance.
Temporary unavailability is acceptable.
```

### Instagram feed

Prefer availability.

```text
Show slightly stale feed.
Do not block user experience.
Eventually sync latest posts.
```

### Video streaming

Prefer availability for playback.

```text
Keep video playing.
Quality can reduce.
Metadata can be slightly stale.
```

---

## Important clarification

CAP is not saying you can only ever have two of three forever.

It says that **when a network partition occurs**, you must choose between consistency and availability.

When the network is healthy, systems try to provide both.

---

## CP vs AP systems

| Type | Chooses | Behavior during partition |
|---|---|---|
| CP | Consistency + Partition tolerance | May reject/timeout requests |
| AP | Availability + Partition tolerance | Responds with possibly stale data |

---

## Interview wording

> “In CAP theorem, partition tolerance is mandatory in distributed systems because networks fail. During a partition, we choose between consistency and availability. For payments I would choose consistency; for feeds or counters I would often choose availability with eventual consistency.”

---

# 36. Message Queue

## Core idea

A message queue lets services communicate asynchronously.

Instead of Service A directly waiting for Service B:

```text
Service A → Service B
```

Use a queue:

```text
Service A → Queue → Service B
```

This decouples services and improves resilience.

---

## Why queues exist

Queues solve several production problems:

1. Slow downstream services.
2. Traffic spikes.
3. Retry handling.
4. Decoupling microservices.
5. Background processing.
6. Better user response time.

---

## Synchronous vs asynchronous

### Synchronous

The caller waits for the callee.

```text
Order Service → Payment Service → wait → response
```

Good when immediate answer is required.

### Asynchronous

The caller publishes work and moves on.

```text
Order Service → Queue → Email Service later
```

Good when work can happen after the main request.

---

## E-commerce example

When a user places an order:

Immediate critical path:

```text
Validate cart
Reserve inventory
Create order
Take payment
Return order confirmation
```

Async background work:

```text
Send email
Send SMS
Generate invoice
Update analytics
Notify seller
Trigger recommendation update
```

Do not make the user wait for every background task.

---

## Queue architecture

```text
Producer → Queue → Consumer
```

- Producer sends messages.
- Queue stores messages.
- Consumer processes messages.

---

## FIFO Queue

FIFO means First In, First Out.

```text
Message 1 → processed first
Message 2 → processed second
Message 3 → processed third
```

Good when order matters.

Examples:

- wallet transaction events,
- ordered chat messages,
- booking lifecycle events.

---

## Priority Queue

Some messages are more important.

Example:

```text
Payment failure alert → high priority
Marketing email → low priority
```

Good for systems where urgent jobs must jump ahead.

---

## Poison Messages

A poison message is a message that repeatedly fails processing.

Example:

```text
Message contains invalid JSON
Consumer crashes every time it reads it
```

If not handled, it can block the queue.

### Solution

Use retries and a Dead Letter Queue.

```text
Queue → Consumer fails
      → retry 1
      → retry 2
      → retry 3
      → Dead Letter Queue
```

---

## Duplicate Messages

Distributed queues may deliver the same message more than once.

Therefore consumers should be **idempotent**.

Idempotent means processing the same message multiple times has the same final result.

### Example

Bad:

```text
Message: add ₹100 cashback
Processing twice adds ₹200
```

Good:

```text
Message has event_id
Consumer checks if event_id already processed
Processing twice does not duplicate cashback
```

---

## At-most-once, at-least-once, exactly-once

| Delivery type | Meaning | Risk |
|---|---|---|
| At-most-once | Message delivered 0 or 1 time | Can lose messages |
| At-least-once | Delivered 1 or more times | Can duplicate messages |
| Exactly-once | Delivered exactly once | Hard/expensive, often scoped |

Most practical systems use **at-least-once + idempotency**.

---

## Interview wording

> “I would use a message queue to decouple services and move non-critical work out of the request path. I would design consumers to be idempotent because duplicate delivery can happen. Failed messages would go to a dead-letter queue after retries.”

---

# 37. Pub/Sub Model

## Core idea

Pub/Sub means publishers send events to a topic, and multiple subscribers receive those events.

```text
Publisher → Topic
              ├── Subscriber A
              ├── Subscriber B
              └── Subscriber C
```

The publisher does not need to know who consumes the event.

---

## Queue vs Pub/Sub

### Queue

Usually one message is processed by one consumer in a consumer group.

```text
Message → one worker handles it
```

### Pub/Sub

One event can be received by multiple subscribers.

```text
OrderCreated event
  ├── Email Service
  ├── Analytics Service
  ├── Seller Notification Service
  └── Recommendation Service
```

---

## When Pub/Sub is useful

Use Pub/Sub when many services should react to the same event independently.

Examples:

- order created,
- user registered,
- payment completed,
- video uploaded,
- stock item expired,
- appointment booked,
- plan item purchased.

---

## Push vs Pull Model

### Push model

The broker pushes events to consumers.

```text
Broker → Consumer
```

Good for low-latency delivery, but consumer may get overwhelmed.

### Pull model

Consumers request messages when ready.

```text
Consumer → Broker: give me messages
```

Good for backpressure because consumers control their pace.

---

## Backpressure

Backpressure means the system can slow down intake when downstream components are overloaded.

Without backpressure:

```text
Producer sends 100k events/sec
Consumer handles 5k/sec
Queue grows forever
```

With backpressure:

```text
Consumer pulls only what it can handle
Producer may be rate-limited
```

---

## Event naming

Good event names describe business facts, not commands.

Bad:

```text
SendEmail
UpdateAnalytics
```

Good:

```text
OrderCreated
PaymentCompleted
VideoUploaded
AppointmentBooked
```

Why?

Because multiple services can decide what to do after a fact happens.

---

## Interview wording

> “I would use Pub/Sub when multiple services need to react to the same event independently. For example, after OrderCreated, email, analytics, seller notification, and recommendation services can all subscribe without coupling to the order service.”

---

# 38. Fault / Error

## Core idea

Fault tolerance means the system continues operating, possibly in degraded mode, even when something fails.

A mature system assumes failure.

The question is not:

> Will something fail?

The question is:

> What fails, how do we detect it, and how does the system recover?

---

## Types of failures

### 1. Hardware failures

Examples:

- server crash,
- disk failure,
- memory failure,
- network device failure,
- power outage.

### 2. Software failures

Examples:

- bug in deployment,
- memory leak,
- infinite loop,
- bad database query,
- dependency version issue,
- unhandled exception.

### 3. Network failures

Examples:

- timeout,
- packet loss,
- DNS failure,
- region connectivity issue,
- dependency unreachable.

### 4. Human errors

Examples:

- wrong config,
- accidental delete,
- bad migration,
- wrong environment variable,
- deploying to wrong environment.

---

## Common fault-tolerance patterns

## 1. Retry

Retry temporary failures.

But retries must be controlled.

Bad:

```text
Retry immediately forever
```

Good:

```text
Retry with exponential backoff and max attempts
```

Example:

```text
Attempt 1 fails
Wait 1 sec
Attempt 2 fails
Wait 2 sec
Attempt 3 fails
Wait 4 sec
Stop and send to DLQ / return fallback
```

---

## 2. Timeout

Never wait forever.

Every network call should have a timeout.

Bad:

```text
Service A waits indefinitely for Service B
Thread pool fills
Service A collapses
```

Good:

```text
Service A waits max 2 seconds
Then fails gracefully or returns fallback
```

---

## 3. Circuit Breaker

If a dependency is failing repeatedly, stop calling it temporarily.

```text
Normal → failures increase → circuit opens → calls fail fast
                          → after cooldown → half-open test
                          → healthy? close circuit
```

This prevents cascading failure.

---

## 4. Bulkhead

Isolate resources so one failing area does not sink the whole system.

Example:

```text
Payment thread pool separate from recommendation thread pool
```

If recommendation fails, payment can still work.

---

## 5. Fallback

Return a degraded response instead of failing completely.

Examples:

- show cached feed,
- show default recommendations,
- hide optional widgets,
- queue email for later,
- reduce video quality.

---

## 6. Graceful degradation

The system keeps core functionality alive while non-core features degrade.

Example e-commerce:

```text
Checkout must work.
Recommendations can fail.
Reviews can load later.
Analytics can be delayed.
```

---

## 7. Idempotency

If an operation is retried, it should not produce duplicate side effects.

Critical for:

- payments,
- order creation,
- booking confirmation,
- stock transfer,
- notification sending.

Example:

```text
POST /payments
Idempotency-Key: abc-123
```

If the client retries, backend recognizes the same operation.

---

## Interview wording

> “I would design for failure by adding timeouts, retries with backoff, circuit breakers, idempotency, dead-letter queues, and graceful degradation. The goal is to prevent one dependency failure from cascading into full system failure.”

---

# 39. Monitoring and Observability

## Core idea

Monitoring tells you **something is wrong**.

Observability helps you understand **why it is wrong**.

A production system without observability is like driving at night without headlights.

---

## The three pillars

## 1. Logs

Logs are event records.

Examples:

```text
User login failed
Payment provider timeout
Order created
Cache miss for product:123
```

Good logs include:

- timestamp,
- request ID / correlation ID,
- user ID when safe,
- service name,
- error code,
- meaningful message.

Avoid logging sensitive data like passwords, tokens, full card data, OTPs.

---

## 2. Metrics

Metrics are numerical measurements over time.

Examples:

- requests per second,
- latency,
- error rate,
- CPU usage,
- memory usage,
- queue depth,
- cache hit ratio.

Metrics help dashboards and alerts.

---

## 3. Traces

Traces follow a request across services.

```text
Client request
  ↓
API Gateway
  ↓
Order Service
  ↓
Payment Service
  ↓
Inventory Service
```

A trace shows where time was spent and where failure happened.

---

## Golden signals

A strong baseline is:

| Signal | Meaning |
|---|---|
| Latency | How long requests take |
| Traffic | How many requests arrive |
| Errors | How many requests fail |
| Saturation | How full resources are |

---

## Why observability matters to developers

Observability is not only an operations topic. It directly affects code quality.

A good engineer asks:

- Can I debug this in production?
- Can I identify slow endpoints?
- Can I know which dependency failed?
- Can I trace one user request end-to-end?
- Can I safely deploy and detect regressions?

---

## Interview wording

> “Monitoring tells us whether the system is healthy, while observability helps us debug unknown issues. I would collect logs, metrics, and traces with correlation IDs so we can follow a request across services.”

---

# 40. API Handling Monitoring

## Core idea

API monitoring focuses on how your endpoints behave in production.

For backend systems, API health is business health.

---

## What to monitor for APIs

## 1. Request rate / throughput

How many requests per second/minute?

```text
GET /products → 2,000 rpm
POST /orders → 150 rpm
POST /payments → 30 rpm
```

Throughput helps capacity planning.

---

## 2. Latency

Latency is response time.

Do not only track average latency.

Average can hide bad user experience.

Track percentiles:

| Metric | Meaning |
|---|---|
| P50 | 50% of requests are faster than this |
| P95 | 95% are faster than this |
| P99 | 99% are faster than this |

Example:

```text
Average latency: 150ms
P99 latency: 3000ms
```

Average looks fine, but 1% of users suffer badly.

---

## 3. Error rate

Track 4xx and 5xx separately.

| Error type | Meaning |
|---|---|
| 4xx | Client/request issue |
| 5xx | Server/system issue |

A sudden spike in 500 errors after deployment is a red flag.

---

## 4. Status code distribution

Example dashboard:

```text
2xx: 97.5%
4xx: 2.0%
5xx: 0.5%
```

Useful during incidents.

---

## 5. Dependency latency

Your API may be slow because of dependencies:

- database,
- Redis,
- payment gateway,
- email service,
- third-party API,
- object storage.

Track each dependency separately.

---

## 6. Endpoint-level metrics

Do not only monitor global API latency.

Track per endpoint:

```text
/api/products/{id}
/api/orders
/api/payments
/api/login
/api/search
```

One endpoint can be broken while the global average still looks fine.

---

## 7. SLOs and alerts

Example SLO:

```text
99% of checkout requests should complete under 500ms over 30 days.
```

Alert when burn rate is high.

Good alerts are actionable.

Bad alert:

```text
Something is wrong.
```

Good alert:

```text
POST /checkout 5xx rate > 2% for 5 minutes after deployment v1.4.2
```

---

## API monitoring checklist

For each important API, track:

- RPS,
- P50/P95/P99 latency,
- 4xx rate,
- 5xx rate,
- dependency latency,
- timeout count,
- retry count,
- payload size,
- cache hit/miss ratio,
- database query time.

---

## Android/frontend connection

As an Android developer, you should care about API monitoring because mobile apps feel slow when APIs are slow.

Client-side metrics to track:

- API call duration,
- timeout rate,
- retry count,
- network type,
- app version,
- crash correlation,
- screen load time.

A strong mobile engineer can say:

> “I monitor not only backend latency, but also perceived client latency.”

---

## Interview wording

> “For API monitoring, I would track throughput, latency percentiles, error rates, status-code distribution, and dependency latency per endpoint. I would alert on SLO violations rather than noisy raw metrics.”

---

# 41. Machine Monitoring

## Core idea

Machine monitoring tells you whether the infrastructure has enough resources to keep the application healthy.

APIs can fail because machines are overloaded.

---

## What to monitor

## 1. CPU usage

High CPU means the machine is spending too much time computing.

Possible causes:

- heavy computation,
- inefficient code,
- too many requests,
- tight loops,
- serialization/deserialization overhead,
- encryption/compression cost.

Watch sustained CPU, not just short spikes.

---

## 2. Memory usage

High memory can cause:

- garbage collection pressure,
- slow response time,
- out-of-memory crashes,
- container restarts.

For JVM/Spring Boot apps, also monitor:

- heap usage,
- non-heap memory,
- GC pause time,
- thread count.

---

## 3. Disk I/O

Disk IO matters for:

- databases,
- logging-heavy systems,
- file uploads,
- video processing,
- analytics pipelines.

High disk wait can make the whole system slow.

---

## 4. Network I/O

Network monitoring matters when:

- services communicate heavily,
- media files are served,
- databases are remote,
- third-party calls are frequent,
- cross-region replication is used.

Track:

- bandwidth,
- packet loss,
- connection errors,
- DNS failures,
- timeout rate.

---

## 5. Thread pools

In backend systems, thread pool saturation is a silent killer.

Example:

```text
All request threads wait for slow payment provider
New requests cannot be handled
System appears down
```

Monitor:

- active threads,
- queue size,
- rejected tasks,
- blocked threads.

---

## 6. Database machine metrics

For DB servers, monitor:

- CPU,
- memory,
- disk usage,
- IOPS,
- slow queries,
- lock waits,
- connection count,
- replication lag,
- index usage.

---

## Alert thresholds

Do not alert on every tiny spike.

Better:

```text
CPU > 85% for 10 minutes
Memory > 90% for 5 minutes
Disk free < 15%
P99 latency > 1 second for 10 minutes
Database connections > 90% capacity
```

---

## Interview wording

> “Machine monitoring covers CPU, memory, disk IO, network IO, thread pools, and database resource pressure. I would correlate machine metrics with API latency and error rates to understand whether the issue is infrastructure or application logic.”

---

# 42. System Design of a Streaming Application

## Goal

Design a video streaming system like a simplified YouTube/Netflix-style application.

This is the final case study because it combines almost everything:

- uploads,
- storage,
- queues,
- processing,
- caching,
- CDN,
- databases,
- load balancing,
- scalability,
- monitoring.

---

## Step 1: Clarify requirements

Before drawing architecture, ask requirements.

### Functional requirements

Users should be able to:

- upload videos,
- view videos,
- stream videos smoothly,
- see title/description/thumbnail,
- search/browse videos,
- watch different quality levels,
- resume playback,
- like/comment/share if needed.

### Non-functional requirements

The system should support:

- high availability,
- low startup latency,
- smooth playback,
- scalability for many viewers,
- durable video storage,
- async video processing,
- CDN delivery,
- monitoring and recovery.

---

## Step 2: Separate upload path and playback path

A common mistake is treating upload and streaming as one flow.

They are different.

### Upload path

```text
User uploads video
  ↓
Upload API
  ↓
Object Storage
  ↓
Message Queue
  ↓
Video Processing Workers
  ↓
Encoded files + thumbnails
  ↓
Metadata DB update
```

### Playback path

```text
User opens video
  ↓
Playback API fetches metadata
  ↓
Player gets manifest file
  ↓
Video segments loaded from CDN
  ↓
Adaptive playback continues
```

Upload is write/processing-heavy.
Playback is read/network-heavy.

---

## Step 3: Video metadata vs video file

Do not store video files directly in a relational database.

Store video file in object storage:

- S3,
- Google Cloud Storage,
- Azure Blob Storage,
- MinIO,
- other media storage.

Store metadata in DB:

```text
video_id
user_id
title
description
thumbnail_url
status
created_at
duration
available_resolutions
manifest_url
```

---

## Step 4: Upload architecture

```text
Client
  ↓
Upload Service
  ↓
Object Storage: raw video
  ↓
Queue: VideoUploaded event
  ↓
Transcoding Workers
  ↓
Object Storage: encoded outputs
  ↓
Metadata Service updates DB
```

### Why queue is needed

Video processing is slow and compute-heavy. The upload API should not keep the user waiting until all encoding completes.

Better:

```text
Upload complete → return "processing" status
Encoding happens asynchronously
Notify user when ready
```

---

## Step 5: Transcoding

Users upload videos in different formats:

- MP4,
- MOV,
- AVI,
- MKV,
- different codecs,
- different resolutions,
- different bitrates.

The system should convert them into standard playback formats.

Example outputs:

```text
240p
360p
480p
720p
1080p
```

Each resolution may have different bitrate.

---

## Step 6: Segmenting

Streaming systems usually split videos into small chunks/segments.

Instead of downloading a full 1GB video, the player downloads small pieces.

```text
video_720p_segment_001.ts
video_720p_segment_002.ts
video_720p_segment_003.ts
```

This allows:

- faster start,
- seeking,
- adaptive bitrate,
- retrying small failed chunks,
- CDN caching.

---

## Step 7: Manifest file

A manifest file tells the player what segments are available.

Example concept:

```text
video.m3u8
  ├── 360p playlist
  ├── 720p playlist
  └── 1080p playlist
```

The player reads the manifest and downloads the right segments.

---

## Step 8: Adaptive Bitrate Streaming

Adaptive bitrate streaming means the player changes quality based on network conditions.

```text
Fast network → 1080p
Medium network → 720p
Slow network → 360p
Very slow network → 240p
```

This is better than forcing one quality because it prevents buffering.

For user experience, continuous playback is often more important than always showing the highest quality.

---

## Step 9: CDN

A CDN stores video segments close to users.

```text
Origin Storage
  ↓
CDN Edge Mumbai
  ↓
User in India
```

Without CDN, every user hits your origin storage or backend. That is expensive and slow.

With CDN:

- lower latency,
- lower origin load,
- better global performance,
- caching for popular videos.

---

## Step 10: Playback architecture

```text
Mobile/Web Player
  ↓
Playback API
  ↓
Metadata DB / Cache
  ↓
Manifest URL
  ↓
CDN
  ↓
Video segments
```

Important point:

The backend should not stream every video byte if CDN can serve the segments.

Backend handles metadata, authorization, tracking, and signed URLs. CDN handles media delivery.

---

## Step 11: Caching in streaming

Caching appears at multiple levels:

### Metadata cache

```text
video_id → title, thumbnail, manifest_url, duration
```

### CDN cache

```text
popular video segments cached near users
```

### Client cache

```text
player buffers next few segments
browser/mobile may cache segments
```

---

## Step 12: Load balancing

Use load balancers for:

- upload API,
- playback API,
- metadata service,
- search service,
- processing control service.

But media segment delivery should primarily go through CDN.

---

## Step 13: Database design

Possible tables/collections:

```text
users
videos
video_assets
video_processing_jobs
comments
likes
watch_history
playback_sessions
```

### videos

```text
id
owner_id
title
description
status
created_at
published_at
duration_seconds
thumbnail_url
manifest_url
```

### video_assets

```text
id
video_id
resolution
bitrate
format
storage_url
size_bytes
```

### processing_jobs

```text
id
video_id
job_type
status
attempt_count
error_message
created_at
updated_at
```

---

## Step 14: Scaling reads

Streaming systems are usually read-heavy.

One upload can create thousands or millions of views.

Scale reads using:

- CDN,
- metadata cache,
- read replicas,
- search indexes,
- async counters.

---

## Step 15: Scaling writes

Uploads and comments/likes/watch events are writes.

Use:

- queues for processing,
- batching for analytics,
- idempotency for uploads,
- retries for failed jobs,
- DLQ for poison processing events.

---

## Step 16: View count design

Do not update the main DB row for every view synchronously.

Bad:

```text
Every view → update videos.view_count in DB
```

This creates hot rows for popular videos.

Better:

```text
View event → queue/stream → aggregate counters → periodic DB update
```

Or use Redis counters and flush periodically.

---

## Step 17: Search

For video search, do not depend only on relational DB `LIKE` queries at scale.

Use a search system:

- Elasticsearch,
- OpenSearch,
- Solr,
- managed search service.

Flow:

```text
Video published
  ↓
VideoPublished event
  ↓
Search index updated
```

---

## Step 18: Failure handling

### Upload failure

- support resumable upload,
- chunk upload,
- retry failed chunks.

### Processing failure

- retry job,
- mark failed after max attempts,
- store error,
- alert if failure rate spikes,
- send to DLQ.

### CDN failure

- fallback to another edge/origin,
- multi-CDN for large platforms,
- retry segment.

### Metadata DB failure

- serve cached metadata if safe,
- degrade comments/likes,
- keep playback alive when possible.

---

## Step 19: Security

Streaming systems need:

- authenticated uploads,
- authorization for private videos,
- signed URLs for protected content,
- rate limiting,
- virus/malware scan for uploads,
- content moderation pipeline,
- abuse detection,
- secure object storage permissions.

Do not expose raw private storage URLs directly for protected content.

---

## Step 20: Monitoring for streaming

Track:

- upload success rate,
- processing queue depth,
- transcoding failure rate,
- average processing time,
- playback start time,
- buffering ratio,
- CDN hit ratio,
- segment download failure rate,
- API latency,
- storage errors,
- worker CPU/GPU usage.

User experience metrics:

- time to first frame,
- rebuffer count,
- average bitrate,
- playback failure rate.

---

## Complete high-level architecture

```text
                           ┌────────────────────┐
                           │   Web / Mobile App  │
                           └─────────┬──────────┘
                                     │
                     ┌───────────────┴────────────────┐
                     │                                │
              Upload Flow                       Playback Flow
                     │                                │
                     ▼                                ▼
          ┌──────────────────┐              ┌──────────────────┐
          │  Upload Service   │              │ Playback Service │
          └─────────┬────────┘              └─────────┬────────┘
                    │                                 │
                    ▼                                 ▼
          ┌──────────────────┐              ┌──────────────────┐
          │ Object Storage    │              │ Metadata Cache   │
          │ Raw Video         │              └─────────┬────────┘
          └─────────┬────────┘                        │
                    │                                 ▼
                    ▼                       ┌──────────────────┐
          ┌──────────────────┐              │ Metadata DB      │
          │ Video Queue       │              └─────────┬────────┘
          └─────────┬────────┘                        │
                    │                                 ▼
                    ▼                       ┌──────────────────┐
          ┌──────────────────┐              │ CDN              │
          │ Transcode Workers │              │ Video Segments   │
          └─────────┬────────┘              └─────────┬────────┘
                    │                                 │
                    ▼                                 ▼
          ┌──────────────────┐              ┌──────────────────┐
          │ Encoded Videos    │              │ User Playback    │
          │ Thumbnails        │              └──────────────────┘
          │ Manifests         │
          └──────────────────┘
```

---

## How to explain this in an interview

Use this flow:

1. Clarify requirements.
2. Separate upload and playback paths.
3. Store raw video in object storage.
4. Use queue for async processing.
5. Transcode into multiple resolutions.
6. Segment videos and generate manifest.
7. Use CDN for playback.
8. Store metadata in DB and cache hot metadata.
9. Use load balancers for APIs.
10. Monitor playback quality, CDN hit ratio, and processing jobs.

### Strong interview answer

> “I would separate upload from playback. Upload writes the raw video to object storage and publishes a VideoUploaded event. Processing workers transcode the video into multiple resolutions, segment it, generate manifests, and update metadata. Playback API returns metadata and manifest URLs, while the CDN serves the actual video segments. This keeps the backend out of the media byte path and allows scaling through CDN, queues, workers, and caching.”

---

# 43. Final Mindset / Thank You

The biggest takeaway from this course is not one specific tool.

It is the thinking model:

```text
Understand requirements
  ↓
Identify bottlenecks
  ↓
Choose components
  ↓
Explain trade-offs
  ↓
Design for failure
  ↓
Measure in production
```

A strong engineer does not say:

> “I will use Redis, Kafka, Kubernetes, Cassandra, and CDN.”

A strong engineer says:

> “This endpoint is read-heavy, so I will cache hot data with TTL. Writes need correctness, so DB remains source of truth. Async work will go through a queue. For video delivery, CDN serves segments because backend streaming would not scale.”

That is the real difference.

---

# Final Revision Sheet

## Cache

- Use cache for hot, frequently accessed data.
- Cache is not automatically correct.
- Always think about TTL, invalidation, and consistency.
- Read-through is good for read-heavy systems.
- Write-through improves freshness but slows writes.
- Write-around avoids cache pollution.
- Write-back is fast but needs durability protection.

## Load Balancer

- Distributes traffic.
- Checks server health.
- Enables horizontal scaling.
- Supports zero-downtime deployment.
- Algorithms depend on workload.

## Replication

- Copies data for availability and read scale.
- Creates replication lag.
- Single-leader is simplest.
- Multi-leader supports regional writes but has conflicts.
- Leaderless uses quorum and is more complex.

## Partitioning

- Splits data across nodes.
- Helps with storage and throughput.
- Adds routing and cross-shard query complexity.
- Good partition key is critical.
- Hotspots can destroy performance.

## CAP

- During network partition, choose consistency or availability.
- Payments prefer consistency.
- Feeds/video metadata often prefer availability.
- Partition tolerance is mandatory in distributed systems.

## Message Queues

- Decouple services.
- Move slow work out of request path.
- Handle retries and traffic spikes.
- Consumers should be idempotent.
- Use DLQ for poison messages.

## Observability

- Logs explain events.
- Metrics show trends.
- Traces follow requests.
- Track latency percentiles, not only averages.
- Alert on user/business impact.

## Streaming Application

- Upload and playback are separate flows.
- Store videos in object storage, not DB.
- Use queues for transcoding.
- Generate multiple resolutions and segments.
- Serve video through CDN.
- Cache metadata.
- Monitor playback quality.

---

# Interview Confidence Prompts

Practice answering these aloud:

1. Why do we need cache if we already have a database?
2. What is the difference between read-through and write-through cache?
3. Why is write-back cache risky?
4. Which eviction policy would you choose for a product catalog?
5. Why does a load balancer need health checks?
6. When would you use least connections instead of round robin?
7. What problem does replication solve?
8. What is replication lag?
9. Why is sharding hard?
10. How do you choose a partition key?
11. Explain CAP theorem with a banking and Instagram example.
12. Why use a message queue after order creation?
13. What is a poison message?
14. Why should queue consumers be idempotent?
15. What is the difference between monitoring and observability?
16. What metrics would you track for APIs?
17. How would you design video upload and playback separately?
18. Why should CDN serve video segments instead of backend servers?

---

# Mini System Design Templates

## Template 1: Read-heavy service

```text
Client
  ↓
Load Balancer
  ↓
Backend API
  ↓
Cache
  ↓ miss
Database Read Replica
```

Use for:

- product listing,
- course catalog,
- public blog pages,
- user profile summaries.

---

## Template 2: Write-heavy async service

```text
Client
  ↓
Backend API
  ↓
Database for critical write
  ↓
Event Queue
  ├── Notification Worker
  ├── Analytics Worker
  └── Search Index Worker
```

Use for:

- order creation,
- appointment booking,
- stock updates,
- user registration.

---

## Template 3: Media processing service

```text
Client Upload
  ↓
Upload Service
  ↓
Object Storage
  ↓
Queue
  ↓
Processing Workers
  ↓
CDN-ready assets
```

Use for:

- video upload,
- image processing,
- document conversion,
- audio processing.

---

# Project Connection: How this applies to your work

## Android apps

For Android, these topics appear as:

- local cache vs remote API,
- offline-first design,
- retry and idempotency,
- API timeout handling,
- pagination,
- WebSocket/realtime updates,
- image/video caching,
- client-side observability.

Strong Android explanation:

> “The mobile app should not blindly retry unsafe POST requests. For critical actions, backend should support idempotency keys, and the app should handle loading, retry, and eventual sync states clearly.”

---

## Spring Boot backend

For Spring Boot, these topics appear as:

- Redis caching,
- Actuator health checks,
- rate limiting,
- Resilience4J retries/circuit breakers,
- Kafka/RabbitMQ queues,
- database read replicas,
- async event processing,
- Micrometer metrics,
- structured logging.

Strong backend explanation:

> “For non-critical side effects after a successful business operation, I would publish an event and process it asynchronously. The main transaction stays small, and downstream failures do not block the user.”

---

# Final mental model

Whenever you design a system, walk through this sequence:

```text
1. What are the core user actions?
2. Which actions are read-heavy?
3. Which actions are write-heavy?
4. Which data must be strongly consistent?
5. Which data can be eventually consistent?
6. What can be cached?
7. What can be async?
8. What needs replication?
9. What needs partitioning?
10. What fails, and how do we recover?
11. What metrics prove the system is healthy?
```

If you can answer these questions clearly, you are no longer just memorizing system design terms. You are thinking like an engineer.
