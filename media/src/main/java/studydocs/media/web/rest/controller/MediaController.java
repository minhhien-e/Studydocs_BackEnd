package studydocs.media.web.rest.controller;

import studydocs.media.web.rest.dto.request.InitUploadRequest;
import studydocs.media.web.rest.dto.response.InitUploadResponse;
import studydocs.media.web.rest.dto.response.MediaStatusResponse;
import studydocs.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/init-upload")
    public ResponseEntity<InitUploadResponse> initUpload(@RequestBody InitUploadRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        request.setIdempotencyKey(idempotencyKey);
        return ResponseEntity.ok(mediaService.initUpload(request));
    }

    @PutMapping("/{mediaId}/complete-upload")
    public ResponseEntity<Void> completeUpload(@PathVariable Long mediaId) {
        mediaService.completeUpload(mediaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<MediaStatusResponse> getMediaStatus(@PathVariable Long mediaId) {
        return ResponseEntity.ok(mediaService.getMediaById(mediaId));
    }
}
