# 🧠 DAY 1 MASTERY

# Spring Boot REST APIs + Spring Security + JWT

**Outcome of this day:**  
You can confidently **design, build, secure, and explain** a production-ready REST API using *
*Spring Boot + JWT security**.

---

# 🌅 MORNING – CORE CONCEPTS

## 🔹 Spring Boot REST API Architecture

```
Client → Controller → Service → Repository → Database
```

| Layer          | Responsibility                    |
|----------------|-----------------------------------|
| **Controller** | Handles HTTP requests & responses |
| **Service**    | Business logic                    |
| **Repository** | Database access (JPA)             |
| **Entity**     | DB table representation           |
| **DTO**        | Data sent over network            |

---

## 🔹 DTO vs Entity

| Entity                          | DTO                           |
|---------------------------------|-------------------------------|
| Maps directly to database table | Used for API request/response |
| Contains internal fields        | Exposes only safe data        |
| Should NOT be exposed directly  | Used to shape API contract    |

### Example

```java

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role;
}
```

```java
public class UserResponseDTO {
    private Long id;
    private String username;
}
```

---

## 🔹 Dependency Injection

```java

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
}
```

<div id=“dependencyinjection”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">DEPENDENCY INJECTION (ELABORATED)</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>
Ahh nice — you're asking **HOW Spring automatically injects objects** (Dependency Injection). This is a *core interview topic*, so let’s break it down cleanly.

---

## 🧩 What is Dependency Injection (DI)?

**Dependency Injection means:**
A class does **not create the objects it needs**.
Instead, **Spring provides (injects) them**.

So instead of this ❌

```java
UserRepository repo = new UserRepository(); // BAD practice
```

We do this ✅

```java
private final UserRepository repo; // Spring gives this
```

---

## ⚙️ How Spring Does This Internally

