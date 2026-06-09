package studydoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studydoc.port.AuthenticatedUserAccessor;

import studydoc.repository.UserRepository;
import studydoc.vo.User;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final AuthenticatedUserAccessor authenticatedUserAccessor;
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String userId = authenticatedUserAccessor.getUserId()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user id (X-User-Id header)"));

        return userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy profile người dùng với keycloakId: " + userId));
    }
}
