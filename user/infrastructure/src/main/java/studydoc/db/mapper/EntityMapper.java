package studydoc.db.mapper;

import org.springframework.stereotype.Component;
import studydoc.db.entity.UserEntity;
import studydoc.vo.User;

@Component
public class EntityMapper {

    // VO → Entity
    public UserEntity voToUserEntity(User user) {
        if (user == null) return null;

        return new UserEntity(user.getId(),user.getFullName(),user.getUsername(),user.getPassword(),user.getEmail(),user.getPhoneNumber(),user.getAvatarUrl(),user.getGender(),user.getDateOfBirth(),user.getAddress(), user.getSchool(), user.getFollowersCount(), user.getFollowingCount(), user.getLikesCount(), user.getPostsCount(), user.getCommentsCount());
    }

    // Entity → VO
    public User entityToUserVO(UserEntity entity) {
        if (entity == null) return null;

        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getAvatarUrl(),
                entity.getGender(),
                entity.getDateOfBirth(),
                entity.getAddress(),
                entity.getSchool(),
                entity.getFollowersCount(),
                entity.getFollowingCount(),
                entity.getLikesCount(),
                entity.getPostsCount(),
                entity.getCommentsCount()
        );
    }
}
