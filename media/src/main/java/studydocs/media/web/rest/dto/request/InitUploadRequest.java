package studydocs.media.web.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import studydocs.media.model.enums.OwnerType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InitUploadRequest {
    private String contentType;
    private String fileName;
    private long sizeBytes;
    private OwnerType ownerType;
    private Long ownerId;
    @JsonIgnore
    private String idempotencyKey;
}
