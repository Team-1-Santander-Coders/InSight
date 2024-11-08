package getterson.insight.services;

import getterson.insight.dtos.TopicPreferenceDTO;
import getterson.insight.entities.TopicEntity;
import getterson.insight.entities.TopicPreferenceEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.mappers.TopicPreferenceMapper;
import getterson.insight.repositories.TopicPreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicPreferenceService {

    private final TopicPreferenceMapper topicPreferenceMapper;
    private final TopicPreferenceRepository topicPreferenceRepository;

    public TopicPreferenceService(TopicPreferenceMapper topicPreferenceMapper, TopicPreferenceRepository topicPreferenceRepository) {
        this.topicPreferenceMapper = topicPreferenceMapper;
        this.topicPreferenceRepository = topicPreferenceRepository;
    }

    public List<TopicPreferenceDTO> getPreferenceDTOsByUserId(long userId) {
        return topicPreferenceRepository.findAllByUserIdAsDTO(userId);
    }

    public TopicPreferenceEntity findById(long id) throws Exception {
        Optional<TopicPreferenceEntity> topicPreferenceEntity = topicPreferenceRepository.findById(id);
        if(topicPreferenceEntity.isPresent()) return topicPreferenceEntity.get();

        throw new Exception("TopicPreference não encontrado");
    }

    public TopicPreferenceEntity createTopicPreference(UserEntity user, TopicEntity topic) {
        TopicPreferenceEntity entity = new TopicPreferenceEntity(user, topic);
        return topicPreferenceRepository.saveAndFlush(entity);
    }

    public TopicPreferenceDTO updateTopicPreference(TopicPreferenceDTO topicPreferenceDTO) throws Exception {
        TopicPreferenceEntity topicPreference = topicPreferenceMapper.toEntity(topicPreferenceDTO);
        topicPreference = topicPreferenceRepository.saveAndFlush(topicPreference);

        return topicPreferenceMapper.toDTO(topicPreference);
    }
}
