package getterson.insight.repositories;

import getterson.insight.entities.SummaryDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SummaryDataRepository extends JpaRepository<SummaryDataEntity, String> {

    List<SummaryDataEntity> findByFinalDate(LocalDate date);

    Page<SummaryDataEntity> findByFinalDate(LocalDate date, Pageable pageable);

    List<SummaryDataEntity> findByUserId(String userId);
    Page<SummaryDataEntity> findByUserId(String userId, Pageable pageable);

}
