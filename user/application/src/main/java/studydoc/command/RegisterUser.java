package studydoc.command;

import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value(staticConstructor = "commandOf")
public class RegisterUser implements UserCommand {
    String fullName;
    String username;
    String email;
    String phoneNumber;
    String avatarUrl;
    String gender;
    LocalDate dateOfBirth;
    String address;
    String school;
    LocalDateTime timestamp = LocalDateTime.now();
}