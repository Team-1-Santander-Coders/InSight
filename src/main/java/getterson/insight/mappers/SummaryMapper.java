package getterson.insight.mappers;

import getterson.insight.dtos.SummaryDTO;
import getterson.insight.entities.SummaryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface SummaryMapper {
    SummaryMapper INSTANCE = Mappers.getMapper(SummaryMapper.class);

    SummaryDTO toDTO(SummaryEntity summaryEntity);
    SummaryEntity toEntity(SummaryDTO summaryDTO);
}
