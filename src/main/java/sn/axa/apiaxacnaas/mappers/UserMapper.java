package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.UserCreateDTO;
import sn.axa.apiaxacnaas.dto.UserDTO;
import sn.axa.apiaxacnaas.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "partner.name", target = "partnerName")
    UserDTO toDTO(User userEntity);
    User toEntity(UserCreateDTO userDTO);
}
