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

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final FileStorageService fileStorageService;
    private final MediaAssetRepository mediaAssetRepository;
    private final Cloudinary cloudinary;

    @Override
    public SystemDtos.MediaResponse uploadFile(MultipartFile file, String ownerId) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String fileUrl = uploadResult.get("url").toString();
            String storedFileName = uploadResult.get("public_id").toString();

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
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public byte[] getFile(String fileName) {
        return fileStorageService.loadFileAsBytes(fileName);
    }
}
