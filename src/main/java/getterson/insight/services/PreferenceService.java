package getterson.insight.services;

import getterson.insight.dtos.PreferenceDTO;
import getterson.insight.entities.PreferenceEntity;
import getterson.insight.exceptions.preference.InvalidPreferenceTypeException;
import getterson.insight.exceptions.preference.PreferenceNotFoundException;
import getterson.insight.mappers.PreferenceMapper;
import getterson.insight.repositories.PreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final PreferenceMapper preferenceMapper;

    @Autowired
    public PreferenceService(PreferenceRepository preferenceRepository, PreferenceMapper preferenceMapper) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceMapper = preferenceMapper;
    }

    public PreferenceDTO createPreference(PreferenceDTO preferenceDTO) throws InvalidPreferenceTypeException {
        validateType(preferenceDTO);
        PreferenceEntity preferenceEntity = preferenceMapper.preferenceDTOToPreferenceEntity(preferenceDTO);
        return preferenceMapper.preferenceToPreferenceDTO(preferenceRepository.save(preferenceEntity));
    }

    public List<PreferenceDTO> getAllPreferences() {
        return preferenceRepository.findAll().stream()
                .map(preferenceMapper::preferenceToPreferenceDTO)
                .collect(Collectors.toList());
    }

    public PreferenceDTO getPreferenceById(long id) throws PreferenceNotFoundException {
        return preferenceMapper.preferenceToPreferenceDTO(preferenceRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException("Preference not found with id: " + id)));
    }

    public PreferenceDTO updatePreference(long id, PreferenceDTO preferenceDTO) throws PreferenceNotFoundException, InvalidPreferenceTypeException {
        validateExistence(id);
        validateType(preferenceDTO);
        PreferenceEntity preferenceEntity = preferenceMapper.preferenceDTOToPreferenceEntity(preferenceDTO);
        preferenceEntity.setId(id);
        return preferenceMapper.preferenceToPreferenceDTO(preferenceRepository.save(preferenceEntity));
    }

    public void deletePreference(long id) throws PreferenceNotFoundException {
        validateExistence(id);
        preferenceRepository.deleteById(id);
    }

    private void validateExistence(long id) throws PreferenceNotFoundException {
        if (!preferenceRepository.existsById(id)) {
            throw new PreferenceNotFoundException("Preference not found with id: " + id);
        }
    }

    private void validateType(PreferenceDTO preferenceDTO) throws InvalidPreferenceTypeException {
        if (preferenceDTO.type() == null) {
            throw new InvalidPreferenceTypeException("Preference type is invalid or null.");
        }
    }
}
