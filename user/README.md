# User Microservice - studydoc

Dự án này là một Microservice quản lý người dùng (User) cho hệ thống **studydoc**, được thiết kế dựa trên các nguyên tắc của **Clean Architecture** (Kiến trúc sạch), **CQRS** (Command Query Responsibility Segregation) và **Domain-Driven Design (DDD)**.

Cấu trúc thư mục được chia thành các module độc lập, giúp việc phát triển, bảo trì và mở rộng hệ thống trở nên dễ dàng, đồng thời tách biệt hoàn toàn phần logic nghiệp vụ (business logic) khỏi các công nghệ hạ tầng (frameworks, databases).

## Cấu trúc các module (Folders)

Dưới đây là vai trò chi tiết của từng thư mục trong dự án:

### 1. `domain` (Lõi Nghiệp Vụ)
Đây là trái tim của hệ thống. Nó chứa tất cả các logic nghiệp vụ (business rules) cốt lõi nhất.
- **Không phụ thuộc** vào bất kỳ framework bên ngoài nào (ngoại trừ một số thư viện siêu nhẹ như Validation/Assert).
- **Chứa:**
  - `vo` (Value Object / Entity): Chứa các lớp cốt lõi như `User`. Entity có khả năng tự kiểm tra tính hợp lệ của chính nó (ví dụ: các trường không được bỏ trống, đúng định dạng).
  - `service`: Chứa các **Domain Service** (`UserDomainService`). Dùng để xử lý các logic nghiệp vụ phức tạp đòi hỏi sự tương tác với DB (ví dụ: kiểm tra username/email có bị trùng hay không).
  - `repository`: Chứa các Interface Repository (`UserRepository`). Đây là các bản hợp đồng định nghĩa cách thao tác với dữ liệu, còn việc thực thi như thế nào sẽ nhường cho layer khác.

### 2. `application` (Tầng Điều Phối / Use Case)
Tầng này đóng vai trò là Orchestrator (người nhạc trưởng), điều phối giữa `domain` và `infrastructure` để thực hiện một use case cụ thể (Ví dụ: Đăng ký người dùng).
- **Chứa:**
  - `command`: Chứa các Command object mang dữ liệu từ người dùng xuống (ví dụ: `RegisterUser`).
  - `handler`: Chứa các Command Handler (ví dụ: `RegisterUserHandler`). Handler nhận command, gọi `domain` để verify dữ liệu, sau đó gọi `infrastructure` thông qua interface để lưu DB.
  - `bus`: Triển khai cơ chế Command Bus để định tuyến các Command tới đúng Handler tương ứng.
  - `dto` và `mapper`: Chuyển đổi dữ liệu giữa Entity của Domain và DTO để trả về.

### 3. `infrastructure` (Tầng Hạ Tầng / Giao Tiếp DB)
Tầng này chứa tất cả các phần kỹ thuật tương tác với thế giới bên ngoài: Cơ sở dữ liệu (MongoDB), các dịch vụ bên thứ 3, hệ thống Message Broker, v.v.
- **Chứa:**
  - `db`: Thực thi các Repository Interface đã định nghĩa ở `domain` (ví dụ: `UserRepositoryImpl`).
  - `entity`: Chứa các Class map trực tiếp với cấu trúc bảng trong Database (`UserEntity`).
  - Spring Data Mongo Repository (`UserMongoRepository`) để thao tác trực tiếp với MongoDB.

### 4. `presentation` (Tầng Giao Tiếp Người Dùng / Cổng Vào)
Đây là mặt tiền của ứng dụng, nơi tiếp nhận các request từ phía client (như Mobile App, Web Frontend).
- **Chứa:**
  - `controller`: Chứa các REST Controller định tuyến API (`UserController`).
  - `request` / `response`: Định nghĩa cấu trúc JSON nhận vào (`RegisterRequest`) và trả về (`ApiResponse`).
  - Chỉ làm nhiệm vụ tiếp nhận HTTP request, chuyển đổi nó thành `Command` rồi đẩy vào `CommandBus` xuống cho tầng `application` xử lý.

### 5. `boot` (Khởi Chạy Ứng Dụng)
Module này là điểm bắt đầu (entry point) để chạy ứng dụng Spring Boot.
- **Chứa:**
  - Lớp `UserApplication` chứa hàm `main`.
  - Các cấu hình (configurations), file `application.yml` liên kết tất cả các module trên lại với nhau thành một microservice hoàn chỉnh và chạy nó lên.

---

## Luồng hoạt động (Data Flow) ví dụ: Chức năng Đăng ký (Register)

1. **Client** gửi một HTTP POST request `/api/v1/users/register` cùng với dữ liệu đăng ký.
2. **`presentation`** (`UserController`) nhận Request, map thành `RegisterUser` (Command) và gửi vào `CommandBus`.
3. **`application`** (`SimpleUserCommandBus`) tìm được `RegisterUserHandler` phù hợp để xử lý.
4. **`application`** (`RegisterUserHandler`) convert command thành `User` (Domain Entity). `User` sẽ ngay lập tức tự xác thực (verify) tính hợp lệ các trường của nó.
5. **`application`** gọi **`domain`** (`UserDomainService`) để kiểm tra xem Email/Username đã bị trùng trong DB hay chưa.
6. Nếu tất cả đều hợp lệ, **`application`** gọi hàm `save()` trên Interface `UserRepository`.
7. **`infrastructure`** (`UserRepositoryImpl`) tiếp nhận lệnh `save()`, chuyển đổi `User` sang `UserEntity` và lưu xuống MongoDB thông qua Spring Data.
8. Kết quả cuối cùng được trả ngược dần lên `presentation` và phản hồi về Client dưới dạng `ApiResponse`.
