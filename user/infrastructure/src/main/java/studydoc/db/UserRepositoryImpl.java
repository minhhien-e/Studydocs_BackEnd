package studydoc.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import studydoc.db.entity.UserEntity;
import studydoc.db.mapper.EntityMapper;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMongoRepository repository;
    private final EntityMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = repository.save(mapper.voToUserEntity(user));
        return mapper.entityToUserVO(entity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::entityToUserVO);
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return repository.findByKeycloakId(keycloakId).map(mapper::entityToUserVO);
    }

    @Override

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(mapper::entityToUserVO);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(mapper::entityToUserVO)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
