package com.example.educationservice.academic.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UniversityResponse(
        UUID uuid,
        String name,
        String slug,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
