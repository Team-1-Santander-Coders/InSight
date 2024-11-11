package getterson.insight.mappers;

import getterson.insight.dtos.TopicPreferenceDTO;
import getterson.insight.entities.TopicPreferenceEntity;
import getterson.insight.repositories.TopicPreferenceRepository;
import getterson.insight.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TopicPreferenceMapper implements Mapper<TopicPreferenceEntity, TopicPreferenceDTO> {

    private final TopicPreferenceRepository topicPreferenceRepository;

    public TopicPreferenceMapper(TopicPreferenceRepository topicPreferenceRepository) {
        this.topicPreferenceRepository = topicPreferenceRepository;
    }



    public TopicPreferenceDTO toDTO(TopicPreferenceEntity topicPreferenceEntity){
        return new TopicPreferenceDTO(topicPreferenceEntity.getId(), topicPreferenceEntity.getUser().getId(), topicPreferenceEntity.isSendNewsLetter(),topicPreferenceEntity.getType());
    }

    public TopicPreferenceEntity toEntity(TopicPreferenceDTO topicPreferenceDTO) throws Exception {
        Optional<TopicPreferenceEntity> topicPreferenceEntity = topicPreferenceRepository.findById(topicPreferenceDTO.id());

        if(topicPreferenceEntity.isPresent()) return topicPreferenceEntity.get();
        else throw new Exception("TopicPreference não encontrado");
    }
}
