package com.studydocs.shared.exception;

import com.studydocs.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.UUID;

/**
 * Bộ xử lý ngoại lệ toàn cục (Global Exception Handler).
 * Bắt tất cả các lỗi xảy ra trong REST Controllers và chuẩn hóa về dạng {@link ApiResponse}.
 *
 * @author StudyDocs Team
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý ngoại lệ nghiệp vụ tùy chỉnh {@link AppException}.
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException exception, HttpServletRequest request) {
        ErrorCode errorCode = exception.getErrorCode();
        String traceId = getTraceId(request);

        log.warn("AppException [traceId: {}]: {}", traceId, exception.getMessage());

        ApiResponse<Object> apiResponse = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                exception.getMessage(),
                traceId
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    /**
     * Xử lý ngoại lệ vi phạm Validation dữ liệu đầu vào (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String traceId = getTraceId(request);
        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        try {
            if (enumKey != null) {
                errorCode = ErrorCode.valueOf(enumKey);
            }
        } catch (IllegalArgumentException e) {
            // Giữ mã lỗi mặc định INVALID_REQUEST nếu message không phải tên enum ErrorCode
        }

        String errorMessage = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();

        log.warn("ValidationException [traceId: {}]: {}", traceId, errorMessage);

        ApiResponse<Object> apiResponse = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorMessage,
                traceId
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    /**
     * Xử lý ngoại lệ không có quyền truy cập (Spring Security AccessDenied).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        String traceId = getTraceId(request);

        ApiResponse<Object> apiResponse = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage(),
                traceId
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    /**
     * Xử lý các ngoại lệ hệ thống không xác định (Fallback Handler).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception exception, HttpServletRequest request) {
        String traceId = getTraceId(request);
        log.error("Unhandled Exception [traceId: {}]: ", traceId, exception);

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<Object> apiResponse = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                exception.getMessage(),
                traceId
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    /**
     * Lấy Trace ID từ Request Header hoặc khởi tạo ngẫu nhiên UUID để truy vết log.
     */
    private String getTraceId(HttpServletRequest request) {
        String traceId = request.getHeader("X-Trace-Id");
        return (traceId != null && !traceId.isBlank()) ? traceId : UUID.randomUUID().toString();
    }
}
