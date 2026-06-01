package studydoc.db.mapper;

import org.springframework.stereotype.Component;
import studydoc.db.entity.UserEntity;
import studydoc.vo.User;

@Component
public class EntityMapper {

    public UserEntity voToUserEntity(User user) {
        if (user == null)
            return null;

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setKeycloakId(user.getKeycloakId());
        entity.setFullName(user.getFullName());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setAvatarId(user.getAvatarId());
        entity.setAvatarUrl(user.getAvatarUrl());
        entity.setGender(user.getGender());
        entity.setDateOfBirth(user.getDateOfBirth());
        entity.setAddress(user.getAddress());
        entity.setSchool(user.getSchool());
        entity.setPrivate(user.isIsprivate());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setFollowersCount(user.getFollowersCount());
        entity.setFollowingCount(user.getFollowingCount());
        entity.setLikesCount(user.getLikesCount());
        entity.setPostsCount(user.getPostsCount());
        entity.setCommentsCount(user.getCommentsCount());
        return entity;
    }

    public User entityToUserVO(UserEntity entity) {
        if (entity == null)
            return null;

        return new User(
                entity.getId(),
                entity.getKeycloakId(),
                entity.getFullName(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getAvatarId(),
                entity.getAvatarUrl(),
                entity.getGender(),
                entity.getDateOfBirth(),
                entity.getAddress(),
                entity.getSchool(),
                entity.isPrivate(),
                entity.getCreatedAt(),
                entity.getFollowersCount(),
                entity.getFollowingCount(),
                entity.getLikesCount(),
                entity.getPostsCount(),
                entity.getCommentsCount());
    }
}
