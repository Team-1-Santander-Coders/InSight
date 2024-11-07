package getterson.insight.services;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.*;
import getterson.insight.mappers.UserPreferenceMapper;
import getterson.insight.repositories.UserPreferenceRepository;
import org.springframework.stereotype.Service;


@Service
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;

    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceMapper = userPreferenceMapper;
    }

    public UserPreferenceEntity createUserPreferenceEntity(UserEntity user) {
        UserPreferenceEntity entity = new UserPreferenceEntity(user);
        return userPreferenceRepository.saveAndFlush(entity);
    }

    public UserPreferenceDTO updateUserPreference(UserPreferenceDTO userPreferenceDTO) throws Exception {
        UserPreferenceEntity userPreference = userPreferenceMapper.toEntity(userPreferenceDTO);
        userPreference = userPreferenceRepository.saveAndFlush(userPreference);

        return userPreferenceMapper.toDTO(userPreference);
    }
}
