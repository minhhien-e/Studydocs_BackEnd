package studydocs.media.web.rest.dto.response;

import studydocs.media.model.enums.AssetState;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MediaStatusResponse {
    private Long mediaId;
    private String originalFilename;
    private AssetState state;
    private String downloadUrl;
    private Map<String, String> variants;
    private String rejectReason;
}
