package com.studydocs.review.domain.exception;

/**
 * Exception khi không tìm thấy bình luận.
 */
public class ReviewNotFoundException extends BaseException {
    public ReviewNotFoundException() {
        super("R001", "Không tìm thấy bình luận", 404);
    }
}
