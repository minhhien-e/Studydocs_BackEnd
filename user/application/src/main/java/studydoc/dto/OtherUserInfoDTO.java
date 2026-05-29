package studydoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtherUserInfoDTO {
    private String avatarUrl;
    private String fullName;
    private String school;
    private int followersCount;
    private int followingCount;
    private int likesCount;
    private int postsCount;
    private int commentsCount;
}
