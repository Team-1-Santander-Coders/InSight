package getterson.insight.repositories;

import getterson.insight.entities.GeneratedSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneratedSummaryRepository extends JpaRepository<GeneratedSummaryEntity, Long> {
    Optional<GeneratedSummaryEntity> findById(long id);
}