![Image](https://www.springboottutorial.com/images/spring-features.png)

![Image](https://miro.medium.com/1%2A5MShKzJV3ClbCRHHWxIWIw.png)

![Image](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/images/container-magic.png)

![Image](https://miro.medium.com/0%2ATyMHbF96XDfJf1Wi.png)

Spring uses something called the **IoC Container** (Inversion of Control Container).

### Step-by-step flow:

1️⃣ Spring starts your application
2️⃣ It scans for classes with annotations like:

* `@Component`
* `@Service`
* `@Repository`
* `@Controller`

3️⃣ It **creates objects (Beans)** of those classes
4️⃣ It stores them in the **Application Context**
5️⃣ When another class needs that object → Spring **injects it automatically**

---

## 🏗 Example Walkthrough

### Step 1 — Repository Bean

```java

@Repository
public class UserRepository {
}
```

Spring sees `@Repository` → creates a bean.

---

### Step 2 — Service Needs Repository

```java

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
}
```

Spring sees:

* `@Service` → create UserService bean
* Constructor requires `UserRepository`
* Spring **looks in its container**
* Finds UserRepository bean
* Injects it into constructor automatically

You **never used `new`**, but the object exists.

---

## 🧠 Why This Is Powerful

| Without DI                     | With DI              |
|--------------------------------|----------------------|
| Tight coupling                 | Loose coupling       |
| Hard to test                   | Easy to mock         |
| Manual object creation         | Automatic management |
| Hard to change implementations | Plug & play          |

---

## 🧪 Types of Injection (Interview Question)

| Type                  | Example                                   | Recommended? |
|-----------------------|-------------------------------------------|--------------|
| Constructor Injection | `public UserService(UserRepository repo)` | ✅ BEST       |
| Field Injection       | `@Autowired private UserRepository repo;` | ❌ Avoid      |
| Setter Injection      | `setRepo(UserRepository repo)`            | Rare         |

### Why Constructor Injection is Best

✔ Makes dependency mandatory
✔ Easier unit testing
✔ Immutable design (`final` fields)

---

## 🎯 Interview Answer (Polished)

> “Spring uses an IoC container to manage objects called beans. When we annotate classes with
`@Service`, `@Repository`, or `@Component`, Spring creates those objects at startup and stores them
> in the application context. If another bean requires it through constructor injection, Spring
> automatically injects the dependency. This promotes loose coupling and makes the application
> easier
> to test and maintain.”

---

If you'd like, next I can explain
👉 **How Spring creates Beans internally**
👉 **Bean lifecycle**
👉 **Difference between @Component, @Service, @Repository**


> [⬆️ Back to Top / Close](#dependencyinjection)
</blockquote>
</details>

---

## 🔹 Authentication vs Authorization

| Authentication      | Authorization        |
|---------------------|----------------------|
| Who are you?        | What can you access? |
| Login               | Roles/permissions    |
| Username + password | ADMIN / USER roles   |

---

## 🔹 What is JWT?

**JSON Web Token** is a signed token used to verify identity.

### Structure:

```
HEADER.PAYLOAD.SIGNATURE
```

| Part      | Contains                 |
|-----------|--------------------------|
| Header    | Algorithm                |
| Payload   | User info, roles         |
| Signature | Verifies token integrity |

JWT enables **stateless authentication** — server does NOT store sessions.

---

# 🌞 AFTERNOON – HANDS-ON IMPLEMENTATION

## 🏗 Step 1: Create Spring Boot Project

Dependencies:

- Spring Web
- Spring Data JPA
- Spring Security
- H2/MySQL
- Lombok
- Validation

---

## 📁 Project Structure

```
com.example.demo
 ┣ controller
 ┣ service
 ┣ repository
 ┣ dto
 ┣ entity
 ┣ security
 ┗ config
```

---

## 👤 Step 2: User Entity

```java

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String role;
}
```

---

## 📦 Step 3: Repository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

---

## ⚙️ Step 4: Service Layer

```java

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        return repo.save(user);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }
}
```

---

## 🌐 Step 5: REST Controller

```java

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(user));
    }

    @GetMapping
    public List<User> allUsers() {
        return service.getAllUsers();
    }
}
```

---

## ✅ Request Validation

```java
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
```

---

## 🌍 Global Exception Handling

```java

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
```

<div id=“globalexception”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍 Global Exception Handling in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>
Love this topic — **Global Exception Handling** is a *big-league backend skill*. It shows you know how to make APIs **clean, predictable, and production-ready** instead of returning messy stack traces.

---

# 🌍 Global Exception Handling in Spring Boot

![Image](https://miro.medium.com/v2/resize%3Afit%3A1200/1%2ACzoJBW4aL6y6zIo9NQpL6A.png)

![Image](https://i.sstatic.net/bZ7jO.png)

![Image](https://scaler.com/topics/images/controller-advice-working.webp)

![Image](https://miro.medium.com/1%2AC_1y3eGCE7wAbbAitujSYg.png)

## 🧠 Why We Need It

Without global handling ❌
If an error happens, Spring may return:

* Long stack traces
* HTML error pages
* Inconsistent error formats

That’s bad for frontend/mobile teams.

With global handling ✅
We return **structured JSON errors** like:

```json
{
  "timestamp": "2026-02-04T10:15:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Username is required",
  "path": "/api/users"
}
```

---

## 🔹 What is `@RestControllerAdvice`?

It is a **global error interceptor** for all controllers.

Think of it as:

> “If any controller throws an exception → send it here first.”

---

## 🏗 Basic Structure

```java

@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

Now we define methods to handle different exception types.

---

## 🔥 1️⃣ Handle Generic Exceptions

```java

@ExceptionHandler(Exception.class)
public ResponseEntity<?> handleGeneralException(Exception ex, HttpServletRequest request) {

    Map<String, Object> error = new LinkedHashMap<>();
    error.put("timestamp", LocalDateTime.now());
    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.put("error", "Internal Server Error");
    error.put("message", ex.getMessage());
    error.put("path", request.getRequestURI());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
}
```

Catches **all uncaught exceptions**.

---

## ⚠️ 2️⃣ Handle Custom Business Exceptions

### Step A — Create Custom Exception

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

### Step B — Throw It in Service

```java
public User getUser(Long id) {
    return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
}
```

### Step C — Handle It Globally

```java

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

    Map<String, Object> error = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.NOT_FOUND.value(),
            "error", "Not Found",
            "message", ex.getMessage(),
            "path", request.getRequestURI()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}
```

Now every "not found" error returns **404 JSON**, not crash.

---

## 🧾 3️⃣ Handle Validation Errors (`@Valid`)

When request body validation fails.

```java

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
    );

    return new ResponseEntity<>(Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "errors", errors
    ), HttpStatus.BAD_REQUEST);
}
```

### Example Response

```json
{
  "timestamp": "2026-02-04T10:20:00",
  "status": 400,
  "errors": {
    "username": "Username cannot be blank",
    "password": "Password must be at least 6 characters"
  }
}
```

---

## 🔒 4️⃣ Handle Security Exceptions

```java

@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(Map.of("error", "You don't have permission to access this resource"));
}
```

---

## 🧩 How It Works Internally

1️⃣ Controller throws exception
2️⃣ Spring checks for `@ExceptionHandler` methods
3️⃣ If found → that method builds response
4️⃣ If not → default Spring error handler

---

## 🧱 Best Practice: Create Standard Error DTO

```java

@Data
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
```

Use this instead of `Map` for cleaner design.

---

## 🎯 Interview Answer (Perfect Version)

> “In Spring Boot, we use `@RestControllerAdvice` to centralize exception handling across all REST
> controllers. We define methods annotated with `@ExceptionHandler` to catch specific exceptions
> like
> validation failures, resource not found, or generic server errors. This ensures we return
> consistent, structured JSON error responses with proper HTTP status codes instead of stack traces,
> improving API reliability and client integration.”

---

## 🏁 Summary

| Feature                 | Why Important              |
|-------------------------|----------------------------|
| `@RestControllerAdvice` | Central error handling     |
| Custom Exceptions       | Clean business logic       |
| Validation Handling     | User-friendly input errors |
| Consistent Error JSON   | Frontend-friendly APIs     |
| Proper HTTP Codes       | REST best practice         |

---

If you want next, I can give:
✅ Full **Error Handling Template** ready to paste into project
✅ How to log errors properly with SLF4J
✅ Difference between `@ExceptionHandler` and `ResponseStatusException`


> [⬆️ Back to Top / Close](#globalexception)
</blockquote>
</details>

---

# 🔐 SPRING SECURITY + JWT

## 🔹 Password Encoder

```java

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## 🔹 JWT Utility Class

```java

@Component
public class JwtUtil {

    private final String SECRET = "secretkey123456";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject();
    }
}
```

---

## 🔹 JWT Filter

```java

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String username = jwtUtil.extractUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}
```

---

## 🔹 Security Configuration

```java

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

---

## 🔹 Login Controller (JWT Generation)

```java

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
```

---

## 🔹 Role-Based Authorization

```java

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin")
public String adminOnly() {
    return "Welcome Admin!";
}
```

---

# 🌙 EVENING – INTERVIEW PREPARATION

## 🎯 Explain Secure REST API Flow

Client logs in with username & password → Server authenticates → JWT token generated → Client sends
JWT in Authorization header → Spring Security filter validates token → If valid, request reaches
controller → Stateless & scalable.

<div id=“securerestapiflow”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍 Global Exception Handling in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>

Perfect question — this is the **exact part interviewers love to probe**. Let’s slow it down and
make the flow crystal clear.

---

# 🔐 Part You’re Confused About (Deep Dive)

> **Client sends JWT in Authorization header → Spring Security filter validates token → If valid,
request reaches controller → Stateless**

We’ll trace **ONE protected API request** step by step.

---

## 🧑‍💻 Step 1: Client Sends Request with JWT

After login, client got a token like:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Now every protected API call includes this in the **HTTP header**:

```
GET /api/users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

📌 This is how the client proves:

> “I am already logged in. Here is my identity token.”

No username/password again.

---

## 🛂 Step 2: Request Hits Spring Security Filter Chain

![Image](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/multi-securityfilterchain.png)

![Image](https://miro.medium.com/v2/resize%3Afit%3A1400/1%2AEcbUIt-Fgf55UQGXcPx9cQ.png)

![Image](https://docs.spring.io/spring-security/reference/_images/servlet/architecture/securityfilterchain.png)

![Image](https://miro.medium.com/1%2AoU_09lWZITVrBko_csgT6A.png)

Before reaching your controller, **every request passes through security filters**.

One of them is your **JWT Filter** (`OncePerRequestFilter`).

Think of it like an **airport security check** before boarding.

---

## 🔍 Step 3: JWT Filter Validates Token

Inside the filter:

### 1️⃣ Extract Token from Header

```java
String header = request.getHeader("Authorization");

if(header !=null&&header.

startsWith("Bearer ")){
String token = header.substring(7);
}
```

### 2️⃣ Decode Token & Get Username

```java
String username = jwtUtil.extractUsername(token);
```

### 3️⃣ Check If Token is Valid

* Signature correct?
* Not expired?
* Not tampered?

```java
if(jwtUtil.validateToken(token, userDetails)){
```

### 4️⃣ Create Authentication Object

```java
UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
```

### 5️⃣ Store in Security Context

```java
SecurityContextHolder.getContext().

setAuthentication(auth);
```

📌 This step tells Spring:

> “This request is from an authenticated user.”

---

## 🚦 Step 4: If Token is Valid → Controller Runs

Now Spring Security says:
✅ User is authenticated
✅ Roles are known
✅ Permissions can be checked

So request continues to:

```
Controller → Service → DB
```

If the token was ❌ invalid:

* Expired
* Wrong signature
* Missing

Spring returns **401 Unauthorized** immediately.
Controller never runs.

---

## 🧠 What Does “Stateless” Mean Here?

Traditional login (session-based):

| Old Method                      | JWT Method              |
|---------------------------------|-------------------------|
| Server stores session in memory | Server stores NOTHING   |
| Uses session ID cookie          | Uses JWT token          |
| Hard to scale                   | Easy to scale           |
| Server remembers user           | Token carries user info |

### In JWT:

Each request contains **all identity info inside the token**.

So the server:

* Does NOT store sessions
* Does NOT remember previous requests
* Just verifies token each time

That’s why we say **stateless authentication**.

---

## 🔁 Full Flow in Simple Words

1️⃣ Client logs in → gets JWT
2️⃣ Client calls protected API with JWT in header
3️⃣ Spring Security filter intercepts request
4️⃣ JWT is validated (signature + expiry)
5️⃣ User identity stored in SecurityContext
6️⃣ Request allowed to reach controller
7️⃣ Server does not store session → **stateless**

---

## 🎯 Interview-Ready Explanation

> “After login, the client includes the JWT in the Authorization header for every protected request.
> Spring Security has a filter that intercepts each request, extracts the token, validates its
> signature and expiration, and if valid, creates an Authentication object stored in the
> SecurityContext. This allows the request to reach the controller with the user identity available.
> Since the server does not store session data and relies only on the token sent by the client, the
> system is stateless and highly scalable.”

---

If you want, next I can explain visually:
👉 **Difference between Session vs JWT login**
👉 **Access Token vs Refresh Token**
👉 **How roles are stored inside JWT**

Let’s level this up — these two topics are **frequent interview questions** and also key to building
**real production auth systems**.

---

# 🔑 Access Token vs Refresh Token

![Image](https://images.ctfassets.net/xqb1f63q68s1/5KRSBrWiRfJRaFicCsU5zB/c0a166d01c500a303cb02dcf9ee8a03d/How_access_token_works.png)

![Image](https://i.sstatic.net/TlVrM.png)

![Image](https://is.docs.wso2.com/en/5.9.0/assets/img/using-wso2-identity-server/oauth-refresh-token-diagram.png)

![Image](https://apaleo.dev/assets/images/oauth/refresh_token_flow.png)

## 🧠 Why Two Tokens?

If a JWT is stolen, attacker can use it until it expires.
So we use:

| Token             | Purpose              | Lifetime          | Sent Often? |
|-------------------|----------------------|-------------------|-------------|
| **Access Token**  | Access APIs          | Short (15–30 min) | YES         |
| **Refresh Token** | Get new access token | Long (7–30 days)  | NO          |

---

## 🔹 Access Token

Used in every API call:

```
Authorization: Bearer <access_token>
```

Contains:

* Username
* Roles
* Expiry time

Short-lived for **security**.

---

## 🔹 Refresh Token

Used only to get a new access token when it expires.

Stored:

* In **HTTP-only cookie** or
* Secure storage (mobile)

Never sent with every request.

---

## 🔄 Refresh Flow

1️⃣ Access token expires
2️⃣ Client calls `/api/auth/refresh` with refresh token
3️⃣ Server verifies refresh token
4️⃣ Server issues **new access token**
5️⃣ User stays logged in without re-entering password

---

## ❓ Why Not Long Access Tokens?

Because if leaked:

* Attacker gets long access
* Hard to revoke

Short access + long refresh = **balance of security & UX**

---

# 🧩 How Roles Are Stored Inside JWT

![Image](https://fusionauth.io/img/shared/json-web-token.png)

![Image](https://cdn.sanity.io/images/3jwyzebk/production/c098fa07deca1062e013d92cabba4ba7ec7e7f19-1584x988.png)

![Image](https://miro.medium.com/0%2AsxUrFN61PAoScWED)

![Image](https://www.codejava.net/images/articles/frameworks/springboot/jwt-authorization/api_request_with_role_customer.png)

JWT has **claims** (data stored inside payload).

### Example Payload (Decoded)

```json
{
  "sub": "vishnu",
  "roles": [
    "ROLE_USER",
    "ROLE_ADMIN"
  ],
  "iat": 1707030000,
  "exp": 1707033600
}
```

| Claim   | Meaning            |
|---------|--------------------|
| `sub`   | Username (subject) |
| `roles` | User permissions   |
| `iat`   | Issued at          |
| `exp`   | Expiration         |

---

## 🔐 Adding Roles While Generating Token

```java
public String generateToken(UserDetails userDetails) {

    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList());

    return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact();
}
```

---

## 🔍 Extracting Roles from JWT

```java
public List<String> extractRoles(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody();

    return claims.get("roles", List.class);
}
```

Spring Security converts these roles into **GrantedAuthority** objects.

---

## 🛂 How Roles Control Access

```java

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin")
public String adminOnly() {
    return "Admin content";
}
```

Flow:
1️⃣ Token contains roles
2️⃣ JWT filter extracts roles
3️⃣ Spring Security checks `@PreAuthorize`
4️⃣ Access allowed/denied

---

# 🎯 Interview Answers

### ✅ Access vs Refresh Token

> “Access tokens are short-lived JWTs used to access protected APIs, while refresh tokens are
> long-lived credentials used to obtain new access tokens without forcing the user to log in again.
> This improves security by limiting the window of token misuse while maintaining good user
> experience.”

### ✅ Roles in JWT

> “Roles are stored as claims in the JWT payload, typically under a key like ‘roles’. During
> authentication, these roles are embedded into the token, and on each request, the JWT filter
> extracts them and converts them into Spring Security authorities, enabling role-based access
> control
> using annotations like `@PreAuthorize`.”

---

If you want next, I can explain:
👉 **Where to store tokens on frontend (Web vs Mobile)**
👉 **How to revoke JWT tokens (logout problem)**
👉 **Complete diagram of Spring Security + JWT flow**

> [⬆️ Back to Top / Close](#securerestapiflow)
</blockquote>
</details>

---

## 🎯 Explain Spring Security Filter Chain

Every request passes through a chain of security filters before reaching the controller. JWT filter
extracts and validates token, sets authentication in SecurityContext. If authentication fails,
request is blocked.

<div id=“authdata”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🧠 Where Is User Data Actually Stored?</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>

Bingo — this is the **exact mental gap** most people have with JWT.
“Server is stateless… but user data must live somewhere, right?”
YES. Stateless **authentication**, not stateless **application**.

Let’s break it cleanly.

---

# 🧠 Where Is User Data Actually Stored?

JWT does **NOT** store full user data.
JWT is just a **proof of identity**, not a database replacement.

![Image](https://www.miniorange.com/blog/assets/2023/jwt-structure.webp)

![Image](https://images.ctfassets.net/23aumh6u8s0i/RqMA41JEHFthu4sEZ5zd4/8f6438a08464257c4787a9a221d8d4b2/4)

![Image](https://bs-uploads.toptal.io/blackfish-uploads/uploaded_file/file/412345/image-1602672495860.085-952930c83f53503d7e84d1371bec3775.png)

![Image](https://miro.medium.com/1%2AUtz_wEXQWYcRcnWh9cO6-g.png)

## 🗄️ 1️⃣ User Data Lives in the DATABASE

Your real user info is stored in DB like always:

| id | username | password (hashed) | role  |
|----|----------|-------------------|-------|
| 1  | vishnu   | $2a$10$XyZ...     | ADMIN |

👉 This never changes because of JWT.

---

# 🔐 Then What Does JWT Actually Store?

JWT contains **only essential identity info**, like:

```json
{
  "sub": "vishnu",
  "roles": [
    "ROLE_ADMIN"
  ],
  "exp": 1707033600
}
```

So JWT says:

> “This token was issued by the server for user **vishnu** who has **ADMIN** role, and it hasn’t
> expired.”

It does NOT contain:
❌ Password
❌ Full profile
❌ Address, phone, etc.

---

# 🔄 How Validation Works Without Session

Let’s follow one API request:

### Step 1 — Client Sends Token

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Step 2 — Server Verifies Token Signature

Using secret key:

✔ Token not tampered
✔ Token issued by this server
✔ Token not expired

At this stage, server trusts:

> “This token belongs to user 'vishnu'”

---

## ❓ Does Server Check Database Every Time?

There are **TWO approaches**:

### 🟢 Approach A — Fully Stateless (Common in microservices)

Server does **NOT** query DB each request.
It trusts JWT completely.

Flow:
1️⃣ Verify signature
2️⃣ Extract username & roles
3️⃣ Create Authentication object
4️⃣ Allow access

Fast ⚡ Scalable 📈
But if user role changes → old token still valid until expiry

---

### 🟡 Approach B — Hybrid (More Secure)

After extracting username from token:

```java
UserDetails userDetails = userDetailsService.loadUserByUsername(username);
```

This fetches latest data from DB.

Then check:
✔ User still exists
✔ Account not locked
✔ Roles not changed

More secure 🔒 Slightly less scalable

---

# 🎯 So Where Is “Session” Stored?

Nowhere on server.

Instead:

| Traditional Login                     | JWT Login                 |
|---------------------------------------|---------------------------|
| Server stores session in memory/Redis | Client stores token       |
| Server remembers user                 | Token carries identity    |
| Server checks session ID              | Server verifies signature |

JWT = **Self-contained identity card**

---

# 🧩 Real-World Analogy

Think of JWT like an **airport boarding pass**.

* Airport DB = passenger database
* Boarding pass (JWT) = proof you are allowed
* Security checks pass validity (signature)
* They don't call airline DB for every passenger

But if boarding pass expires → not allowed

---

# 🧠 Key Line for Interviews

> “User data such as credentials and roles are stored in the database. The JWT only contains minimal
> identity claims like username and roles. When a request arrives, the server validates the token’s
> signature and expiry. Depending on design, the server may either trust the token fully (stateless)
> or
> optionally load fresh user details from the database for extra validation. No session data is
> stored
> server-side.”

---

If you'd like, next I can explain:
👉 **What happens when user logs out in JWT systems**
👉 **How to immediately revoke a token**
👉 **Where to store JWT on frontend securely**


> [⬆️ Back to Top / Close](#authdata)
</blockquote>
</details>

---

## 🎯 Common HTTP Status Codes

| Code | Meaning      |
|------|--------------|
| 200  | OK           |
| 201  | Created      |
| 400  | Bad Request  |
| 401  | Unauthorized |
| 403  | Forbidden    |
| 404  | Not Found    |
| 500  | Server Error |

---
Read part of -> [Day1_SpringBoot_REST_Security_JWT_Guide_Part2.md](Day1_SpringBoot_REST_Security_JWT_Guide_Part2.md)

# 🏁 DAY 1 RESULT

After mastering this, you can confidently say:

“I have built secure REST APIs using Spring Boot with JWT-based stateless authentication, role-based
authorization, request validation, global exception handling, and layered architecture.”
