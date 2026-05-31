package studydoc.command;

import lombok.Value;
import java.time.LocalDate;

@Value(staticConstructor = "commandOf")
public class UpdateUser implements UserCommand {
    String id;
    String fullName;
    String phoneNumber;
    String avatarUrl;
    String gender;
    LocalDate dateOfBirth;
    String address;
    String school;
    boolean isPrivate;
}
