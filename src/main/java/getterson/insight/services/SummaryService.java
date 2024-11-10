package getterson.insight.services;

import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.dtos.SummaryRequestDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.entities.SummaryEntity;
import getterson.insight.repositories.SummaryDataRepository;
import getterson.insight.repositories.SummaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final HashSet<SummaryRequestDTO> processingRequests = new HashSet<>();
    private final SummaryDataRepository summaryDataRepository;


    public SummaryService(SummaryRepository summaryRepository, SummaryDataRepository summaryDataRepository) {
        this.summaryRepository = summaryRepository;
        this.summaryDataRepository = summaryDataRepository;
    }

    public SummaryEntity saveSummary(SummaryEntity summaryEntity) {
        return summaryRepository.save(summaryEntity);
    }

    public List<SummaryEntity> findAllSummaries() {
        return summaryRepository.findAll();
    }

    public SummaryEntity findSummaryById(long id) {
        Optional<SummaryEntity> summaryEntity = summaryRepository.findById(id);
        return summaryEntity.get();
    }

    public void deleteSummary(Long id) {
        summaryRepository.deleteById(id);
    }

    public List<SummaryEntity> findByCategoriesContaining(String category) {
        return summaryRepository.findByCategoriesContaining(category);
    }

    public List<SummaryEntity> findByTopicTitle(String topicTitle) {
        return summaryRepository.findByTopicTitle(topicTitle);
    }

    public Page<SummaryEntity> findByTopicTitle(String topicTitle, Pageable pageable) {
        return summaryRepository.findByTopicTitle(topicTitle, pageable);
    }

    public Page<SummaryEntity> findByCategoriesContaining(String category, Pageable pageable) {
        return summaryRepository.findByCategoriesContaining(category, pageable);
    }

    public boolean isRequestInProgress(String term, String startDate, String endDate) {
        return processingRequests.contains(new SummaryRequestDTO(term, startDate, endDate));
    }

    public boolean enqueueSummarizationRequest(String term, String startDate, String endDate) {
        SummaryRequestDTO request = new SummaryRequestDTO(term, startDate, endDate);
        if(processingRequests.add(request)) {
            return true;
        } else {
            return false;
        }
    }

    public void processSummarization(SummaryRequestDTO request) {
        processingRequests.remove(request);
    }

    public List<SummaryDataDTO> getSummariesForUser(String userId) {
        List<SummaryDataEntity> summaries = summaryDataRepository.findByUserId(userId);
        return summaries.stream()
                .map(summary -> new SummaryDataDTO(
                        summary.getId(),
                        summary.getInitialDate(),
                        summary.getFinalDate(),
                        summary.getDescription(),
                        summary.getSummary(),
                        summary.getImage(),
                        summary.getAudio()))
                .collect(Collectors.toList());
    }
}