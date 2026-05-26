package studydoc.db.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;

    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String school;

    private boolean isPrivate;

    private int followersCount;
    private int followingCount;
    private int likesCount;
    private int postsCount;
    private int commentsCount;

    public UserEntity(String id, String fullName, String username, String email,
                      String phoneNumber, String avatarUrl, String gender,
                      LocalDate dateOfBirth, String address, String school,
                      int followersCount, int followingCount, int likesCount,
                      int postsCount, int commentsCount) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.school = school;
        this.isPrivate = false;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.likesCount = likesCount;
        this.postsCount = postsCount;
        this.commentsCount = commentsCount;
    }

}
