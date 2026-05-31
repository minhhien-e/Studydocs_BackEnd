package studydoc.command.auth;

import lombok.Value;
import studydoc.command.UserCommand;

@Value(staticConstructor = "commandOf")
public class AuthLogout implements UserCommand {
    String refreshToken;
}
