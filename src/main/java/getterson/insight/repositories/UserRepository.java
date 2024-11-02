package getterson.insight.repositories;

import getterson.insight.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findById(Long id);
    public Optional<UserEntity> findByDocument(String document);
    public Optional<UserEntity> findByEmail(String email);
    public Optional<UserEntity> findByUsername(String username);
    public Optional<UserEntity> findByEmailAndPassword(String email, String password);


}
