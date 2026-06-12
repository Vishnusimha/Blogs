# 🧠 DAY 1 MASTERY

# Spring Boot REST APIs + Spring Security + JWT

Think of what you listed as **Tier-1 Fundamentals**. Below are the **Tier-2 Topics** that often
separate *“I learned Spring Boot”* from *“I’ve built real systems.”*

---

# ✅ You Already Covered (Solid Foundation)

✔ REST Controllers
✔ Layered architecture
✔ DTO vs Entity
✔ Validation
✔ Exception handling
✔ JWT authentication
✔ Role-based authorization
✔ Stateless security

This is excellent. Now let’s upgrade.

---

# 🚨 Important Topics You MISSED (Add These)

## 1️⃣ 🔒 **CORS (Cross-Origin Resource Sharing)**

If frontend (React/Angular/Mobile) calls backend, CORS must be configured.

```java

@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("*");
        }
    };
}
```

📌 Interview question:
**“How do you allow frontend apps to call your secured APIs?”**

<div id=“cors”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍What is CORS? in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>

Great question! When a frontend application (React, Angular, or Mobile) needs to call a secured
backend API, **CORS (Cross-Origin Resource Sharing)** and **API security** are two critical aspects
to address. Let’s break down the solution in detail:

---

## 1. **What is CORS?**

CORS is a security mechanism enforced by web browsers to prevent malicious websites from making
unauthorized requests to a different domain than the one that served the web page. If your
frontend (e.g., `http://localhost:3000`) tries to call a backend API (e.g.,
`http://localhost:8080`), the browser will block the request unless the backend explicitly allows it
via CORS headers.

---

## 2. **How to Configure CORS in Spring Boot**

The code snippet you provided is a Spring Boot configuration to allow CORS for your backend API:

```java

@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("*");
        }
    };
}
```

### **Explanation:**

- **`@Bean`**: This annotation tells Spring to manage this configuration as a bean.
- **`WebMvcConfigurer`**: An interface for customizing Spring MVC configuration.
- **`addCorsMappings`**: This method allows you to define CORS rules for your endpoints.
- **`registry.addMapping("/**")`**: Applies CORS rules to all endpoints (`/**`).
- **`.allowedOrigins("http://localhost:3000")`**: Only allows requests from the frontend running on
  `http://localhost:3000`.
- **`.allowedMethods("*")`**: Allows all HTTP methods (GET, POST, PUT, DELETE, etc.).

---

## 3. **Securing APIs for Frontend Calls**

CORS alone is **not security**. It only tells the browser which domains are allowed to call your
API. To secure your API, you need additional layers:

### **A. Authentication & Authorization**

- **JWT (JSON Web Tokens)**: Frontend sends a JWT token in the `Authorization` header for each
  request. The backend validates the token before processing the request.
- **OAuth 2.0**: For more complex scenarios, use OAuth 2.0 with providers like Google, Facebook, or
  your own auth server.
- **Spring Security**: Integrate Spring Security to enforce authentication and authorization rules.

### **B. HTTPS**

- Always use HTTPS to encrypt data in transit. Never allow HTTP in production.

### **C. CSRF Protection**

- If your frontend and backend share the same domain (e.g., both on `example.com`), enable CSRF
  protection to prevent Cross-Site Request Forgery attacks.

### **D. Rate Limiting**

- Use libraries like `bucket4j` or Spring Cloud Gateway to limit API calls and prevent abuse.

---

## 4. **Example: Secured API with CORS and JWT**

Here’s how you might configure a Spring Boot API to:

1. Allow CORS for a specific frontend domain.
2. Secure endpoints with JWT.

### **Step 1: CORS Configuration (as above)**

```java

@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    };
}
```

### **Step 2: Spring Security Configuration**

```java

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

### **Step 3: JWT Authentication Filter**

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && jwtUtil.validateToken(token)) {
            Authentication auth = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
```

---

## 5. **Frontend Configuration**

In your frontend (React/Angular/Mobile), ensure:

- You include the JWT token in the `Authorization` header for each API call.
- You handle CORS errors gracefully (e.g., show a user-friendly message if the request is blocked).

**Example (React with Axios):**

```javascript
axios.get('http://localhost:8080/api/data', {
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  }
});
```

