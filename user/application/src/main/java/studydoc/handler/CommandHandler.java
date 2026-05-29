package studydoc.handler;

public interface CommandHandler<C, R> {
    R handle(C command);
    Class<C> commandType();
}
