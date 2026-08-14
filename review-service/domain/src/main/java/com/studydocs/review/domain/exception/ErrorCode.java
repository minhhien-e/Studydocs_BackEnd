package com.studydocs.review.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    REVIEW_NOT_FOUND("R001", "Review not found", 404),
    DOCUMENT_NOT_FOUND("R002", "Document not found", 404),
    USER_NOT_FOUND("R003", "User not found", 404),
    INVALID_SCORE("R004", "Score must be between 1 and 5", 400),
    UNAUTHORIZED("R403", "Bạn không có quyền thực hiện hành động này", 403),
    INTERNAL_SERVER_ERROR("R999", "Internal server error", 500);

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
