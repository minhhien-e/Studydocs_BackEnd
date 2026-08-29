package com.studydocs.modules.review.event.publisher;

public interface ReviewEventPublisher {
    
    /**
     * Publish event khi có đánh giá/bình luận mới hoặc bị xóa.
     *
     * @param documentId ID của tài liệu
     * @param userId     ID của user thực hiện review
     * @param isAdd      true nếu thêm review, false nếu xóa review
     */
    void publishReviewEvent(String documentId, String userId, boolean isAdd);
}
