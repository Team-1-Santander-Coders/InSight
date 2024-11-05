package getterson.insight.mappers;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.UserEntity;
import getterson.insight.entities.UserPreferenceEntity;
import getterson.insight.repositories.UserPreferenceRepository;
import getterson.insight.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPreferenceMapper implements Mapper<UserPreferenceEntity, UserPreferenceDTO> {
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    public  UserPreferenceDTO toDTO(UserPreferenceEntity userPreferenceEntity){
        return new UserPreferenceDTO(userPreferenceEntity.getId(),
                                     userPreferenceEntity.getUser().getId(),
                                     userPreferenceEntity.getBlackListWords(),
                                     userPreferenceEntity.getType());
    }

    public  UserPreferenceEntity toEntity(UserPreferenceDTO userPreferenceDTO) throws Exception {
        Optional<UserPreferenceEntity> userPreferenceEntity = userPreferenceRepository.findById(userPreferenceDTO.id());
        if(userPreferenceEntity.isPresent()) return userPreferenceEntity.get();
        else throw new Exception("UserPreference não encontrada");
    }




}
