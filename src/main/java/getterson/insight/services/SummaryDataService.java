package getterson.insight.services;
import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.repositories.SummaryDataRepository;
import getterson.insight.repositories.SummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class SummaryDataService {
    private final SummaryDataRepository summaryDataRepository;

    @Autowired
    private SummaryDataRepository summaryDataRepository;
  
    @Autowired
    private SummaryRepository summaryRepository;

    public void save(SummaryDataEntity summaryDataEntity) {
        summaryDataRepository.save(summaryDataEntity);
    }

    public SummaryDataEntity findById(Long id) throws Exception {
        Optional<SummaryDataEntity> summaryDataEntity = summaryDataRepository.findById(id);
        if(summaryDataEntity.isPresent()) return summaryDataEntity.get();

        throw new Exception("SummaryData nao encontrado");
    }
}
