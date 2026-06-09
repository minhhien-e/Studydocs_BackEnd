package com.example.educationservice.academic.service;

import com.example.educationservice.academic.dto.request.CreateUniversityRequest;
import com.example.educationservice.academic.dto.request.UpdateUniversityRequest;
import com.example.educationservice.academic.dto.response.UniversityResponse;
import com.example.educationservice.academic.entity.UniversityEntity;
import com.example.educationservice.academic.mapper.UniversityMapper;
import com.example.educationservice.academic.repository.UniversityRepository;
import com.example.educationservice.common.slug.SlugService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final SlugService slugService;
    private final UniversityMapper universityMapper;

    @Override
    @Transactional
    public UniversityResponse createUniversity(CreateUniversityRequest request) {
        String normalizedName = request.name().trim();
        String baseSlug = slugService.toSlug(normalizedName);
        String slug = resolveSlugForCreate(baseSlug);
        UniversityEntity entity = new UniversityEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setName(normalizedName);
        entity.setSlug(slug);

        UniversityEntity saved = universityRepository.save(entity);
        return universityMapper.toResponse(saved);
    }

@Override
@Transactional
public UniversityResponse updateUniversity(UpdateUniversityRequest request) {
    UniversityEntity entity = universityRepository.findUniversityEntitiesByUuid(request.uuid())
            .orElseThrow(() -> new RuntimeException("University not found"));
    String normalizedName = request.name().trim();
    String baseSlug = slugService.toSlug(normalizedName);
    String slug = resolveSlugForUpdate(baseSlug, request.uuid());
    entity.setName(normalizedName);
    entity.setSlug(slug);

    UniversityEntity saved = universityRepository.save(entity);
    return universityMapper.toResponse(saved);
}

    @Override
    public UniversityResponse getUniversity(UUID uuid) {
        return universityRepository.findUniversityEntitiesByUuid(uuid)
                .map(universityMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("University not found"));
    }

    @Override
    public List<UniversityResponse> getAllUniversities() {
        return universityRepository.findAll()
                .stream()
                .map(universityMapper::toResponse)
                .toList();
    }

    private String resolveSlugForCreate(String baseSlug) {
        String candidate = baseSlug;
        int counter = 2;
        while (universityRepository.existsBySlug(candidate)) {
            candidate = baseSlug + "-" + counter;
            counter++;
        }
        return candidate;
    }

    private String resolveSlugForUpdate(String baseSlug, UUID uuid) {
        String candidate = baseSlug;
        int counter = 2;
        while (universityRepository.existsBySlugAndUuidNot(candidate, uuid)) {
            candidate = baseSlug + "-" + counter;
            counter++;
        }
        return candidate;
    }
}
