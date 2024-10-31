package getterson.insight.mappers;

import getterson.insight.dtos.PreferenceDTO;
import getterson.insight.entities.PreferenceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreferenceMapper {
    PreferenceDTO toDTO(PreferenceEntity preference);
    PreferenceEntity toEntity(PreferenceDTO preferenceDTO);
}
