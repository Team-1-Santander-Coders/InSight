package getterson.insight.mappers;

import getterson.insight.dtos.UserPreferenceDTO;
import getterson.insight.entities.UserEntity;
import getterson.insight.entities.UserPreferenceEntity;
import getterson.insight.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserPreferenceMapper {
    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "user", expression = "java(mapUserIdToUser(userPreferenceDTO.userId()))")
    public abstract UserPreferenceEntity toEntity(UserPreferenceDTO userPreferenceDTO);

    @Mapping(source = "user.id", target = "userId")
    public abstract UserPreferenceDTO toDTO(UserPreferenceEntity userPreferenceEntity);

    protected UserEntity mapUseIdToUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o ID: " + id));
    }
}
