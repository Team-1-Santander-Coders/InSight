package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.services.SummaryDataService;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class SummaryDataMapper implements Mapper<SummaryDataEntity, SummaryDataDTO> {

    private final SummaryDataService summaryDataService;

    public SummaryDataMapper(SummaryDataService summaryDataService) {
        this.summaryDataService = summaryDataService;
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
        return new SummaryDataDTO(summaryDataEntity.getId(), summaryDataEntity.getDate(), summaryDataEntity.getAbout(), summaryDataEntity.getDetails(), summaryDataEntity.getImage());
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
}
