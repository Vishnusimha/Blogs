# Spring Boot Testing Guide (2026 Edition)

**Unit Testing + Integration Testing for Controllers, Services, and Repositories**

---

## 1. Types of Testing in Spring Boot

| Type             | Purpose                                  | Tools                     |
|------------------|------------------------------------------|---------------------------|
| Unit Test        | Test a class in isolation                | JUnit 5, Mockito          |
| Slice Test       | Test one Spring layer                    | @WebMvcTest, @DataJpaTest |
| Integration Test | Test multiple layers with Spring Context | @SpringBootTest           |
| E2E Test         | Full system including DB                 | Testcontainers            |

---

# 2. Sample Use Case

We’ll test a simple **User Management** feature.

### User Entity

```java

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
}
```

### Repository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### Service

```java

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
```

### Controller

```java

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
```

---

# 3. Unit Testing the Service Layer

```java

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        User user = new User(null, "Vishnu", "vishnu@mail.com");
        User saved = new User(1L, "Vishnu", "vishnu@mail.com");

        when(userRepository.save(user)).thenReturn(saved);

        User result = userService.createUser(user);

        assertEquals(1L, result.getId());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserByEmail("test@mail.com"));
    }
}
```

---

# 4. Testing the Controller Layer

```java

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User(1L, "Vishnu", "vishnu@mail.com");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
```

---

# 5. Testing the Repository Layer

```java

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindByEmail() {
        User user = new User(null, "Vishnu", "vishnu@mail.com");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("vishnu@mail.com");

        assertTrue(found.isPresent());
    }
}
```

---

# 6. Integration Testing (Full Context)

```java

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void setDatasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldCreateAndFetchUser() throws Exception {
        String json = "{\"name\":\"Vishnu\",\"email\":\"vishnu@mail.com\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/vishnu@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("vishnu@mail.com"));
    }
}
```

---

# 7. Best Practices (2026)

✔ Use **JUnit 5**  
✔ Prefer **MockitoExtension** over Spring context for unit tests  
✔ Use **MockMvc** for controller slice tests  
✔ Use **@DataJpaTest** for repository tests  
✔ Use **Testcontainers** for integration tests  
✔ Avoid loading full context in unit tests  
✔ Name tests clearly: `shouldDoXWhenY`

---

# 8. Testing Pyramid Summary

| Level       | Speed   | Coverage        |
|-------------|---------|-----------------|
| Unit Tests  | Fast    | Small scope     |
| Slice Tests | Medium  | One layer       |
| Integration | Slower  | Multiple layers |
| E2E         | Slowest | Full system     |

---

**This is the production-grade testing structure used in modern Spring Boot applications.**
