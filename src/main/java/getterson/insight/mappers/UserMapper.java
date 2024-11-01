package getterson.insight.mappers;

import getterson.insight.dtos.UserDTO;
import getterson.insight.entities.UserEntity;
import getterson.insight.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "user", expression = "java(mapUserIdToUser(userDTO.id()))")
    public abstract UserEntity UserDTOToUserEntity(UserDTO userDTO);
    public abstract UserDTO userToUserDTO(UserEntity user);

    protected UserEntity mapUseIdToUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o ID: " + id));
    }
}
