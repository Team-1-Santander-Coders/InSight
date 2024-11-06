package getterson.insight.repositories;

import getterson.insight.entities.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TopicRepository extends JpaRepository<TopicEntity, Long> {}
