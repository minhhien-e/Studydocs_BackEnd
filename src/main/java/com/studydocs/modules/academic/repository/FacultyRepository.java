package com.studydocs.modules.academic.repository;

import com.studydocs.modules.academic.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<FacultyEntity, Long> {
    List<FacultyEntity> findByUniversityId(Long universityId);
}
