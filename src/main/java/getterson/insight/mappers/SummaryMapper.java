package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDTO;

import getterson.insight.entities.SummaryEntity;
import getterson.insight.services.SummaryService;
import org.springframework.stereotype.Component;


@Component
public class SummaryMapper implements Mapper<SummaryEntity, SummaryDTO> {

    private final SummaryDataMapper summaryDataMapper;
    private final SummaryService summaryService;

    public SummaryMapper(SummaryDataMapper summaryDataMapper, SummaryService summaryService) {
        this.summaryDataMapper = summaryDataMapper;
        this.summaryService = summaryService;
    }

    public SummaryDTO toDTO(SummaryEntity summaryEntity){
        return new SummaryDTO(summaryEntity.getId(), summaryEntity.getCategories(), summaryDataMapper.toDTO(summaryEntity.getSummaryDataList()), summaryEntity.getTopic().getTitle());
    }

    public SummaryEntity toEntity(SummaryDTO summaryDTO) throws Exception {
        return summaryService.findSummaryById(summaryDTO.id());
    }
}

