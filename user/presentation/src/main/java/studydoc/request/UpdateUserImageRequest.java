package studydoc.request;

import lombok.Data;

@Data
public class UpdateUserImageRequest {
    private Long avatarId;
    private String avatarUrl;
}
