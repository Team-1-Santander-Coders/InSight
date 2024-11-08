package getterson.insight.repositories;

import getterson.insight.entities.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    Optional<TopicEntity> findById (long id);
    Optional<TopicEntity> findByTitle (String title);
}
