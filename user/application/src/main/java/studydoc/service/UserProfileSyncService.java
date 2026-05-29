package studydoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import studydoc.port.KeycloakUserInfo;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@Service
@RequiredArgsConstructor
public class UserProfileSyncService {
    private final UserRepository userRepository;

    public void ensureProfileExists(KeycloakUserInfo userInfo) {
        if (userRepository.findByKeycloakId(userInfo.keycloakId()).isPresent()) {
            return;
        }

        String username = userInfo.username();
        if (!StringUtils.hasText(username)) {
            username = userInfo.keycloakId();
        }

        User user = User.createNewProfile(userInfo.keycloakId(), username, userInfo.fullName());
        if (StringUtils.hasText(userInfo.email())) {
            user.setEmail(userInfo.email());
        }
        userRepository.save(user);
    }
}
