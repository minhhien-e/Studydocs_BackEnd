package studydoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studydoc.repository.UserRepository;
import studydoc.vo.User;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    public void verifyUserUniqueness(User user) {
        Assert.isTrue(!userRepository.existsByUsername(user.getUsername()), "Username đã tồn tại: " + user.getUsername());
        Assert.isTrue(!userRepository.existsByEmail(user.getEmail()), "Email đã tồn tại: " + user.getEmail());
    }

    public void verifyUserExists(String id) {
        Assert.isTrue(userRepository.findById(id).isPresent(), "User không tồn tại: " + id);
    }
}
