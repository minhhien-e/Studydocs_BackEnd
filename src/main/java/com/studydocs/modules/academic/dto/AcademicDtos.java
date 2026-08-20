package com.studydocs.modules.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AcademicDtos {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UniversityDto {
        private Long id;
        private String name;
        private String code;
        private String logoUrl;
        private String address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacultyDto {
        private Long id;
        private String name;
        private Long universityId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentDto {
        private Long id;
        private String name;
        private Long facultyId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectDto {
        private Long id;
        private String name;
        private String code;
        private Long departmentId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUniversityRequest {
        private String name;
        private String code;
        private String logoUrl;
        private String address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSubjectRequest {
        private String name;
        private String code;
        private Long departmentId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDocumentRequest {
        private String title;
        private String description;
        private String fileUrl;
        private Long fileSize;
        private String fileType;
        private Long universityId;
        private Long facultyId;
        private Long subjectId;
        private Boolean isPublic;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InitiateDocumentUploadRequest {
        private String title;
        private String description;
        private Long universityId;
        private Long facultyId;
        private Long subjectId;
        private Boolean isPublic;
        private String fileName;
        private Long fileSize;
        private String fileType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentInitiateResponse {
        private String documentId;
        private String mediaId;
        private String uploadUrl;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteDocumentUploadRequest {
        private String mediaId;
        private String fileUrl;
        private Long fileSize;
        private String fileType;
    }
}
