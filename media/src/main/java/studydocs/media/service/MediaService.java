package studydocs.media.service;

import studydocs.media.web.rest.dto.request.InitUploadRequest;
import studydocs.media.web.rest.dto.response.InitUploadResponse;
import studydocs.media.web.rest.dto.response.MediaStatusResponse;


public interface MediaService {
    InitUploadResponse initUpload(InitUploadRequest request);
    void completeUpload(Long mediaId, java.util.Map<String, Object> request);
    MediaStatusResponse getMediaById(Long mediaId);
}
