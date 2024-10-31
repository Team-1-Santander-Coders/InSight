package getterson.insight.mappers;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.UserPreferenceEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserPreferenceMapper {
    @Mapping(target = "user.id", source = "userId")
    UserPreferenceEntity toEntity(UserPreferenceDTO userPreferenceDTO);

    @Mapping(source = "user.id", target = "userId")
    UserPreferenceDTO toDTO(UserPreferenceEntity userPreferenceEntity);
}
