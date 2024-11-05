package getterson.insight.mappers;

import getterson.insight.dtos.UserDTO;
import getterson.insight.entities.UserEntity;
import getterson.insight.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper implements Mapper<UserEntity, UserDTO>{
    @Autowired
    private UserRepository userRepository;
    private final TopicMapper topicMapper = new TopicMapper();
    private final TopicPreferenceMapper topicPreferenceMapper = new TopicPreferenceMapper();

    public  UserDTO toDTO(UserEntity userEntity){
        return new UserDTO( userEntity.getName(),
                            userEntity.getUsername(),
                            userEntity.getDocument(),
                            topicPreferenceMapper.toDTO(userEntity.getTopicPreferenceList()),
                            topicMapper.toDTO(userEntity.getTopicList()));
    }

    public UserEntity toEntity(UserDTO userDTO){
        Optional<UserEntity> userEntity = userRepository.findByDocument(userDTO.document());
        if(userEntity.isPresent()) return userEntity.get();
        throw new EntityNotFoundException("Usuário não encontrado");
    }
}
