# Sơ đồ Nghiệp vụ Chi tiết Toàn hệ thống (Comprehensive Business Flow)

Sơ đồ này mô tả CHI TIẾT sự tương tác giữa các Domain trong hệ thống. Nó bao gồm các luồng gọi trực tiếp (Synchronous), các luồng đọc chéo Domain (nếu có), và luồng giao tiếp bất đồng bộ qua Message Broker (Kafka/Events). Sơ đồ này phục vụ cho việc phân tích kiến trúc và ranh giới (boundaries) của các Domain.

```mermaid
graph LR
    %% ==========================================
    %% 1. USER DOMAIN
    %% ==========================================
    subgraph UserDomain [User Domain]
        API_Auth[AuthController]
        API_User[UserController]
        S_Auth[AuthService]
        S_User[UserService]
        DB_User[(UserRepository)]
        C_UserStats[UserStatsConsumer]
        
        API_Auth --> S_Auth
        API_User --> S_User
        S_Auth --> DB_User
        S_User --> DB_User
        
        C_UserStats -.->|"Cập nhật Stats"| DB_User
    end

    %% ==========================================
    %% 2. ACADEMIC DOMAIN
    %% ==========================================
    subgraph AcademicDomain [Academic Domain]
        API_Doc[DocumentController]
        S_Doc[DocumentService]
        DB_Doc[(DocumentRepository)]
        DB_DocInteract[(DocInteractionRepository)]
        DB_Univ[(UniversityRepository)]
        DB_Subj[(SubjectRepository)]
        Pub_Doc[DocumentEventPublisher]
        C_DocStats[DocumentStatsConsumer]

        API_Doc --> S_Doc
        S_Doc --> DB_Doc
        S_Doc --> DB_DocInteract
        S_Doc --> DB_Univ
        S_Doc --> DB_Subj
        
        %% Giao tiếp với Publisher
        S_Doc -->|"Bắn sự kiện"| Pub_Doc
        
        C_DocStats -.->|"Cập nhật Stats"| DB_Doc
    end

    %% ==========================================
    %% 3. REVIEW DOMAIN
    %% ==========================================
    subgraph ReviewDomain [Review Domain]
        API_Review[ReviewController]
        S_Review[ReviewService]
        DB_Review[(ReviewRepository)]
        Pub_Review[ReviewEventPublisher]

        API_Review --> S_Review
        S_Review --> DB_Review
        
        %% Giao tiếp với Publisher
        S_Review -->|"Bắn sự kiện"| Pub_Review
        
        %% Điểm Đọc Chéo (Cross-domain Sync Read)
        S_Review -.->|"Đọc trực tiếp (để map DTO)"| DB_User
    end

    %% ==========================================
    %% 4. FOLLOW DOMAIN
    %% ==========================================
    subgraph FollowDomain [Follow Domain]
        API_Follow[FollowController]
        S_Follow[FollowService]
        DB_Follow[(FollowRepository)]
        Pub_Follow[FollowEventPublisher]

        API_Follow --> S_Follow
        S_Follow --> DB_Follow
        
        %% Giao tiếp với Publisher
        S_Follow -->|"Bắn sự kiện"| Pub_Follow
    end
    
    %% ==========================================
    %% 5. INFRASTRUCTURE & EXTERNAL
    %% ==========================================
    subgraph InfrastructureDomain [Infrastructure & External]
        S_Media["MediaService / Cloudinary"]
        C_Pdf[PdfPageCountConsumer]
        
        S_Doc -->|"Upload File Đồng bộ"| S_Media
        C_Pdf -.->|"Cập nhật PageCount"| DB_Doc
    end

    %% ==========================================
    %% 6. MESSAGE BROKER (KAFKA)
    %% ==========================================
    subgraph MessageBroker [Message Broker]
        Topic_DocUpload(("Topic: document-page-count"))
        Topic_DocInteract(("Topic: document-interacted"))
        Topic_Review(("Topic: review"))
        Topic_Follow(("Topic: user-follow"))
    end

    %% ==========================================
    %% LUỒNG SỰ KIỆN (EVENT FLOWS)
    %% ==========================================
    
    %% Publishers -> Topics
    Pub_Doc ==>|DocumentUploadedEvent| Topic_DocUpload
    Pub_Doc ==>|DocumentInteractedEvent| Topic_DocInteract
    Pub_Review ==>|ReviewEvent| Topic_Review
    Pub_Follow ==>|UserFollowEvent| Topic_Follow

    %% Topics -> Consumers (Fan-out)
    Topic_DocUpload ==>|Consume| C_Pdf
    Topic_DocUpload ==>|Consume| C_UserStats
    
    Topic_DocInteract ==>|Consume| C_UserStats
    Topic_DocInteract ==>|Consume| C_DocStats
    
    Topic_Review ==>|Consume| C_UserStats
    Topic_Review ==>|Consume| C_DocStats
    
    Topic_Follow ==>|Consume| C_UserStats
```
