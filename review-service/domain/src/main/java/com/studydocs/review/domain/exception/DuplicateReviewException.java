package com.studydocs.review.domain.exception;


public class DuplicateReviewException extends BaseException {
    public DuplicateReviewException() {
        super("R005", "Bạn đã đánh giá tài liệu này rồi", 400);
    }
}
