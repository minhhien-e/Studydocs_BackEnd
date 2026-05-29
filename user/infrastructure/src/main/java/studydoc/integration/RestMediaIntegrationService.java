package studydoc.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class RestMediaIntegrationService implements MediaIntegrationService {
    private final RestClient restClient;

    public RestMediaIntegrationService(
            @Value("${app.media-service.url:http://localhost:8082}") String mediaServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(removeTrailingSlash(mediaServiceUrl))
                .build();
    }

    @Override
    public String getMediaUrl(Long mediaId) {
        if (mediaId == null) {
            return null;
        }

        try {
            MediaStatusResponse response = restClient.get()
                    .uri("/api/v1/media/{mediaId}", mediaId)
                    .retrieve()
                    .body(MediaStatusResponse.class);

            return response != null ? response.downloadUrl() : null;
        } catch (RestClientException e) {
            log.warn("Could not fetch media URL for mediaId={}", mediaId, e);
            return null;
        }
    }

    private String removeTrailingSlash(String value) {
        return value == null ? "" : value.replaceAll("/+$", "");
    }

    private record MediaStatusResponse(
            Long mediaId,
            String state,
            String downloadUrl
    ) {
    }
}
