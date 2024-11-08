package getterson.insight.repositories;

import getterson.insight.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(long id);
    Optional<UserEntity> findByDocument(String document);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmailAndPassword(String email, String password);
}
