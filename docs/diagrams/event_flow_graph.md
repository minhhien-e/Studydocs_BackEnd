# Sơ đồ Kiến trúc Hướng Sự kiện (Event-Driven Flow)

Sơ đồ này mô tả luồng bất đồng bộ (asynchronous) xử lý các side-effects như: cập nhật thống kê (posts, likes, comments, follows), đếm số trang PDF, và có thể mở rộng cho Notifications sau này.

```mermaid
graph LR
    %% Services (Nguồn phát sinh sự kiện)
    S_Doc["Service: DocumentService"]
    S_Review["Service: ReviewService"]
    S_Follow["Service: FollowService"]

    %% Event Publishers
    Pub_Doc["Publisher: DocumentEventPublisher"]
    Pub_Review["Publisher: ReviewEventPublisher"]
    Pub_Follow["Publisher: FollowEventPublisher"]

    %% Kafka Topics
    K_Upload(("Kafka Topic: document-page-count"))
    K_Interact(("Kafka Topic: document-interacted"))
    K_Review(("Kafka Topic: review"))
    K_Follow(("Kafka Topic: user-follow"))

    %% Consumers (Workers xử lý logic ngầm)
    C_Pdf["Consumer: PdfPageCountConsumer<br/>Đếm số trang"]
    C_UserStats["Consumer: UserStatsConsumer<br/>Cập nhật User Stats"]
    C_DocStats["Consumer: DocumentStatsConsumer<br/>Cập nhật Document Stats"]

    %% Databases
    DB_User[("Database: Users<br/>postsCount, likesCount...")]
    DB_Doc[("Database: Documents<br/>likeCount, commentCount...")]

    %% Triggers (Service -> Publisher)
    S_Doc -->|Gọi khi Upload/Tương tác| Pub_Doc
    S_Review -->|Gọi khi Thêm/Xóa Review| Pub_Review
    S_Follow -->|Gọi khi Follow/Unfollow| Pub_Follow

    %% Publish (Publisher -> Kafka)
    Pub_Doc -->|DocumentUploadedEvent| K_Upload
    Pub_Doc -->|DocumentInteractedEvent| K_Interact
    Pub_Review -->|ReviewEvent| K_Review
    Pub_Follow -->|UserFollowEvent| K_Follow

    %% Consume (Kafka -> Consumers)
    K_Upload -.->|"Lắng nghe"| C_Pdf
    K_Upload -.->|"Lắng nghe"| C_UserStats
    
    K_Interact -.->|"Lắng nghe"| C_UserStats
    K_Interact -.->|"Lắng nghe"| C_DocStats
    
    K_Review -.->|"Lắng nghe"| C_UserStats
    K_Review -.->|"Lắng nghe"| C_DocStats
    
    K_Follow -.->|"Lắng nghe"| C_UserStats

    %% Save Data (Consumers -> DB)
    C_Pdf ==>|"Update pageCount"| DB_Doc
    C_UserStats ==>|"Update Stats"| DB_User
    C_DocStats ==>|"Update Stats"| DB_Doc


```
