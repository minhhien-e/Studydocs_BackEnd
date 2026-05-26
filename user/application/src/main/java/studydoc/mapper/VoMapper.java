package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.dto.UserDTO;
import studydoc.vo.User;

@Component
public class VoMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId()
                , user.getFullName()
                , user.getUsername()
                , user.getEmail()
                , user.getPhoneNumber()
                , user.getAvatarUrl()
                , user.getGender()
                , user.getDateOfBirth()
                , user.getAddress()
                , user.getSchool()
                , user.isIsprivate()
                , user.getFollowersCount()
                , user.getFollowingCount()
                , user.getLikesCount()
                , user.getPostsCount()
                , user.getCommentsCount());
    }

    public studydoc.dto.OtherUserInfoDTO toOtherUserInfoDTO(User user) {
        return new studydoc.dto.OtherUserInfoDTO(
                user.getAvatarUrl(),
                user.getFullName(),
                user.getSchool(),
                user.getFollowersCount(),
                user.getFollowingCount(),
                user.getLikesCount(),
                user.getPostsCount(),
                user.getCommentsCount()
        );
    }
}
