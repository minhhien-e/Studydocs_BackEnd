package studydocs.media.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import studydocs.media.web.rest.dto.request.InitUploadRequest;
import studydocs.media.web.rest.dto.response.InitUploadResponse;
import studydocs.media.web.rest.dto.response.MediaStatusResponse;
import studydocs.media.api.service.IdempotencyService;
import studydocs.media.api.service.MediaService;
import studydocs.media.util.MediaTypeDetector;
import studydocs.media.model.entity.MediaAsset;
import studydocs.media.model.enums.AssetState;
import studydocs.media.model.enums.HttpMethod;
import studydocs.media.model.enums.MediaType;
import studydocs.media.core.repository.MediaAssetRepository;
import studydocs.media.storage.StorageProvider;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {
    private final MediaAssetRepository mediaAssetRepository;
    private final StorageProvider storageProvider;
        private final IdempotencyService idempotencyService;
    private final MediaTypeDetector mediaTypeDetector;
    private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    @Override
    @Transactional
    public InitUploadResponse initUpload(InitUploadRequest request) {
        String key = "users/" + request.getOwnerId() + "/" + java.util.java.util.UUID.randomUUID() + "_" + request.getFileName();

        MediaAsset asset = new MediaAsset();
        asset.setOriginalFilename(request.getFileName());

        MediaType detectedMediaType = mediaTypeDetector.detect(request.getContentType(), request.getFileName());
        asset.setMediaType(detectedMediaType);

        asset.setMimeType(request.getContentType());
        asset.setSizeBytes(request.getSizeBytes());
        asset.setOwnerType(request.getOwnerType());
        asset.setOwnerId(request.getOwnerId());
        asset.setOriginalKey(key);
        asset.setState(AssetState.PENDING_UPLOAD);
        mediaAssetRepository.save(asset);

        String uploadUrl = storageProvider.generatePresignedUrl(key, HttpMethod.PUT, Duration.ofMinutes(15));

        var response = InitUploadResponse.builder()
            .mediaId(asset.getId())
            .uploadUrl(uploadUrl)
            .state(asset.getState().name())
            .build();
        try {
            idempotencyService.save(request.getIdempotencyKey(), objectMapper.writeValueAsString(response), Duration.ofMinutes(15).getSeconds());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    @Transactional
    public void completeUpload(Long mediaId) {
        MediaAsset asset = mediaAssetRepository.findById(mediaId).orElseThrow(() -> new studydocs.media.web.rest.exception.MediaNotFoundException(mediaId.toString()));

        if (asset.getState() != AssetState.PENDING_UPLOAD) {
            throw new IllegalStateException("Asset is not in PENDING_UPLOAD state");
        }

        asset.setState(AssetState.UPLOADED);
        mediaAssetRepository.save(asset);

            }

    @Override
    @Transactional(readOnly = true)
    public MediaStatusResponse getMediaById(Long mediaId) {
        MediaAsset asset = mediaAssetRepository.findById(mediaId).orElseThrow(() -> new studydocs.media.web.rest.exception.MediaNotFoundException(mediaId.toString()));

        String downloadUrl = null;
        if (asset.getState() == AssetState.ACTIVE) {
            downloadUrl = storageProvider.generatePresignedUrl(asset.getOriginalKey(), HttpMethod.GET, Duration.ofHours(1));

            meterRegistry.counter("media.private.url.generated", "type", asset.getMediaType().name()).increment();
            log.info("[AUDIT] Caller requested Private Signed URL for Asset '{}' (Owner '{}')",
                     mediaId, asset.getOwnerId());
        }

        return MediaStatusResponse.builder()
            .mediaId(asset.getId())
            .originalFilename(asset.getOriginalFilename())
            .state(asset.getState())
            .downloadUrl(downloadUrl)
            .rejectReason(asset.getRejectReason())
                        .build();
    }
}
