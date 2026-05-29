package studydocs.media.service;

import studydocs.media.web.rest.dto.request.InitUploadRequest;
import studydocs.media.web.rest.dto.response.InitUploadResponse;
import studydocs.media.web.rest.dto.response.MediaStatusResponse;


public interface MediaService {
    InitUploadResponse initUpload(InitUploadRequest request);
    void completeUpload(Long mediaId);
    MediaStatusResponse getMediaById(Long mediaId);
}
