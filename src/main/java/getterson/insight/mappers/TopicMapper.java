package getterson.insight.mappers;

import getterson.insight.dtos.TopicDTO;
import getterson.insight.entities.TopicEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDTO toDTO(TopicEntity topicEntity);
    TopicEntity toEntity (TopicDTO topicDTO);
}
