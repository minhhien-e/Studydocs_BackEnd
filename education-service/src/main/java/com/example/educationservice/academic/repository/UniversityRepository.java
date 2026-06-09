package com.example.educationservice.academic.repository;

import com.example.educationservice.academic.entity.UniversityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends JpaRepository<UniversityEntity, Long> {

    Optional<UniversityEntity> findUniversityEntitiesByUuid(UUID uuid);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndUuidNot(String slug, UUID uuid);

}
