package getterson.insight.mappers;

import getterson.insight.dtos.UserDTO;
import getterson.insight.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(UserEntity user);
    UserEntity UserDTOToUserEntity(UserDTO userDTO);
}
