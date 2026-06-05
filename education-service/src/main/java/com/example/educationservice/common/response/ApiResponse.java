package com.example.educationservice.common.response;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, Instant.now());
    }

    public static <T> ApiResponse<T> failure() {
        return new ApiResponse<>(false, null, Instant.now());
    }
}
