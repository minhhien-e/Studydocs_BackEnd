package studydoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
@Data
@AllArgsConstructor
public class UserDTO {

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

    private boolean isprivate;

    private int followersCount;
    private int followingCount;
    private int likesCount;
    private int postsCount;
    private int commentsCount;

}
