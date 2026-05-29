package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class GetAllUsers implements UserCommand {
}
