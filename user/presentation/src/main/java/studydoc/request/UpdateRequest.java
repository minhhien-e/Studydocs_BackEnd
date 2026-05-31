package studydoc.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRequest {
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String school;
    private boolean isPrivate;
}
