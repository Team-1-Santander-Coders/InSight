package getterson.insight.repositories;

import getterson.insight.dtos.SummarySimpleDataDTO;
import getterson.insight.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(long id);
    Optional<UserEntity> findByDocument(String document);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmailAndPassword(String email, String password);
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    @Query("SELECT new getterson.insight.dtos.SummarySimpleDataDTO(s.id, s.finalDate, s.initialDate, s.description, s.image, s.audio) " +
            "FROM SummaryDataEntity s " +
            "JOIN s.topic t " +
            "WHERE t.user.id = :userId")
    Optional<List<SummarySimpleDataDTO>> findAllAsSimpleDTO (@Param("userId") Long userId);
}
