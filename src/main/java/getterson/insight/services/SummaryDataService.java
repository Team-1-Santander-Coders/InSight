package getterson.insight.services;

import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.repositories.SummaryDataRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SummaryDataService {
    private final SummaryDataRepository summaryDataRepository;

    public SummaryDataService(SummaryDataRepository summaryDataRepository) {
        this.summaryDataRepository = summaryDataRepository;
    }

    public void save(SummaryDataEntity summaryDataEntity) {
        summaryDataRepository.save(summaryDataEntity);
    }

    public SummaryDataEntity findById(Long id) {
        return summaryDataRepository.findById(id).get();
    }

    public List<SummaryDataEntity> findByDate(LocalDate date) {
        return summaryDataRepository.findByDate(date);
    }

    public Page<SummaryDataEntity> findByDate(LocalDate date, Pageable pageable) {
        return summaryDataRepository.findByDate(date, pageable);
    }
}
