package com.example.educationservice.document.dto.response;

import java.util.List;

public record DocumentListResponse(
        List<DocumentListItemResponse> items,
        int page,
        int pageSize,
        long total,
        boolean hasMore
) {
}
