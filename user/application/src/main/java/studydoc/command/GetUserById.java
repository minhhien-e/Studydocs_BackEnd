package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class GetUserById implements UserCommand {
    String id;
}
