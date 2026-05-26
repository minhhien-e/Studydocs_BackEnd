package studydoc.repository;

import org.springframework.stereotype.Repository;
import studydoc.vo.User;
@Repository
public interface UserRepository {
    User save(User user);
    java.util.Optional<User> findById(String id);
    java.util.List<User> findAll();
    void deleteById(String id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
