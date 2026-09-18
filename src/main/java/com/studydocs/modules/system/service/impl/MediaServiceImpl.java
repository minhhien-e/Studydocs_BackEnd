package com.studydocs.modules.system.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.studydocs.infras.storage.FileStorageService;
import com.studydocs.modules.system.dto.SystemDtos;
import com.studydocs.modules.system.entity.MediaAssetEntity;
import com.studydocs.modules.system.repository.MediaAssetRepository;
import com.studydocs.modules.system.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final FileStorageService fileStorageService;
    private final MediaAssetRepository mediaAssetRepository;
    private final Cloudinary cloudinary;

    @org.springframework.beans.factory.annotation.Value("${cloudinary.folder:studydocs_uploads}")
    private String cloudinaryFolder;

    @Override
    public SystemDtos.MediaResponse uploadFile(MultipartFile file, String ownerId) {
        String fileUrl;
        String storedFileName;

        try {
            // Force resource_type to "image" so Cloudinary can process PDFs into thumbnails
            Map<String, Object> params = ObjectUtils.asMap(
                "resource_type", "image",
                "folder", cloudinaryFolder
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            fileUrl = uploadResult.get("url").toString();
            storedFileName = uploadResult.get("public_id").toString();
        } catch (Exception e) {
            log.error("Cloudinary upload failed: {}", e.getMessage(), e);
            // Fallback to local storage if Cloudinary is disabled or fails
            storedFileName = fileStorageService.storeFile(file);
            fileUrl = "/api/v1/media/files/" + storedFileName;
        }

        MediaAssetEntity asset = MediaAssetEntity.builder()
                .fileName(storedFileName)
                .fileUrl(fileUrl)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .ownerId(ownerId)
                .build();

        asset = mediaAssetRepository.save(asset);

        return SystemDtos.MediaResponse.builder()
                .id(asset.getId())
                .fileName(asset.getFileName())
                .fileUrl(asset.getFileUrl())
                .contentType(asset.getContentType())
                .fileSize(asset.getFileSize())
                .build();
    }

    @Override
    public byte[] getFile(String fileName) {
        return fileStorageService.loadFileAsBytes(fileName);
    }

    @Override
    public int getPdfPageCount(String fileUrl) {
        if (fileUrl != null && fileUrl.contains("cloudinary.com")) {
            try {
                // Extract public_id from Cloudinary URL
                // Example URL: https://res.cloudinary.com/dtshks6lm/image/upload/v1788854097/Studydocs/gwpj0yi95bwvmdomigdq.pdf
                // public_id is Studydocs/gwpj0yi95bwvmdomigdq
                String[] parts = fileUrl.split("/upload/");
                if (parts.length > 1) {
                    String afterUpload = parts[1];
                    // Remove version (e.g., v1788854097/)
                    afterUpload = afterUpload.replaceFirst("^v\\d+/", "");
                    // Remove extension (e.g., .pdf)
                    int extIndex = afterUpload.lastIndexOf('.');
                    if (extIndex != -1) {
                        afterUpload = afterUpload.substring(0, extIndex);
                    }
                    String publicId = afterUpload;

                    // Query Cloudinary Admin API for resource details
                    Map resource = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
                    if (resource.containsKey("pages")) {
                        return ((Number) resource.get("pages")).intValue();
                    }
                }
            } catch (Exception e) {
                log.error("Cloudinary failed to get page count for {}: {}", fileUrl, e.getMessage());
            }
        }
        return 0; // Fallback
    }
}
