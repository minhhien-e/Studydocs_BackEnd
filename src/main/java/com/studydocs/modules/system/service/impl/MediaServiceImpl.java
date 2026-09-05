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

    @Override
    public SystemDtos.MediaResponse uploadFile(MultipartFile file, String ownerId) {
        String fileUrl;
        String storedFileName;

        try {
            // Force resource_type to "image" so Cloudinary can process PDFs into thumbnails
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));
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
}
