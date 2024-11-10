package getterson.insight.mappers;

import getterson.insight.dtos.GeneratedSummary;
import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.entities.TopicEntity;
import getterson.insight.repositories.TopicRepository;
import getterson.insight.services.SummaryDataService;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Component
public class SummaryDataMapper implements Mapper<SummaryDataEntity, SummaryDataDTO> {

    private final SummaryDataService summaryDataService;
    private final TopicRepository topicRepository;

    public SummaryDataMapper(SummaryDataService summaryDataService, TopicRepository topicRepository) {
        this.summaryDataService = summaryDataService;
        this.topicRepository = topicRepository;
    }

    public SummaryDataDTO toDTO(SummaryDataEntity summaryDataEntity){
        return createDTO(summaryDataEntity);
    }

    public List<SummaryDataDTO> toDTO(List<SummaryDataEntity> summaryDataEntities) {
        return summaryDataEntities.stream()
                .map(this::createDTO)
                .toList();
    }

    private SummaryDataDTO createDTO(SummaryDataEntity summaryDataEntity) {
        return new SummaryDataDTO(summaryDataEntity.getId(), summaryDataEntity.getInitialDate(), summaryDataEntity.getFinalDate(), summaryDataEntity.getDescription(), summaryDataEntity.getSummary(), summaryDataEntity.getImage());
    }

    public SummaryDataEntity toEntity(SummaryDataDTO summaryDataDTO) {
        return getEntity(summaryDataDTO);
    }

    public List<SummaryDataEntity> toEntity(List<SummaryDataDTO> summaryDataDTOS) {
        return summaryDataDTOS.stream()
                .map(this::getEntity)
                .toList();
    }

    private SummaryDataEntity getEntity(SummaryDataDTO summaryDataDTO) {
        return summaryDataService.findById(summaryDataDTO.id());
    }

    public SummaryDataEntity convertGeneratedSummaryToSummaryDataEntity(String topicTitle, LocalDate initialDate, LocalDate finalDate, GeneratedSummary generatedSummary) {
        Optional<TopicEntity> topicEntityOptional = topicRepository.findByTitle(topicTitle);
        List<String> categories = List.of(generatedSummary.categories().split(", "));

        return new SummaryDataEntity(generatedSummary.id(), initialDate, finalDate,
                generatedSummary.description(), generatedSummary.summary(), generatedSummary.imageUrl(),
                generatedSummary.audioUrl(),generatedSummary.references(), categories, topicEntityOptional.get());
    }
}
