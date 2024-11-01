package getterson.insight.mappers;

import getterson.insight.dtos.TopicPreferenceDTO;
import getterson.insight.entities.TopicPreferenceEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TopicPreferenceMapper {
    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "user", expression = "java(mapUserIdToUser(topicPreferenceDTO.userId()))")
    public abstract TopicPreferenceEntity toEntity(TopicPreferenceDTO topicPreferenceDTO);

    @Mapping(target = "user.id", source = "userId")
    public abstract TopicPreferenceDTO toDTO(TopicPreferenceEntity topicPreferenceEntity);

    protected UserEntity mapUserIdToUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o ID: " + userId));
    }
}
