package studydoc.repository;

import studydoc.vo.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(String id);

    List<User> findAll();

    void deleteById(String id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByKeycloakId(String keycloakId);
}
