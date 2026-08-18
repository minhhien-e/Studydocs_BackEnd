package com.studydocs.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 1. Generic & System Errors (1000 - 1999)
    SUCCESS(0, "Success", HttpStatus.OK),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error key", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1002, "Invalid request payload", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(1003, "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1004, "Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1005, "Access forbidden", HttpStatus.FORBIDDEN),
    METHOD_NOT_ALLOWED(1006, "HTTP method not supported", HttpStatus.METHOD_NOT_ALLOWED),

    // 2. User & Authentication Errors (2000 - 2999)
    USER_EXISTED(2001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2002, "User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(2003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(2004, "Invalid or expired token", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(2005, "Refresh token expired", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCH(2006, "Password does not match", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD(2007, "Password is too weak", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(2008, "Account is locked or suspended", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(2009, "Account is disabled", HttpStatus.FORBIDDEN),
    EMAIL_NOT_VERIFIED(2010, "Email has not been verified", HttpStatus.FORBIDDEN),

    // 3. Academic Catalog Errors (3000 - 3499)
    ACADEMIC_NOT_FOUND(3001, "Academic entity not found", HttpStatus.NOT_FOUND),
    UNIVERSITY_NOT_FOUND(3002, "University not found", HttpStatus.NOT_FOUND),
    FACULTY_NOT_FOUND(3003, "Faculty not found", HttpStatus.NOT_FOUND),
    DEPARTMENT_NOT_FOUND(3004, "Department not found", HttpStatus.NOT_FOUND),
    SUBJECT_NOT_FOUND(3005, "Subject not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ACADEMIC_CODE(3006, "Academic code already exists", HttpStatus.BAD_REQUEST),

    // 4. Document & File Storage Errors (3500 - 3999)
    DOCUMENT_NOT_FOUND(3501, "Document not found", HttpStatus.NOT_FOUND),
    DOCUMENT_ACCESS_DENIED(3502, "You do not have permission to view this document", HttpStatus.FORBIDDEN),
    FILE_EMPTY(3503, "Uploaded file is empty", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(3504, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_TYPE(3505, "Invalid file type. Only PDF, DOCX, PPTX, JPG, PNG allowed", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED(3506, "File size exceeds allowed limit", HttpStatus.BAD_REQUEST),
    DOCUMENT_ALREADY_BOOKMARKED(3507, "Document is already bookmarked", HttpStatus.BAD_REQUEST),
    BOOKMARK_NOT_FOUND(3508, "Bookmark not found", HttpStatus.NOT_FOUND),

    // 5. Review & Rating Errors (4000 - 4999)
    REVIEW_NOT_FOUND(4001, "Review not found", HttpStatus.NOT_FOUND),
    INVALID_RATING_VALUE(4002, "Rating value must be between 1 and 5", HttpStatus.BAD_REQUEST),
    COMMENT_EMPTY(4003, "Comment content cannot be empty", HttpStatus.BAD_REQUEST),
    REVIEW_ALREADY_EXISTS(4004, "You have already reviewed this document", HttpStatus.BAD_REQUEST),
    REVIEW_ACCESS_DENIED(4005, "You do not have permission to edit/delete this review", HttpStatus.FORBIDDEN),

    // 6. Follow & Social Relationship Errors (5000 - 5999)
    CANNOT_FOLLOW_SELF(5001, "Cannot follow yourself", HttpStatus.BAD_REQUEST),
    ALREADY_FOLLOWED(5002, "Already following this user", HttpStatus.BAD_REQUEST),
    FOLLOW_NOT_FOUND(5003, "Follow relationship not found", HttpStatus.NOT_FOUND),

    // 7. Notification Errors (6000 - 6999)
    NOTIFICATION_NOT_FOUND(6001, "Notification not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_ALREADY_READ(6002, "Notification is already marked as read", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
