package com.example.educationservice.academic.controller;

import com.example.educationservice.academic.dto.request.CreateUniversityRequest;
import com.example.educationservice.academic.dto.request.UpdateUniversityRequest;
import com.example.educationservice.academic.dto.response.UniversityResponse;
import com.example.educationservice.academic.service.UniversityService;
import com.example.educationservice.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/universities")
@RequiredArgsConstructor
public class UniversityController {
    private final UniversityService universityService;

    @PostMapping
    public ResponseEntity<ApiResponse<UniversityResponse>> create(@Valid @RequestBody CreateUniversityRequest request) {
        UniversityResponse response = universityService.createUniversity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UniversityResponse>> update(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateUniversityRequest request
    ) {
        UniversityResponse response = universityService.updateUniversity(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UniversityResponse>>> getUniversities() {
        List<UniversityResponse> response = universityService.getAllUniversities();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{uuid}")
    public  ResponseEntity<ApiResponse<UniversityResponse>>  getUniversity(@PathVariable String uuid) {
        UniversityResponse response = universityService.getUniversity(UUID.fromString(uuid));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteUniversity(@PathVariable String uuid) {
        return null;
    }

}
