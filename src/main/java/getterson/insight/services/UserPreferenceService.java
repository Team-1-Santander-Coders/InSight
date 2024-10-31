package getterson.insight.services;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.UserPreferenceEntity;
import getterson.insight.exceptions.preference.*;
import getterson.insight.mappers.UserPreferenceMapper;
import getterson.insight.repositories.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserPreferenceService {
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;

    @Autowired
    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceMapper = userPreferenceMapper;
    }

    public UserPreferenceEntity saveUserPreference(UserPreferenceEntity userPreferenceEntity) {
        return userPreferenceRepository.saveAndFlush(userPreferenceEntity);
    }

    public UserPreferenceDTO convertToDTO(UserPreferenceEntity userPreferenceEntity) {
        return userPreferenceMapper.toDTO(userPreferenceEntity);
    }
}
