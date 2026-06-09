package studydoc.port;

import java.util.Optional;

/**
 * Lấy access token của request hiện tại từ security context (sau khi JWT đã được validate).
 */
public interface AuthenticatedUserAccessor {
    Optional<String> getUserId();
}
