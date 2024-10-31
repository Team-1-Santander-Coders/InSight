package getterson.insight.mappers;

import getterson.insight.dtos.TopicPreferenceDTO;
import getterson.insight.entities.TopicPreferenceEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TopicPreferenceMapper {
    @Mapping(source = "user.id", target = "userId")
    TopicPreferenceEntity toEntity(TopicPreferenceDTO topicPreferenceDTO);

    @Mapping(target = "user.id", source = "userId")
    TopicPreferenceDTO toDTO(TopicPreferenceEntity topicPreferenceEntity);
}
