package studydocs.media.web.rest.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class InitUploadResponse {
    private Long mediaId;
    private String uploadUrl;
    private String state;
}
