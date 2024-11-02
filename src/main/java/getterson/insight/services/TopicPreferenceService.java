package getterson.insight.services;

import getterson.insight.dtos.TopicPreferenceDTO;
import getterson.insight.entities.TopicEntity;
import getterson.insight.entities.TopicPreferenceEntity;
import getterson.insight.entities.UserEntity;
import getterson.insight.mappers.TopicPreferenceMapper;
import getterson.insight.repositories.TopicPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicPreferenceService {
    @Autowired
    private TopicPreferenceMapper topicPreferenceMapper;

    @Autowired
    private TopicPreferenceRepository topicPreferenceRepository;

    public List<TopicPreferenceDTO> getPreferencesByUserId(long userId) {
        return topicPreferenceRepository.findAllByUserIdAsDTO(userId);
    }

    public Optional<TopicPreferenceEntity> getById(long id) {
        return topicPreferenceRepository.findById(id);
    }

    public TopicPreferenceEntity createTopicPreference(UserEntity user, TopicEntity topic) {
        TopicPreferenceEntity entity = new TopicPreferenceEntity(user, topic);
        return topicPreferenceRepository.saveAndFlush(entity);
    }

    public TopicPreferenceDTO updateTopicPreference(TopicPreferenceDTO topicPreferenceDTO) {
        TopicPreferenceEntity topicPreference = topicPreferenceMapper.toEntity(topicPreferenceDTO);
        topicPreference = topicPreferenceRepository.saveAndFlush(topicPreference);

        return topicPreferenceMapper.toDTO(topicPreference);
    }
}
