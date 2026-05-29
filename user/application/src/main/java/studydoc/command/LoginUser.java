package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class LoginUser implements UserCommand {
    String username;
    String password;
}
