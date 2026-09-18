# Studydocs BackEnd

Dự án BackEnd cho ứng dụng Studydocs, cung cấp các API phục vụ cho việc quản lý tài liệu, người dùng, xác thực, và xử lý tài liệu PDF.

## 🚀 Công nghệ sử dụng

- **Ngôn ngữ:** Java 17
- **Framework chính:** Spring Boot 3.3.4
- **Quản lý dependencies và build:** Gradle
- **Cơ sở dữ liệu:** MySQL 8.0 (Tương tác qua Spring Data JPA)
- **Migration DB:** Flyway
- **Message Broker:** Apache Kafka (Chạy ở chế độ KRaft, không cần Zookeeper)
- **Bảo mật & Xác thực:** Spring Security, OAuth2 (Google Login), JWT (JSON Web Token)
- **Lưu trữ file/hình ảnh:** Cloudinary
- **Xử lý PDF:** Apache PDFBox
- **Tiện ích:** Lombok, MapStruct (Mapping Object), Spring Boot Mail, Spring Boot Actuator

## ⚙️ Yêu cầu hệ thống (Prerequisites)

Để chạy dự án này trên môi trường local, bạn cần cài đặt:

- **Java Development Kit (JDK) 17**
- **Docker & Docker Compose** (để khởi chạy MySQL và Kafka)
- **Gradle** (Tùy chọn, dự án đã tích hợp sẵn `gradlew`)

## 🛠️ Hướng dẫn cấu hình và cài đặt

### 1. Clone dự án

```bash
git clone <đường-dẫn-repo-của-bạn>
cd Studydocs_BackEnd
```

### 2. Cấu hình biến môi trường

Dự án sử dụng file `.env` để quản lý các biến môi trường.
Tạo một file `.env` ở thư mục gốc của dự án dựa trên file mẫu `.env.example`:

**Trên Windows (PowerShell):**
```powershell
Copy-Item .env.example -Destination .env
```

**Trên Linux/macOS/Git Bash:**
```bash
cp .env.example .env
```

Mở file `.env` và cập nhật các thông tin tương ứng của bạn:

```env
# Xác thực GitHub
GITHUB_ACTOR=your-github-username
GITHUB_TOKEN=your-github-token

# Cấu hình Cloudinary (Lưu trữ ảnh/file)
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Cấu hình JWT
JWT_SECRET=your_jwt_secret_can_be_any_secure_random_string_here
JWT_EXPIRATION_MS=86400000
JWT_REFRESH_EXPIRATION_MS=604800000

# Cấu hình Google OAuth2
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_WEB_CLIENT_ID=your_google_web_client_id

# Cấu hình Email Sender
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 🚀 Hướng dẫn chạy ứng dụng

Có hai cách để chạy dự án: Chạy hoàn toàn bằng Docker hoặc chạy App bằng Gradle và các service phụ trợ (MySQL, Kafka) bằng Docker.

### Cách 1: Chạy App trên Local bằng Gradle (Khuyên dùng cho môi trường Dev)

Trong quá trình phát triển, bạn sẽ muốn chạy DB và Kafka bằng Docker, còn App thì chạy trực tiếp qua IDE hoặc Terminal để dễ dàng debug.

**Bước 1: Khởi động các service phụ trợ (MySQL & Kafka)**

Mở terminal tại thư mục gốc của dự án và chạy lệnh:
```bash
docker-compose up -d mysql kafka
```

**Bước 2: Chạy ứng dụng bằng Gradle wrapper**

Trên Windows:
```bash
.\gradlew.bat bootRun
```

Trên Linux/macOS:
```bash
./gradlew bootRun
```

Ứng dụng sẽ khởi động và lắng nghe ở cổng được cấu hình (ví dụ: `8090`).

### Cách 2: Chạy hoàn toàn bằng Docker Compose (Khuyên dùng cho môi trường Test/Deploy)

Cách này sẽ tự động build image của ứng dụng và khởi chạy cùng với MySQL và Kafka trong các container độc lập.

```bash
docker-compose up -d --build
```

Ứng dụng sẽ chạy ở cổng `8090`. Cơ sở dữ liệu MySQL chạy ở cổng `3306`, Kafka ở cổng `9092`.

Để dừng tất cả các container:
```bash
docker-compose down
```

## 🗄️ Cấu trúc cơ sở dữ liệu (Database Migration)

Dự án sử dụng **Flyway** để tự động cập nhật và migration cơ sở dữ liệu. Khi ứng dụng Spring Boot khởi động, Flyway sẽ tự động chạy các script SQL để tạo bảng và cấu trúc cần thiết trong MySQL.
Hơn nữa, thông qua cấu hình `docker-compose.yml`, script dữ liệu khởi tạo (seed) có thể được tự động load thông qua thư mục `./seed`.

## 🧪 Chạy Test

Để chạy các unit test của dự án:

Trên Windows:
```bash
.\gradlew.bat test
```
Hoặc dùng script có sẵn:
```bash
.\run-tests.sh
```
