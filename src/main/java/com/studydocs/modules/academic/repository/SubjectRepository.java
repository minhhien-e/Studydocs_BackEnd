package com.studydocs.modules.academic.repository;

import com.studydocs.modules.academic.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findByDepartmentId(Long departmentId);
}
