package getterson.insight.services;

import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.repositories.SummaryDataRepository;

import org.springframework.stereotype.Service;

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
}
