package studydoc.command;

import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "commandOf")
public class RegisterUser implements UserCommand {
    String fullName;
    String username;
    String password;
    String email;
    LocalDateTime timestamp = LocalDateTime.now();
}