package studydoc.vo;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class User {
    private static final Validator validator;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private String id;

    private String fullName;
    
    private String username;
    
    @NotBlank(message = "Password không được để trống")
    private String password;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
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

    public User(String id, String fullName, String username, String password, String email,
                      String phoneNumber, String avatarUrl, String gender,
                      LocalDate dateOfBirth, String address, String school,
                      int followersCount, int followingCount, int likesCount,
                      int postsCount, int commentsCount) {
        this.id = id;
        this.fullName = fullName;
        this.setUsername(username);
        this.password = password;
        this.setEmail(email);
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.school = school;
        this.isprivate = false;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.likesCount = likesCount;
        this.postsCount = postsCount;
        this.commentsCount = commentsCount;
    }

    public void setUsername(String username) {
        this.username = username;
        validateProperty("username");
    }

    public void setEmail(String email) {
        this.email = email;
        validateProperty("email");
    }

    private void validateProperty(String propertyName) {
        Set<ConstraintViolation<User>> violations = validator.validateProperty(this, propertyName);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }

    public void updateProfile(String fullName, String phoneNumber, String avatarUrl, 
                              String gender, LocalDate dateOfBirth, String address, String school, boolean isPrivate) {
        if (fullName != null) this.fullName = fullName;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (avatarUrl != null) this.avatarUrl = avatarUrl;
        if (gender != null) this.gender = gender;
        if (dateOfBirth != null) this.dateOfBirth = dateOfBirth;
        if (address != null) this.address = address;
        if (school != null) this.school = school;
        this.isprivate = isPrivate;
    }

    public void updateInfo(String fullName, String phoneNumber, String gender, 
                           LocalDate dateOfBirth, String address, String school, boolean isPrivate) {
        if (fullName != null) this.fullName = fullName;
        if (phoneNumber != null) this.phoneNumber = phoneNumber;
        if (gender != null) this.gender = gender;
        if (dateOfBirth != null) this.dateOfBirth = dateOfBirth;
        if (address != null) this.address = address;
        if (school != null) this.school = school;
        this.isprivate = isPrivate;
    }

    public void updateAvatar(String avatarUrl) {
        if (avatarUrl != null) this.avatarUrl = avatarUrl;
    }
}