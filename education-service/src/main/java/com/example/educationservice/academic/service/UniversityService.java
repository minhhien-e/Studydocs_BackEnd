package com.example.educationservice.academic.service;

import com.example.educationservice.academic.dto.request.CreateUniversityRequest;
import com.example.educationservice.academic.dto.request.UpdateUniversityRequest;
import com.example.educationservice.academic.dto.response.UniversityResponse;

import java.util.List;
import java.util.UUID;

public interface UniversityService {

    UniversityResponse createUniversity(CreateUniversityRequest request);
    UniversityResponse updateUniversity(UpdateUniversityRequest request);
    UniversityResponse getUniversity(UUID uuid);
    List<UniversityResponse> getAllUniversities();
}
