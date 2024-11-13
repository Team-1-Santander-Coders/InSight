package getterson.insight.repositories;

import getterson.insight.entities.SummaryDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SummaryDataRepository extends JpaRepository<SummaryDataEntity, String> {
    List<SummaryDataEntity> findByFinalDate(LocalDate date);

    Page<SummaryDataEntity> findByFinalDate(LocalDate date, Pageable pageable);

    Optional<SummaryDataEntity> findByTopicTitleAndInitialDateAndFinalDate(String topicTitle, LocalDate initialDate, LocalDate finalDate);

}