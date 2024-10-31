package getterson.insight.mappers;

import getterson.insight.dtos.PreferenceDTO;
import getterson.insight.entities.PreferenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreferenceMapper {

    @Mapping(source = "user.id", target = "userId")
    PreferenceDTO preferenceToPreferenceDTO(PreferenceEntity preference);

    @Mapping(target = "user.id", source = "userId")
    PreferenceEntity preferenceDTOToPreferenceEntity(PreferenceDTO preferenceDTO);
}
