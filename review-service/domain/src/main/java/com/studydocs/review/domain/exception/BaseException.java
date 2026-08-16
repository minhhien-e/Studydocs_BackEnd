package com.studydocs.review.domain.exception;

import lombok.Getter;

/**
 * Exception cơ bản cho toàn bộ ứng dụng.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final String code;
    private final int status;

    public BaseException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
