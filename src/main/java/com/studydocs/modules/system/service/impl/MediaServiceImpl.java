package com.studydocs.modules.system.service.impl;

import com.studydocs.infras.storage.FileStorageService;
import com.studydocs.modules.system.dto.SystemDtos;
import com.studydocs.modules.system.entity.MediaAssetEntity;
import com.studydocs.modules.system.repository.MediaAssetRepository;
import com.studydocs.modules.system.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final FileStorageService fileStorageService;
    private final MediaAssetRepository mediaAssetRepository;

    @Override
    public SystemDtos.MediaResponse uploadFile(MultipartFile file, String ownerId) {
        String storedFileName = fileStorageService.storeFile(file);
        String fileUrl = "/api/v1/media/files/" + storedFileName;

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
