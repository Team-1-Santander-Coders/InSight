package getterson.insight.services;

import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.repositories.SummaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryDataService {

    private final SummaryDataRepository summaryDataRepository;

    @Autowired
    public SummaryDataService(SummaryDataRepository summaryDataRepository) {
        this.summaryDataRepository = summaryDataRepository;
    }

    public SummaryDataEntity saveSummaryData(SummaryDataEntity summaryDataEntity) {
        return summaryDataRepository.save(summaryDataEntity);
    }

    public List<SummaryDataEntity> findAllSummaryData() {
        return summaryDataRepository.findAll();
    }

    public Optional<SummaryDataEntity> findSummaryDataById(Long id) {
        return summaryDataRepository.findById(id);
    }

    public void deleteSummaryDataById(Long id) {
        summaryDataRepository.deleteById(id);
    }

    public List<SummaryDataEntity> findByDate(LocalDate date) {
        return summaryDataRepository.findByDate(date);
    }

    public Page<SummaryDataEntity> findByAbout(String about, Pageable pageable) {
        return summaryDataRepository.findByAbout(about, pageable);
    }

    public Page<SummaryDataEntity> findByDate(LocalDate date, Pageable pageable) {
        return summaryDataRepository.findByDate(date, pageable);
    }
}
