package studydoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    public void verifyUserUniqueness(User user) {
        verifyUsernameUniqueness(user.getUsername());
        if (StringUtils.hasText(user.getEmail())) {
            verifyEmailUniqueness(user.getEmail());
        }
    }

    public void verifyUsernameUniqueness(String username) {
        Assert.isTrue(!userRepository.existsByUsername(username), "Username đã tồn tại: " + username);
    }

    public void verifyEmailUniqueness(String email) {
        Assert.isTrue(!userRepository.existsByEmail(email), "Email đã tồn tại: " + email);
    }

    public void verifyUserExists(String id) {
        Assert.isTrue(userRepository.findById(id).isPresent(), "User không tồn tại: " + id);
    }
}
