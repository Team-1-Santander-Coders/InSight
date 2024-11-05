package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import getterson.insight.services.SummaryDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class SummaryDataMapper implements Mapper<SummaryDataEntity, SummaryDataDTO> {

    @Autowired
    private SummaryDataService summaryDataService;

    public SummaryDataDTO toDTO(SummaryDataEntity summaryDataEntity){
        return new SummaryDataDTO(summaryDataEntity.getId(), summaryDataEntity.getDate(), summaryDataEntity.getAbout(), summaryDataEntity.getDetails(), summaryDataEntity.getImage());
    }

    public SummaryDataEntity toEntity(SummaryDataDTO summaryDataDTO) throws Exception {
        return summaryDataService.findById(summaryDataDTO.id());
    }
}
