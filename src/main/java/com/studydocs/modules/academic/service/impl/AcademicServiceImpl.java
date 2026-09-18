package com.studydocs.modules.academic.service.impl;

import com.studydocs.modules.academic.dto.AcademicDtos;
import com.studydocs.modules.academic.entity.SubjectEntity;
import com.studydocs.modules.academic.entity.UniversityEntity;
import com.studydocs.modules.academic.repository.DepartmentRepository;
import com.studydocs.modules.academic.repository.FacultyRepository;
import com.studydocs.modules.academic.repository.SubjectRepository;
import com.studydocs.modules.academic.repository.UniversityRepository;
import com.studydocs.modules.academic.service.AcademicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public List<AcademicDtos.UniversityDto> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(u -> AcademicDtos.UniversityDto.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .code(u.getCode())
                        .logoUrl(u.getLogoUrl())
                        .address(u.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<AcademicDtos.FacultyDto> getFacultiesByUniversity(Long universityId) {
        return facultyRepository.findByUniversityId(universityId).stream()
                .map(f -> AcademicDtos.FacultyDto.builder()
                        .id(f.getId())
                        .name(f.getName())
                        .universityId(f.getUniversityId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<AcademicDtos.DepartmentDto> getDepartmentsByFaculty(Long facultyId) {
        return departmentRepository.findByFacultyId(facultyId).stream()
                .map(d -> AcademicDtos.DepartmentDto.builder()
                        .id(d.getId())
                        .name(d.getName())
                        .facultyId(d.getFacultyId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<AcademicDtos.SubjectDto> getSubjectsByDepartment(Long departmentId) {
        return subjectRepository.findByDepartmentId(departmentId).stream()
                .map(s -> AcademicDtos.SubjectDto.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .code(s.getCode())
                        .departmentId(s.getDepartmentId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AcademicDtos.UniversityDto createUniversity(AcademicDtos.CreateUniversityRequest request) {
        UniversityEntity entity = UniversityEntity.builder()
                .name(request.getName())
                .code(request.getCode())
                .logoUrl(request.getLogoUrl())
                .address(request.getAddress())
                .build();
        UniversityEntity saved = universityRepository.save(entity);
        return AcademicDtos.UniversityDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .code(saved.getCode())
                .logoUrl(saved.getLogoUrl())
                .address(saved.getAddress())
                .build();
    }

    @Override
    public AcademicDtos.SubjectDto createSubject(AcademicDtos.CreateSubjectRequest request) {
        SubjectEntity entity = SubjectEntity.builder()
                .name(request.getName())
                .code(request.getCode())
                .departmentId(request.getDepartmentId())
                .build();
        SubjectEntity saved = subjectRepository.save(entity);
        return AcademicDtos.SubjectDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .code(saved.getCode())
                .departmentId(saved.getDepartmentId())
                .build();
    }
}
