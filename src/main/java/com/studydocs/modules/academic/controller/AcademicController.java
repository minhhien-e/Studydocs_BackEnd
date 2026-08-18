package com.studydocs.modules.academic.controller;

import com.studydocs.modules.academic.dto.AcademicDtos;
import com.studydocs.modules.academic.dto.DocumentSummaryDto;
import com.studydocs.modules.academic.service.AcademicService;
import com.studydocs.modules.academic.service.DocumentService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/education/academics")
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicService academicService;
    private final DocumentService documentService;

    @GetMapping({"/universities", "/universities/filter"})
    public ApiResponse<List<AcademicDtos.UniversityDto>> getAllUniversities() {
        return ApiResponse.success(academicService.getAllUniversities());
    }

    @GetMapping("/universities/{universityId}")
    public ApiResponse<AcademicDtos.UniversityDto> getUniversityDetail(@PathVariable Long universityId) {
        return ApiResponse.success(academicService.getAllUniversities().stream()
                .filter(u -> u.getId().equals(universityId))
                .findFirst()
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.ACADEMIC_NOT_FOUND)));
    }

    @GetMapping("/universities/{universityId}/faculties")
    public ApiResponse<List<AcademicDtos.FacultyDto>> getFacultiesByUniversity(@PathVariable Long universityId) {
        return ApiResponse.success(academicService.getFacultiesByUniversity(universityId));
    }

    @GetMapping("/faculties/{facultyId}/departments")
    public ApiResponse<List<AcademicDtos.DepartmentDto>> getDepartmentsByFaculty(@PathVariable Long facultyId) {
        return ApiResponse.success(academicService.getDepartmentsByFaculty(facultyId));
    }

    @GetMapping({"/subjects", "/subjects/filter"})
    public ApiResponse<List<AcademicDtos.SubjectDto>> getSubjects(@RequestParam(value = "departmentId", defaultValue = "1") Long departmentId) {
        return ApiResponse.success(academicService.getSubjectsByDepartment(departmentId));
    }

    @GetMapping("/departments/{departmentId}/subjects")
    public ApiResponse<List<AcademicDtos.SubjectDto>> getSubjectsByDepartment(@PathVariable Long departmentId) {
        return ApiResponse.success(academicService.getSubjectsByDepartment(departmentId));
    }

    @GetMapping("/public/universities/id/{id}")
    public ApiResponse<AcademicDtos.UniversityDto> getUniversityById(@PathVariable Long id) {
        return ApiResponse.success(academicService.getAllUniversities().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.ACADEMIC_NOT_FOUND)));
    }

    @GetMapping("/public/subjects/id/{id}")
    public ApiResponse<AcademicDtos.SubjectDto> getSubjectById(@PathVariable Long id) {
        return ApiResponse.success(academicService.getSubjectsByDepartment(1L).stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.ACADEMIC_NOT_FOUND)));
    }

    @GetMapping("/documents")
    public ApiResponse<List<DocumentSummaryDto>> getAcademicDocuments(@RequestParam(value = "q", required = false) String query) {
        return ApiResponse.success(documentService.searchDocuments(query));
    }
}
