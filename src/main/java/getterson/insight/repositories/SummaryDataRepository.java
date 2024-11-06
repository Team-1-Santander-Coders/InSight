package getterson.insight.repositories;

import getterson.insight.entities.SummaryDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SummaryDataRepository extends JpaRepository <SummaryDataEntity, Long> {
    List<SummaryDataEntity> findByAbout(String about);
    List<SummaryDataEntity> findByDate(LocalDate date);

    Page<SummaryDataEntity> findByAbout(String about, Pageable pageable);
    Page<SummaryDataEntity> findByDate(LocalDate date, Pageable pageable);
}
