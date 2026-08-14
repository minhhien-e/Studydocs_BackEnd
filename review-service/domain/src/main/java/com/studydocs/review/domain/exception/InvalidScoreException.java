package com.studydocs.review.domain.exception;

/**
 * Exception khi điểm đánh giá không hợp lệ.
 */
public class InvalidScoreException extends BaseException {
    public InvalidScoreException() {
        super("R004", "Điểm đánh giá phải từ 1 đến 5", 400);
    }
}
