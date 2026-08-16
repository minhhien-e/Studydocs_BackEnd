package com.studydocs.modules.system.service;

import com.studydocs.modules.system.dto.SystemDtos;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    SystemDtos.MediaResponse uploadFile(MultipartFile file, String ownerId);
    byte[] getFile(String fileName);
}
