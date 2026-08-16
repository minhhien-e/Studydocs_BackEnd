package com.studydocs.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Generic Errors
    SUCCESS(0, "Success", HttpStatus.OK),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error key", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1002, "Invalid request payload", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(1003, "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1004, "Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1005, "Access forbidden", HttpStatus.FORBIDDEN),

    // User / Auth Errors
    USER_EXISTED(2001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2002, "User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(2003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(2004, "Invalid or expired token", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH(2005, "Password does not match", HttpStatus.BAD_REQUEST),

    // Academic / Document Errors
    ACADEMIC_NOT_FOUND(3001, "Academic entity not found", HttpStatus.NOT_FOUND),
    DOCUMENT_NOT_FOUND(3002, "Document not found", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(3003, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),

    // Review / Reaction Errors
    REVIEW_NOT_FOUND(4001, "Review not found", HttpStatus.NOT_FOUND),

    // Follow Errors
    CANNOT_FOLLOW_SELF(5001, "Cannot follow yourself", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
