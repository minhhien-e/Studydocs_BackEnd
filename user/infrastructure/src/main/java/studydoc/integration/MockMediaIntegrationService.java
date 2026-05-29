package studydoc.integration;

import org.springframework.stereotype.Service;

@Service
public class MockMediaIntegrationService implements MediaIntegrationService {
    @Override
    public String getMediaUrl(Long mediaId) {
        // Mock implementation. Later this will be replaced with a gRPC client to media-service.
        return "https://res.cloudinary.com/demo/image/upload/v1614761234/sample.jpg";
    }
}
