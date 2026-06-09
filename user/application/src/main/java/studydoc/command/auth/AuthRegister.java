package studydoc.command.auth;

import lombok.Value;
import studydoc.command.UserCommand;

@Value(staticConstructor = "commandOf")
public class AuthRegister implements UserCommand {
    String username;
    String password;
    String fullName;
}
