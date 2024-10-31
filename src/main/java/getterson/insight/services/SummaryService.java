package getterson.insight.services;

import getterson.insight.entities.SummaryEntity;
import getterson.insight.repositories.SummaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SummaryService {

    private final SummaryRepository summaryRepository;

    @Autowired
    public SummaryService(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    // Salvar resumos
    public SummaryEntity saveSummary(SummaryEntity summaryEntity) {
        return summaryRepository.save(summaryEntity);
    }

    // Buscar todos os resumos
    public List<SummaryEntity> findAllSummaries() {
        return summaryRepository.findAll();
    }

    // Buscar resumo por ID
    public Optional<SummaryEntity> findSummaryById(Long id) {
        return summaryRepository.findById(id);
    }

    // Deletar resumo por ID
    public void deleteSummary(Long id) {
        summaryRepository.deleteById(id);
    }

    // Buscar resumos por categoria específica
    public List<SummaryEntity> findByCategoriesContaining(String category) {
        return summaryRepository.findByCategoriesContaining(category);
    }

    // Buscar resumos por título do tópico associado
    public List<SummaryEntity> findByTopicTitle(String topicTitle) {
        return summaryRepository.findByTopicTitle(topicTitle);
    }

    // Buscar resumos paginados por título do tópico associado
    public Page<SummaryEntity> findByTopicTitle(String topicTitle, Pageable pageable) {
        return summaryRepository.findByTopicTitle(topicTitle, pageable);
    }

    // Buscar resumos paginados por categoria específica
    public Page<SummaryEntity> findByCategoriesContaining(String category, Pageable pageable) {
        return summaryRepository.findByCategoriesContaining(category, pageable);
    }
}
