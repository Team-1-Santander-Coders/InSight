package getterson.insight.mappers;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.UserPreferenceEntity;
import getterson.insight.repositories.UserPreferenceRepository;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceMapper implements Mapper<UserPreferenceEntity, UserPreferenceDTO> {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceMapper(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    public UserPreferenceDTO toDTO(UserPreferenceEntity userPreferenceEntity){
        return new UserPreferenceDTO(userPreferenceEntity.getId(),
                userPreferenceEntity.getUser().getId(),
                userPreferenceEntity.getBlackListWords(),
                userPreferenceEntity.getType());
    }

    public UserPreferenceEntity toEntity(UserPreferenceDTO userPreferenceDTO) {
        return userPreferenceRepository.findById(userPreferenceDTO.id()).get();
    }
}
