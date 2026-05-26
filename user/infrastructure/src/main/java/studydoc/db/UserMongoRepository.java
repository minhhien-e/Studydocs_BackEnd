package studydoc.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import studydoc.db.entity.UserEntity;

@Repository
public interface UserMongoRepository extends MongoRepository<UserEntity, String> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