---

## 6. **Common Pitfalls**

- **Wildcard Origins (`*`)**: Avoid using `.allowedOrigins("*")` in production. Always specify exact
  domains.
- **Missing Headers**: Ensure your backend sends the correct CORS headers (
  `Access-Control-Allow-Origin`, `Access-Control-Allow-Methods`, etc.).
- **Preflight Requests**: For complex requests (e.g., PUT, DELETE with custom headers), the browser
  sends a preflight `OPTIONS` request. Your backend must handle this.

---

## 7. **Summary**

- **CORS**: Configure your backend to allow requests from your frontend domain.
- **Security**: Use JWT, OAuth, or session-based auth to secure your API.
- **HTTPS**: Always encrypt traffic.
- **Frontend**: Include auth tokens in API calls and handle CORS errors.

Would you like a deeper dive into any specific part, such as JWT implementation or Spring Security
configuration?

> [⬆️ Back to Top / Close](#cors)
</blockquote>
</details>

---

## 2️⃣ 🧾 **Logging (Production Must-Have)**

You should never rely only on `System.out.println`.

```java
private static final Logger log = LoggerFactory.getLogger(UserService.class);

log.

info("Creating user {}",user.getUsername());
        log.

error("User creation failed",ex);
```

Tools: **SLF4J + Logback**

---

## 3️⃣ 🛡 **API Documentation (Swagger / OpenAPI)**

Almost every company uses this.

Dependency:
`springdoc-openapi-starter-webmvc-ui`

Access docs at:

```
/swagger-ui.html
```

<div id=“swagger”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍What is OPENAPI and SWAGGER? in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>
Great question, Vishnu Simha!

**Swagger** and **OpenAPI** are closely related but not exactly the same. Here’s a clear breakdown:

---

### **1. OpenAPI**

- **OpenAPI** is a **specification** (a standard) for describing RESTful APIs.
- It defines a language-agnostic way to document APIs, including endpoints, request/response
  formats, authentication, and more.
- The specification is maintained by the **OpenAPI Initiative** (a Linux Foundation project).
- The latest version is **OpenAPI 3.x**.

---

### **2. Swagger**

- **Swagger** is a set of **tools** built around the OpenAPI specification.
- It includes:
    - **Swagger UI**: A visual interface to interact with and test APIs.
    - **Swagger Editor**: A tool to write and edit OpenAPI specs.
    - **Swagger Codegen**: A tool to generate server stubs and client SDKs from OpenAPI specs.
- Swagger was originally created by SmartBear Software and later donated to the OpenAPI Initiative.

---

### **Key Relationship**

- **OpenAPI** is the **standard/specification**.
- **Swagger** is a **toolset** that implements the OpenAPI specification.

---

### **Analogy**

Think of OpenAPI as the "blueprint" for how to document an API, and Swagger as the "toolkit" to
create, visualize, and work with that blueprint.

---

### **Which One Should You Use?**

- If you want to **document your API**, you write an **OpenAPI specification** (usually in YAML or
  JSON).
- If you want to **visualize, test, or generate code** from that spec, you use **Swagger tools** (
  like Swagger UI or Swagger Codegen).

Here’s a **practical, step-by-step guide** for developers on how to write and use **Swagger/OpenAPI
documentation** in a Spring Boot project using `springdoc-openapi-starter-webmvc-ui`. This is what
every developer should know:

---

## **1. Why Swagger/OpenAPI?**

- **Standardized API Documentation**: Automatically generates interactive, up-to-date API docs.
- **Developer Efficiency**: No need to manually update documentation; it’s always in sync with your
  code.
- **Interactive Testing**: Test APIs directly from the browser.
- **Client SDK Generation**: Generate client libraries for multiple languages.

---

## **2. Setup in Spring Boot**

### **Add Dependency**

Add this to your `pom.xml` (Maven) or `build.gradle` (Gradle):

#### **Maven:**

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version> <!-- Use the latest version -->
</dependency>
```

#### **Gradle:**

```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0'
```

---

## **3. Access Swagger UI**

Once the dependency is added and your Spring Boot app is running:

- Open your browser and go to:
  **`http://localhost:<port>/swagger-ui.html`**
  (e.g., `http://localhost:8080/swagger-ui.html`)

You’ll see the **Swagger UI** with all your API endpoints documented and ready to test.

---

## **4. How to Document Your APIs**

### **A. Basic Annotations**

Use **annotations** in your Spring Boot controllers to document your APIs. Here’s how:

#### **Example Controller:**

```java
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book", description = "Book management APIs")
public class BookController {

    @Operation(
            summary = "Get all books",
            description = "Returns a list of all books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of books",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class))))
            }
    )
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(
            summary = "Add a new book",
            description = "Adds a new book to the bookstore",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }
}
```

#### **Book Model:**

```java

@Schema(description = "Book details")
public class Book {
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Schema(description = "Title of the book", example = "The Great Gatsby")
    private String title;

    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald")
    private String author;

    @Schema(description = "Year the book was published", example = "1925")
    private int publishedYear;

    // Getters and setters
}
```

---

### **B. Key Annotations**

| Annotation     | Purpose                                                       |
|----------------|---------------------------------------------------------------|
| `@Tag`         | Groups related APIs (e.g., "Book", "User").                   |
| `@Operation`   | Describes an API endpoint (summary, description, responses).  |
| `@ApiResponse` | Documents possible responses (e.g., 200 OK, 400 Bad Request). |
| `@Schema`      | Describes model properties (e.g., `Book` class fields).       |
| `@Content`     | Defines the structure of the response body.                   |

---

## **5. Customize Swagger UI**

You can customize the Swagger UI page by adding this to your `application.properties` or
`application.yml`:

#### **application.yml:**

```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /api-docs
  packages-to-scan: com.yourpackage.controllers
```

---

## **6. Best Practices**

1. **Always Document Responses**: Use `@ApiResponse` to document all possible responses (200, 400,
   404, etc.).
2. **Use Examples**: Provide examples for request/response bodies using `@Schema(example = "...")`.
3. **Group APIs Logically**: Use `@Tag` to group related APIs (e.g., "Book", "User").
4. **Keep Docs Updated**: Since docs are generated from code, they’re always up-to-date.
5. **Test Your APIs**: Use the "Try it out" button in Swagger UI to test endpoints.

---

## **7. Example Output in Swagger UI**

When you run your app and open `http://localhost:8080/swagger-ui.html`, you’ll see:

- A list of all APIs grouped by tags (e.g., "Book").
- Each endpoint with its summary, description, and request/response details.
- A "Try it out" button to test the API directly from the browser.

---

## **8. Why Every Dev Must Know This**

- **Saves Time**: No need to write separate documentation.
- **Improves Collaboration**: Frontend and backend teams can easily understand APIs.
- **Reduces Errors**: Clear documentation reduces misunderstandings.
- **Professionalism**: Almost every company uses Swagger/OpenAPI for API documentation.

---
---

> [⬆️ Back to Top / Close](#swagger)
</blockquote>
</details>

---

## 4️⃣ 🧱 **Response Wrapping / Standard API Response**

Instead of random JSON:

```json
{
  "data": {
    ...
  },
  "message": "Success",
  "status": 200
}
```

Create a wrapper:

```java

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
```

---

## 5️⃣ ⏳ **Pagination & Sorting (You mentioned, but need code)**

```java

@GetMapping
public Page<User> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return repo.findAll(PageRequest.of(page, size, Sort.by("username")));
}
```

Interviewers love this.

<div id=“pagination”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍📄 Pagination & Sorting — in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>

---

# 📄 Pagination & Sorting — What Interviewers Ask

![Image](https://assets.bytebytego.com/diagrams/0076-api-pagination-101.png)

![Image](https://miro.medium.com/v2/resize%3Afit%3A1200/1%2AlRYT83RhbWw-4_Uu9cTdXg.png)

![Image](https://substackcdn.com/image/fetch/%24s_%21rGMQ%21%2Cf_auto%2Cq_auto%3Agood%2Cfl_progressive%3Asteep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F4763550d-1639-4e68-9fb7-65758c370693_2774x758.png)

![Image](https://media.licdn.com/dms/image/v2/D5612AQECv1zHmrfDbg/article-cover_image-shrink_600_2000/article-cover_image-shrink_600_2000/0/1678215021711?e=2147483647\&t=ZxKFDpWObWWijL9xIk4EHYlHf_Zxy_-eU6nhRhDOadk\&v=beta)

## 🔹 1️⃣ “Why do we need pagination?”

**Expected Answer:**

> “Pagination is used to limit the number of records returned in a single API response. It improves
> performance, reduces memory usage, and avoids overwhelming the client when dealing with large
> datasets.”

Bonus mention:

* Faster DB queries
* Better user experience on UI tables

---

## 🔹 2️⃣ “How do you implement pagination in Spring Boot?”

They want to hear about **Spring Data JPA’s `Pageable`**.

### Repository

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

### Controller

```java

@GetMapping("/users")
public Page<User> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size);
    return userRepository.findAll(pageable);
}
```

---

## 🔹 3️⃣ “What does the response look like?”

Spring returns a `Page<T>` object:

```json
{
  "content": [
    {
      "id": 1,
      "username": "vishnu"
    }
  ],
  "pageable": {
    ...
  },
  "totalPages": 5,
  "totalElements": 42,
  "last": false,
  "size": 10,
  "number": 0
}
```

Interviewers like hearing:

> “Page gives metadata like total elements and total pages.”

---

## 🔹 4️⃣ “How do you add sorting?”

```java
Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
```

Or from request:

```java

@GetMapping("/users")
public Page<User> getUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
}
```

URL example:

```
/users?page=0&size=5&sort=username,asc
```

<div id=“paginationelo”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍📄 Pagination & Sorting for such request how the internal methods would be? — in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>

Perfect — let’s go **under the hood** for this request:

```
GET /users?page=0&size=5&sort=username,asc
```

You want to understand **what internal methods Spring calls** and how it turns this into a DB query.

---

# 🔄 High-Level Flow

![Image](https://terasolunaorg.github.io/guideline/5.2.0.RELEASE/en/_images/RequestLifecycle.png)

![Image](https://terasolunaorg.github.io/guideline/5.3.0.RELEASE/en/_images/pagination-overview_flow.png)

![Image](https://miro.medium.com/0%2AzScVClCCjn6jHSSC.gif)

![Image](https://www.researchgate.net/publication/341151097/figure/fig2/AS%3A887752266616836%401588668042046/Fig-2-Architecture-flow-of-spring-boot-Applications-Spring-boot-uses-all-the-features.ppm)

```
HTTP Request
   ↓
DispatcherServlet
   ↓
Handler Mapping
   ↓
Controller Method
   ↓
Spring Data Repository
   ↓
Hibernate/JPA
   ↓
Database (LIMIT + OFFSET)
```

---

# 🧩 Step-by-Step Internal Execution

## 1️⃣ Request Hits DispatcherServlet

Spring’s **front controller** receives:

```
/users?page=0&size=5&sort=username,asc
```

It finds this controller:

```java

@GetMapping("/users")
public Page<User> getUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
}
```

But wait — **you didn’t create Pageable manually**. So how did it appear?

---

## 2️⃣ Spring Creates `Pageable` Object Automatically

Spring uses a class called:

### 👉 `PageableHandlerMethodArgumentResolver`

This component:

* Reads `page=0`
* Reads `size=5`
* Reads `sort=username,asc`
* Builds this internally:

```java
Pageable pageable = PageRequest.of(
        0,                      // page number
        5,                      // page size
        Sort.by("username").ascending()
);
```

This is passed into your controller method.

---

## 3️⃣ Controller Calls Repository

```java
userRepository.findAll(pageable);
```

This method comes from:

### 👉 `JpaRepository`

Internally, this goes to:

### 👉 `SimpleJpaRepository.findAll(Pageable pageable)`

---

## 4️⃣ Inside `SimpleJpaRepository`

Spring Data now builds a query using JPA Criteria API.

Internally it does something like:

```java
TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
query.

setFirstResult(pageable.getOffset()); // OFFSET
        query.

setMaxResults(pageable.getPageSize()); // LIMIT
```

### For your request:

```
page = 0
size = 5
offset = page * size = 0 * 5 = 0
limit = 5
```

---

## 5️⃣ Generated SQL Query

Hibernate converts this into SQL:

```sql
SELECT * 
FROM users 
ORDER BY username ASC
LIMIT 5 OFFSET 0;
```

---

## 6️⃣ Count Query (For Total Pages)

Because you requested a **Page**, Spring also runs:

```sql
SELECT COUNT(*) FROM users;
```

This helps return metadata:

```json
{
  "totalElements": 42,
  "totalPages": 9
}
```

---

## 7️⃣ Spring Wraps Result into `PageImpl`

Spring builds:

```java
new PageImpl<>(contentList,pageable,totalCount);
```

This object contains:

* Data (`content`)
* Page number
* Total pages
* Total elements

Returned to controller → converted to JSON → sent to client.

---

# 🧠 Internal Classes Involved (Interview Gold)

| Responsibility       | Class                                   |
|----------------------|-----------------------------------------|
| Front Controller     | `DispatcherServlet`                     |
| Mapping URL → Method | `RequestMappingHandlerMapping`          |
| Creating Pageable    | `PageableHandlerMethodArgumentResolver` |
| JPA Implementation   | `SimpleJpaRepository`                   |
| Query Execution      | `EntityManager`                         |
| SQL Generation       | Hibernate                               |

Dropping 2–3 of these names in interview = ⭐⭐⭐

---

# 🎯 Interview-Ready Answer

> “When a request like `/users?page=0&size=5&sort=username,asc` is received, Spring’s
> DispatcherServlet routes it to the controller. The `PageableHandlerMethodArgumentResolver`
> automatically converts query parameters into a `PageRequest` object. The controller passes this to
`JpaRepository.findAll(pageable)`, which is implemented by `SimpleJpaRepository`. Internally, JPA
> sets the offset and limit on the query and Hibernate generates SQL with `LIMIT` and `OFFSET`. A
> separate count query is also executed to calculate total pages, and results are returned inside a
`PageImpl` object.”

---

> [⬆️ Back to Top / Close](#paginationelo)
</blockquote>
</details>

---

## 🔹 5️⃣ “What is the difference between Page and Slice?”

| Page                 | Slice                   |
|----------------------|-------------------------|
| Knows total count    | Doesn’t know total      |
| Extra DB count query | Faster                  |
| Used in admin panels | Used in infinite scroll |

Great bonus answer:

> “Slice is more efficient when total count is not required.”

---

## 🔹 6️⃣ “How does pagination work at DB level?”

They want to hear **LIMIT & OFFSET**.

Example SQL:

```sql
SELECT * FROM users ORDER BY username LIMIT 10 OFFSET 20;
```

Spring Data converts `Pageable` → this query.

---

## 🔹 7️⃣ “Can pagination affect performance?”

Yes, especially large OFFSET.

Smart answer:

> “Offset pagination can become slow for large datasets because the database must skip many rows. In
> such cases, keyset pagination (using WHERE id > lastId) is more efficient.”

🔥 This answer impresses senior interviewers.

---

## 🔹 8️⃣ “How do you return pagination in a custom API response?”

Instead of returning `Page` directly:

```java
Page<User> page = repo.findAll(pageable);

Map<String, Object> response = Map.of(
        "data", page.getContent(),
        "currentPage", page.getNumber(),
        "totalItems", page.getTotalElements(),
        "totalPages", page.getTotalPages()
);

return ResponseEntity.

ok(response);
```

---

# 🎯 Interview-Ready Summary Answer

> “In Spring Boot, pagination is implemented using Spring Data JPA’s Pageable interface. The client
> sends page number, size, and sorting parameters, and the repository returns a Page object that
> includes both the data and metadata like total pages and total elements. Under the hood, this
> translates to SQL queries with LIMIT and OFFSET. For large datasets, keyset pagination can be used
> for better performance.”

---

> [⬆️ Back to Top / Close](#pagination)
</blockquote>
</details>

<div id=“pagination”></div>
<details>
<summary><b>🔴🔴🔴 <font color="red">🌍📄 Pagination & Sorting — in Spring Boot</font> 🔴🔴🔴</b></summary>
<br>
<blockquote>

Almost, **Vishnu Simha**, but let’s clarify the terms **offset** and **limit** in the context of *
*pagination** (a common pattern for APIs and databases):

---

### **1. Offset vs. Page**

- **Offset**:
    - Refers to the **number of items to skip** before starting to return results.
    - Example: If you set `offset=10`, the query will skip the first 10 items and start returning
      results from the 11th item.
    - **Not the same as "page"**, but it can be used to implement pagination.

- **Page**:
    - Refers to a **specific group of items** (e.g., "Page 1", "Page 2").
    - To calculate the offset for a given page, you use:
      `offset = (page_number - 1) * items_per_page`
      Example: For page 2 with 10 items per page, `offset = (2-1)*10 = 10`.

---

### **2. Limit vs. Count of Items**

- **Limit**:
    - Refers to the **maximum number of items to return** in a single query.
    - Example: If you set `limit=5`, the query will return up to 5 items.
    - **Same as "count of items"** in the context of a single query.

---

### **3. How Offset and Limit Work Together**

When you combine `offset` and `limit`, you get **pagination**:

- **`offset`**: How many items to skip.
- **`limit`**: How many items to return after skipping.

**Example**:
If you have 100 items and want to fetch "Page 3" with 10 items per page:

- `offset = (3-1)*10 = 20`
- `limit = 10`
- The query will skip the first 20 items and return the next 10 (items 21-30).

---

### **4. Example in SQL**

```sql
-- Fetch page 3 with 10 items per page
SELECT * FROM books
ORDER BY id
OFFSET 20 LIMIT 10;
```

---

### **5. Example in API Requests**

Many APIs use query parameters like:

- `?page=3&limit=10`
- `?offset=20&limit=10`

Both achieve the same result, but `page` is more user-friendly, while `offset` is more
database-friendly.

---

### **6. Summary Table**

| Term   | Meaning                               | Example                       |
|--------|---------------------------------------|-------------------------------|
| Offset | Number of items to skip               | `offset=10` (skip 10 items)   |
| Page   | Group of items (calculated as offset) | `page=3` (with 10 items/page) |
| Limit  | Maximum number of items to return     | `limit=5` (return 5 items)    |

---

### **7. Key Takeaway**

- **Offset** is not the same as **page**, but you can calculate the offset from the page number.
- **Limit** is the same as the **count of items** to return.
- Together, they enable **pagination** in APIs and databases.

> [⬆️ Back to Top / Close](#pagination)
</blockquote>
</details>

---

## 6️⃣ 🧪 **Basic Testing (Huge Hiring Signal)**

At least know:

```java

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    void shouldCreateUser() {
        User user = new User("test", "pass");
        assertNotNull(service.register(user));
    }
}
```

Tools:

* JUnit 5
* Mockito
* @WebMvcTest

---

## 7️⃣ 🔁 **Password Reset / Forgot Password Flow (Security Depth)**

Shows you understand real-world auth beyond login.

---

## 8️⃣ 🚦 **Rate Limiting / Brute Force Protection (Advanced Security)**

Even just mentioning is impressive.

---

## 9️⃣ 🗂 **Application Properties / Profiles**

```yaml
spring:
  profiles:
    active: dev
```

`application-dev.yml` vs `application-prod.yml`

---

## 🔟 📡 **Actuator (Production Monitoring)**

```
/actuator/health
/actuator/metrics
```

Used in real deployments.

---

# 🎯 Updated “Complete” Topic Checklist

### REST API

✔ Controllers / Services / Repositories
✔ DTO mapping
✔ Validation
✔ Exception handling
✔ Pagination & sorting
✔ Standard API responses
✔ API versioning
✔ CORS

### Security

✔ Spring Security filter chain
✔ JWT authentication
✔ BCrypt password hashing
✔ Role-based access
✔ Access vs Refresh token
✔ Stateless architecture

### Production Readiness

✔ Logging
✔ Swagger/OpenAPI
✔ Profiles & configs
✔ Actuator health checks
✔ Basic testing

---

# 🧠 Final Verdict

You had **~75% of what interviews expect**
Adding the topics above pushes you to **90–95% job-ready backend developer**

---

# 🏁 DAY 1 RESULT

After mastering this, you can confidently say:

“I have built secure REST APIs using Spring Boot with JWT-based stateless authentication, role-based
authorization, request validation, global exception handling, and layered architecture.”
