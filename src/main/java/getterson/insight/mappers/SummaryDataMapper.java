package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDataDTO;
import getterson.insight.entities.SummaryDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SummaryDataMapper {
    SummaryDataMapper INSTANCE = Mappers.getMapper(SummaryDataMapper.class);

    SummaryDataDTO toDTO(SummaryDataEntity entity);
    SummaryDataEntity toEntity(SummaryDataDTO dto);
}
