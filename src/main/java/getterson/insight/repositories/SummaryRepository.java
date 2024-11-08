package getterson.insight.repositories;

import getterson.insight.entities.SummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SummaryRepository extends JpaRepository<SummaryEntity, Long> {

    List<SummaryEntity> findByCategoriesContaining(String category);
    List<SummaryEntity> findByTopicTitle(String topicTitle);

    Page<SummaryEntity> findByCategoriesContaining(String category, Pageable pageable);
    Page<SummaryEntity> findByTopicTitle(String topicTitle, Pageable pageable);
}