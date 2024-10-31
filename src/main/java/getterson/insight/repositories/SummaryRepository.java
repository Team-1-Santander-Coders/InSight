package getterson.insight.repositories;


import getterson.insight.entities.SummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface SummaryRepository extends JpaRepository<SummaryEntity, Long> {

    //buscar por categoria específica
    List<SummaryEntity> findByCategoriesContaining(String category);

    //buscar por titulo do topico associado
    List<SummaryEntity> findByTopicTitle(String topicTitle);


    //ou com paginacao
    Page<SummaryEntity> findByCategoriesContaining(String category, Pageable pageable);
    Page<SummaryEntity> findByTopicTitle(String topicTitle, Pageable pageable);
}
