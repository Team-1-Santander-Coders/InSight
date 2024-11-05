package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDTO;

import getterson.insight.entities.SummaryEntity;
import getterson.insight.services.SummaryDataService;
import getterson.insight.services.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SummaryMapper implements Mapper<SummaryEntity, SummaryDTO> {

    @Autowired
    private SummaryDataMapper summaryDataMapper;
    @Autowired
    private SummaryDataService summaryDataService;

    @Autowired
    private SummaryService summaryService;

    public SummaryDTO toDTO(SummaryEntity summaryEntity){
        return new SummaryDTO(summaryEntity.getId(), summaryEntity.getCategories(), summaryDataMapper.toDTO(summaryEntity.getSummaryDataList()), summaryEntity.getTopic().getTitle());
    };

    public SummaryEntity toEntity(SummaryDTO summaryDTO) throws Exception {
        return summaryService.findSummaryById(summaryDTO.id());
    }
}

