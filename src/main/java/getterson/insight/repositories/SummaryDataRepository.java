package getterson.insight.repositories;

import getterson.insight.entities.SummaryDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryDataRepository extends JpaRepository <SummaryDataEntity, Long> {}
