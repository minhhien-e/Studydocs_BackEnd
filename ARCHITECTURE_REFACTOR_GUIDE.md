# HƯỚNG DẪN CHUYỂN ĐỔI / REFACTOR DỰ ÁN BACKEND THEO KIẾN TRÚC STUDYDOCS

Tài liệu này tổng hợp toàn bộ kiến trúc, cấu trúc thư mục, quy tắc thiết kế và hướng dẫn từng bước (Step-by-Step Refactoring Guide) để chuyển đổi hoặc tái cấu trúc (refactor) một dự án Spring Boot bất kỳ thành cấu trúc **Modular Monolith** tiêu chuẩn của dự án `StudyDocs Backend`.

---

## 1. Tổng Quan Kiến Trúc (Architecture Overview)

Dự án áp dụng mô hình **Modular Monolith** kết hợp **Shared Kernel** và **Infrastructure Layer**:
- **Modular Monolith**: Phân chia codebase thành các module độc lập theo miền nghiệp vụ (`modules/user`, `modules/academic`, `modules/review`, `modules/follow`, `modules/system`). Mỗi module chứa đầy đủ Controller, DTO, Entity, Repository và Service.
- **Shared Kernel**: Tập trung các thành phần dùng chung toàn hệ thống (`shared/dto`, `shared/entity`, `shared/exception`).
- **Infrastructure Layer**: Chứa các dịch vụ hạ tầng kỹ thuật độc lập với logic nghiệp vụ (`infras/storage`).
- **Config Layer**: Quản lý cấu hình Spring Security, JWT Filter, CORS và dữ liệu khởi tạo (`config/`).

---

## 2. Thẻ Công Nghệ & Thư Viện Chuẩn (Tech Stack)

- **Java Version**: Java 17
- **Framework**: Spring Boot `3.3.4`
- **Build Tool**: Gradle (Groovy DSL)
- **Database & Persistence**: MySQL (`mysql-connector-j`), Spring Data JPA, Hibernate, Flyway (`flyway-core`, `flyway-mysql`)
- **Security & Token**: Spring Boot Security, OAuth2 Resource Server & Client, JJWT (`io.jsonwebtoken:jjwt-api:0.11.5`)
- **Validation & Tooling**: Spring Boot Validation (`@Valid`), Spring Boot Actuator, Dotenv (`me.paulschwarz:spring-dotenv:4.0.0`)
- **Code Generation & Mapping**: Lombok, MapStruct (`1.5.5.Final` với `lombok-mapstruct-binding:0.2.0`)

---

## 3. Cấu Trúc Thư Mục Tiêu Chuẩn (Folder & Package Structure)

```text
src/main/java/com/<your-package-name>/
├── StudyDocsApplication.java            # Class main chạy ứng dụng
│
├── config/                              # Cấu hình hệ thống & Security
│   ├── SecurityConfig.java              # Cấu hình Spring Security, Permitted URLs, Stateless Session
│   ├── JwtAuthenticationFilter.java     # Filter kiểm tra & giải mã JWT Bearer Token
│   ├── WebCorsConfig.java               # Cấu hình CORS cho Frontend
│   └── DataInitializer.java             # Khởi tạo dữ liệu mẫu khi ứng dụng khởi chạy
│
├── infras/                              # Tầng Hạ tầng Kỹ thuật (Infrastructure)
│   └── storage/
│       ├── FileStorageService.java      # Interface quản lý lưu trữ tệp
│       └── LocalFileStorageServiceImpl.java # Implementation lưu tệp cục bộ
│
├── shared/                              # Tầng Dùng chung (Shared Kernel)
│   ├── dto/
│   │   └── ApiResponse.java             # Standard REST API Response Envelope
│   ├── entity/
│   │   └── BaseEntity.java              # MappedSuperclass chứa createdAt, updatedAt (JPA Auditing)
│   └── exception/
│       ├── AppException.java            # Custom Business Runtime Exception
│       ├── ErrorCode.java               # Enum chứa mã lỗi, thông điệp và HttpStatus
│       └── GlobalExceptionHandler.java  # Controller Advice bắt & chuẩn hóa toàn bộ Exception
│
└── modules/                             # Các Module Nghiệp vụ (Modular Monolith)
    ├── academic/                        # Module Quản lý Học thuật & Tài liệu
    │   ├── controller/                  # REST Controllers (e.g., AcademicController, DocumentController)
    │   ├── dto/                         # Data Transfer Objects
    │   ├── entity/                      # JPA Entities (University, Faculty, Subject, Document...)
    │   ├── repository/                  # Spring Data JPA Repositories
    │   └── service/                     # Service Interfaces & Implementations (service/impl/)
    ├── follow/                          # Module Theo dõi (User Follow)
    ├── review/                          # Module Đánh giá & Phản hồi (Reviews/Reactions)
    ├── system/                          # Module Hệ thống (Notifications, Media Assets)
    └── user/                            # Module Người dùng & Xác thực (Auth, User, Role)
```

---

## 4. Chi Tiết Các Thành Phần Cốt Lõi (Core Building Blocks)

### 4.1. Chuẩn hóa API Response Envelope (`ApiResponse<T>`)
Tất cả các REST Controller đều trả về duy nhất 1 kiểu cấu trúc response chuẩn:

```java
package com.studydocs.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int statusCode;
    private Integer errorCode;
    private T data;
    private String traceId;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().statusCode(200).data(data).build();
    }

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return ApiResponse.<T>builder().statusCode(200).data(data).traceId(traceId).build();
    }

    public static <T> ApiResponse<T> error(int statusCode, Integer errorCode, String traceId) {
        return ApiResponse.<T>builder().statusCode(statusCode).errorCode(errorCode).traceId(traceId).build();
    }
}
```

