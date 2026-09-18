package com.studydocs.modules.academic.repository;

import com.studydocs.modules.academic.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    List<DepartmentEntity> findByFacultyId(Long facultyId);
}
