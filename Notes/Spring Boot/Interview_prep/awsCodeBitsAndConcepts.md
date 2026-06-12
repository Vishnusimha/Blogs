Perfect — let’s build a **real-world Spring Boot (MVC) file upload system** that:

1️⃣ Accepts large file via REST
2️⃣ Streams it to **Amazon S3**
3️⃣ Downloads file from S3
4️⃣ Sends that file to another external API

This is exactly how production systems handle media/doc uploads.

---

# 🧩 1️⃣ Dependencies

```xml
<!-- AWS SDK v2 -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
</dependency>

<!-- For calling external APIs -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

---

# 🧩 2️⃣ AWS S3 Configuration

```java
@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_SOUTH_1) // change region
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
```

AWS credentials should come from IAM Role / env vars (not hardcoded).

---

# 🧩 3️⃣ Service to Upload File to S3

```java
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {

        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                putRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return key;
    }
}
```

✔ Streams file directly to S3
✔ No large file stored in memory

---

# 🧩 4️⃣ Controller to Receive Upload

```java
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String key = storageService.uploadFile(file);
            return ResponseEntity.ok("Uploaded successfully. File key: " + key);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed");
        }
    }
}
```

Client sends:

```
POST /files/upload
form-data → file=<file>
```

---

# 🧩 5️⃣ Download File from S3

```java
public InputStream downloadFile(String key) {

    GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

    return s3Client.getObject(request);
}
```

---

# 🧩 6️⃣ Send S3 File to Another API

We fetch file from S3 → send to external API using **WebClient**

```java
@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final FileStorageService storageService;
    private final WebClient webClient;

    public String sendFileToAnotherService(String key) throws IOException {

        InputStream fileStream = storageService.downloadFile(key);

        return webClient.post()
                .uri("http://external-service/api/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file",
                        new InputStreamResource(fileStream) {
                            @Override
                            public String getFilename() {
                                return key;
                            }
                        }))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
```

---

# 🧩 7️⃣ WebClient Bean

```java
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
```

---

# 🧠 Flow Summary

### Upload Flow

```
Client → Spring Boot → Streams file → S3
```

### Forwarding Flow

```
S3 → Spring Boot → Streams file → External API
```

---

# 🏆 Production Best Practices

| Concern          | Solution                                  |
| ---------------- | ----------------------------------------- |
| Very large files | Use multipart streaming                   |
| Slow uploads     | Use async processing / SQS                |
| Security         | Pre-signed S3 URLs (better for big files) |
| Scaling          | Don’t store files locally                 |

---

# 🎯 Interview-Ready Summary

> Large files should not be stored on the application server. Instead, the REST API streams the file directly to Amazon S3 using the AWS SDK. When needed, the file can be streamed back from S3 and forwarded to other services. This keeps the application stateless, scalable, and memory-efficient.

---

If you want, next I can show:

✅ Pre-signed URL approach (industry favorite)
✅ Async file processing with SQS
✅ Progress tracking for uploads
