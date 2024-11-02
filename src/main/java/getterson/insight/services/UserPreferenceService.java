package getterson.insight.services;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.*;
import getterson.insight.mappers.UserPreferenceMapper;
import getterson.insight.repositories.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserPreferenceService {
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserPreferenceMapper userPreferenceMapper;

    public UserPreferenceEntity createUserPreferenceEntity(UserEntity user) {
        UserPreferenceEntity entity = new UserPreferenceEntity(user);
        return userPreferenceRepository.saveAndFlush(entity);
    }

    public UserPreferenceDTO updateUserPreference(UserPreferenceDTO userPreferenceDTO) {
        UserPreferenceEntity userPreference = userPreferenceMapper.toEntity(userPreferenceDTO);
        userPreference = userPreferenceRepository.saveAndFlush(userPreference);

        return userPreferenceMapper.toDTO(userPreference);
    }
}
