package com.studydocs.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int statusCode;
    private Integer errorCode;
    private T data;
    private String traceId;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .errorCode(null)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .errorCode(null)
                .data(data)
                .traceId(traceId)
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, Integer errorCode, T data, String traceId) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .errorCode(errorCode)
                .data(data)
                .traceId(traceId)
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, Integer errorCode, String traceId) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .errorCode(errorCode)
                .traceId(traceId)
                .build();
    }
}
