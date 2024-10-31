package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SummaryDataMapper {
    SummaryDataDTO toDTO(SummaryDataEntity entity);
    SummaryDataEntity toEntity(SummaryDataDTO dto);
}
