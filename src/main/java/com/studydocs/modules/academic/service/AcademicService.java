package com.studydocs.modules.academic.service;

import com.studydocs.modules.academic.dto.AcademicDtos;

import java.util.List;

public interface AcademicService {
    List<AcademicDtos.UniversityDto> getAllUniversities();
    List<AcademicDtos.FacultyDto> getFacultiesByUniversity(Long universityId);
    List<AcademicDtos.DepartmentDto> getDepartmentsByFaculty(Long facultyId);
    List<AcademicDtos.SubjectDto> getSubjectsByDepartment(Long departmentId);
    AcademicDtos.UniversityDto createUniversity(AcademicDtos.CreateUniversityRequest request);
    AcademicDtos.SubjectDto createSubject(AcademicDtos.CreateSubjectRequest request);
}
