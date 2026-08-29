package com.studydocs.modules.academic.event.publisher;

/**
 * Interface định nghĩa contract cho việc publish các sự kiện liên quan đến tài liệu.
 * Giúp tách biệt tầng domain (service) khỏi implementation cụ thể (Kafka, RabbitMQ, v.v.)
 */
public interface DocumentEventPublisher {

    /**
     * Publish event để đếm số trang của tài liệu PDF bất đồng bộ.
     *
     * @param documentId ID của tài liệu cần đếm trang
     * @param fileUrl    URL đầy đủ của file PDF
     * @param uploaderId ID của người upload để tăng postsCount
     */
    void publishPageCountEvent(String documentId, String fileUrl, String uploaderId);

    /**
     * Publish event khi có tương tác (Like/Bookmark) với tài liệu.
     *
     * @param documentId ID của tài liệu
     * @param userId     ID của user thực hiện tương tác
     * @param type       Loại tương tác (LIKE, BOOKMARK)
     * @param isAdd      true nếu là thêm tương tác, false nếu là xóa
     */
    void publishInteractionEvent(String documentId, String userId, String type, boolean isAdd);
}
