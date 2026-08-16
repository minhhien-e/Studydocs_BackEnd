package com.studydocs.modules.system.controller;

import com.studydocs.modules.system.dto.SystemDtos;
import com.studydocs.modules.system.service.MediaService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ApiResponse<SystemDtos.MediaResponse> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String ownerId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(mediaService.uploadFile(file, ownerId));
    }

    @PostMapping("/init-upload")
    public ApiResponse<Map<String, Object>> initUpload(@RequestBody Map<String, Object> body) {
        String mediaId = UUID.randomUUID().toString();
        String uploadUrl = "/api/v1/media/" + mediaId + "/complete-upload";
        return ApiResponse.success(Map.of(
                "mediaId", mediaId,
                "uploadUrl", uploadUrl
        ));
    }

    @PostMapping("/{mediaId}/complete-upload")
    public ApiResponse<SystemDtos.MediaResponse> completeUpload(@PathVariable String mediaId,
                                                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                                                 Authentication authentication) {
        String ownerId = authentication != null ? authentication.getName() : "anonymous";
        if (file != null && !file.isEmpty()) {
            return ApiResponse.success(mediaService.uploadFile(file, ownerId));
        }
        return ApiResponse.success(SystemDtos.MediaResponse.builder()
                .id(mediaId)
                .fileName(mediaId + ".pdf")
                .fileUrl("/api/v1/media/files/" + mediaId + ".pdf")
                .contentType("application/pdf")
                .fileSize(1024000L)
                .build());
    }

    @GetMapping("/files/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) {
        byte[] fileBytes = mediaService.getFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileBytes);
    }
}
