package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.UserDTO;
import sn.axa.apiaxacnaas.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User userEntity);
    User toEntity(UserDTO userDTO);
}
