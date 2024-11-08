package getterson.insight.mappers;

import getterson.insight.dtos.GeneratedSummaryDTO;
import getterson.insight.entities.GeneratedSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class GeneratedSummaryMapper implements Mapper<GeneratedSummaryEntity, GeneratedSummaryDTO> {
    public GeneratedSummaryEntity toEntity(GeneratedSummaryDTO dto) {
        return new GeneratedSummaryEntity(dto.id(), dto.summary(), dto.description(), dto.imageUrl());
    }

    public GeneratedSummaryDTO toDTO(GeneratedSummaryEntity entity) {
        return new GeneratedSummaryDTO(entity.getId(), entity.getSummary(), entity.getDescription(), entity.getImageURL(), entity.getAudioURL());
    }
}