### 4.2. Quản lý Lỗi Tập Trung (`AppException`, `ErrorCode`, `GlobalExceptionHandler`)

- **`ErrorCode.java`**: Khai báo enum chứa `code`, `message`, `httpStatus`.
- **`AppException.java`**: Runtime exception nhận vào một `ErrorCode`.
- **`GlobalExceptionHandler.java`**: `@RestControllerAdvice` xử lý:
  1. `AppException` -> Đọc `ErrorCode` để trả về status tương ứng.
  2. `MethodArgumentNotValidException` -> Xử lý lỗi validation `@Valid`.
  3. `AccessDeniedException` -> Trả về HTTP 403 Forbidden.
  4. `Exception` -> Fallback lỗi 500 Uncategorized.
  5. Luôn tự động gắn hoặc sinh `X-Trace-Id` (UUID) để dễ dàng truy vết log.

### 4.3. Quản lý Thực Thể Base (`BaseEntity`)
Tất cả JPA Entity cần lưu mốc thời gian tạo/sửa đều kế thừa `BaseEntity`:

```java
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```
*(Đảm bảo đã kích hoạt `@EnableJpaAuditing` trên Main Application Class hoặc JPA Config).*

### 4.4. Bảo Mật & Cấu Hình Security (`SecurityConfig` & JWT)

- Khai báo context-path chung `/api/v1` trong `application.yml`.
- Session Management sử dụng chiến lược `SessionCreationPolicy.STATELESS`.
- Endpoint công khai phân loại theo tiền tố URL (vd: `/user/public/**`, `/education/documents/public/**`, `/swagger-ui/**`, `/actuator/**`).
- Các request cần bảo mật đi qua `JwtAuthenticationFilter` để trích xuất Bearer Token từ header `Authorization`.

---

## 5. Quy Trình 6 Bước Refactor Dự Án Mới Theo Kiến Trúc Này

Khi bạn xóa dự án cũ và đưa dự án mới vào, hãy thực hiện lần lượt 6 bước sau:

### Bước 1: Chuẩn bị File Cấu Hình Dự Án
1. **`build.gradle`**: Cập nhật danh sách dependency chuẩn (Spring Web, Spring Security, JPA, MySQL, Flyway, JJWT, Validation, Lombok, MapStruct).
2. **`application.yml`**:
   ```yaml
   server:
     port: 8090
     servlet:
       context-path: /api/v1

   spring:
     application:
       name: studydocs-backend
     datasource:
       url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/studydocs?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
       username: ${SPRING_DATASOURCE_USERNAME:root}
       password: ${SPRING_DATASOURCE_PASSWORD:123456}
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: false

   jwt:
     secret: ${JWT_SECRET:studydocsSecretKeySuperLongAndSecureForJwtSigning1234567890}
     expiration-ms: 86400000
     refresh-expiration-ms: 604800000

   file:
     upload-dir: ${UPLOAD_DIR:uploads}
   ```

### Bước 2: Dựng Bộ Khung Dùng Chung (`shared/`)
1. Tạo package `com.<your_project>.shared.dto` và thêm file `ApiResponse.java`.
2. Tạo package `com.<your_project>.shared.entity` và thêm file `BaseEntity.java`.
3. Tạo package `com.<your_project>.shared.exception` và thêm:
   - `ErrorCode.java` (bổ sung các enum mã lỗi phù hợp với dự án mới).
   - `AppException.java`.
   - `GlobalExceptionHandler.java`.

### Bước 3: Dựng Tầng Cấu Hình & Security (`config/`)
1. Tạo `SecurityConfig.java` triển khai `SecurityFilterChain` cấp quyền mở/khóa API.
2. Tạo `JwtAuthenticationFilter.java` kiểm tra JWT Token.
3. Tạo `WebCorsConfig.java` cho phép truy cập từ Frontend.

### Bước 4: Dựng Tầng Hạ Tầng (`infras/`)
1. Tạo gói `infras/storage/` chứa `FileStorageService` và `LocalFileStorageServiceImpl` để xử lý upload/download file.

### Bước 5: Phân Chia Module Nghiệp Vụ (`modules/`)
Thay vì chia theo tầng truyền thống (`controllers/`, `services/`, `entities/` nằm ở ngoài cùng), hãy nhóm toàn bộ code theo **Domain Module**:
1. Xác định các miền nghiệp vụ của dự án mới (ví dụ: `user`, `order`, `product`, `notification`...).
2. Với mỗi module `<domain>`, tạo thư mục con:
   - `modules/<domain>/controller`
   - `modules/<domain>/dto`
   - `modules/<domain>/entity`
   - `modules/<domain>/repository`
   - `modules/<domain>/service`
   - `modules/<domain>/service/impl`
3. Refactor các Entity kéo dài từ `BaseEntity`.
4. Refactor các Controller: Thay đổi kiểu trả về thành `ApiResponse<T>`, bỏ việc try-catch thủ công trong Controller, ném ngoại lệ `throw new AppException(ErrorCode.RESOURCE_NOT_FOUND)` ở Service layer.

### Bước 6: Kiểm Thử & Xác Nhận (Verification)
1. Chạy thử ứng dụng Spring Boot: `./gradlew bootRun`.
2. Kiểm tra các endpoint với Newman / Postman hoặc cURL, đảm bảo:
   - Tất cả kết quả trả về chuẩn dạng `{ "statusCode": 200, "data": ... }`.
   - Tất cả lỗi được ném ra chuẩn dạng `{ "statusCode": 4xx/5xx, "errorCode": 100x, "traceId": "..." }`.

---
*Tài liệu này được tự động trích xuất và hệ thống hóa từ codebase `StudyDocs Backend`.*
