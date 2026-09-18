# Hướng dẫn Cập nhật Sơ đồ Kiến trúc (Graph Diagrams)

Thư mục `docs/diagrams/` chứa các sơ đồ luồng dữ liệu nghiệp vụ của hệ thống được viết bằng ngôn ngữ [Mermaid JS](https://mermaid.js.org/).

Sơ đồ giúp các lập trình viên mới hoặc team Architecture có cái nhìn tổng quan về hệ thống mà không cần đọc từng dòng code, từ đó dễ dàng đánh giá độ phức tạp và đưa ra phương án cấu trúc (VD: tách microservices).

## Các loại sơ đồ hiện có

1. **`business_flow_graph.md`**: Sơ đồ đồng bộ (Synchronous). Hiển thị các thao tác API và việc lưu trữ dữ liệu chính.
2. **`event_flow_graph.md`**: Sơ đồ bất đồng bộ (Event-Driven). Hiển thị quá trình xử lý side-effect, cập nhật thống kê, gửi thông báo,... qua Message Broker (Kafka).

## Cách cập nhật sơ đồ khi có nghiệp vụ mới

Bất cứ khi nào bạn (hoặc AI) thêm một Service, một Event Publisher, Topic mới, hay một Consumer mới, bạn **BẮT BUỘC** phải làm theo các bước sau để cập nhật lại Graph:

### Bước 1: Mở file Sơ đồ tương ứng
- Nếu bạn thêm API mới gọi thẳng xuống Database -> Sửa `business_flow_graph.md`.
- Nếu bạn thêm tính năng chạy ngầm qua Kafka (ví dụ: Gửi email thông báo) -> Sửa `event_flow_graph.md`.

### Bước 2: Khai báo Node mới
Ở nửa đầu của khối Mermaid, hãy tìm phần định nghĩa tương ứng (VD: `%% Services`, `%% Kafka Topics`) và thêm node mới:
```mermaid
%% Cú pháp: [Mã_Node]([Mô tả hiển thị])
S_Notif[Service: NotificationService]
K_Email((Kafka Topic: send-email))
C_Email[Consumer: EmailConsumer]
```

### Bước 3: Định nghĩa liên kết (Flow)
Ở nửa sau của khối Mermaid, tìm phần `%% Flow` và thêm mũi tên chỉ định hướng đi của dữ liệu:
```mermaid
%% Cú pháp: [Node_A] -->|Nội dung| [Node_B]
S_Notif -->|SendEmailEvent| K_Email
K_Email -.->|Lắng nghe| C_Email
```
*(Ghi chú: Dùng `-->` cho luồng gọi trực tiếp/publish, dùng `-.->` cho luồng lắng nghe/consume).*

### Bước 4: Kiểm tra lại Graph
Bạn có thể cài đặt các Extension Markdown Preview có hỗ trợ Mermaid trên IDE (VS Code, IntelliJ) hoặc dán đoạn mã khối `mermaid` vào [Mermaid Live Editor](https://mermaid.live/) để xem trước biểu đồ hiển thị có bị lỗi cú pháp hay không.

### 💡 Ví dụ thực tế trong tương lai
Nếu sau này bạn cần làm hệ thống **Notification** (Thông báo) cho tính năng Follow:
1. Mở `event_flow_graph.md`.
2. Thêm Node `C_Notif[Consumer: NotificationConsumer]` vào phần `%% Consumers`.
3. Trong phần `%% Consume`, thêm dòng `K_Follow -.-> C_Notif`.
4. Trong phần `%% Save Data`, thêm dòng `C_Notif ==> DB_Notif`.
