package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class UpdateUserImage implements UserCommand {
    String id;
    Long mediaId;
}
