package getterson.insight.mappers;

import getterson.insight.dtos.UserDTO;
import getterson.insight.entities.UserEntity;
import getterson.insight.repositories.UserRepository;

import org.springframework.stereotype.Component;


@Component
public class UserMapper implements Mapper<UserEntity, UserDTO>{

    private final UserRepository userRepository;
    private final TopicMapper topicMapper;
    private final TopicPreferenceMapper topicPreferenceMapper;

    public UserMapper(UserRepository userRepository, TopicMapper topicMapper, TopicPreferenceMapper topicPreferenceMapper) {
        this.userRepository = userRepository;
        this.topicMapper = topicMapper;
        this.topicPreferenceMapper = topicPreferenceMapper;
    }

    public UserDTO toDTO(UserEntity userEntity){
        return new UserDTO(userEntity.getName(),
                userEntity.getUsername(),
                userEntity.getDocument(),
                topicPreferenceMapper.toDTO(userEntity.getTopicPreferenceList()),
                topicMapper.toDTO(userEntity.getTopicList()));
    }

    public UserEntity toEntity(UserDTO userDTO) {
        return userRepository.findByDocument(userDTO.document()).get();
    }
}
