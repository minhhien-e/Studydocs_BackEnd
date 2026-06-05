package com.example.educationservice.academic.dto.request;

import java.util.UUID;

public record UpdateUniversityRequest (
    String name,
    UUID uuid
){}
