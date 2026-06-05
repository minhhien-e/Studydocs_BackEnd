package com.example.educationservice.academic.mapper;

import com.example.educationservice.academic.dto.response.UniversityResponse;
import com.example.educationservice.academic.entity.UniversityEntity;
import org.springframework.stereotype.Component;

@Component
public class UniversityMapper {

    public UniversityResponse toResponse(UniversityEntity entity) {
        return new UniversityResponse(
                entity.getUuid(),
                entity.getName(),
                entity.getSlug(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
