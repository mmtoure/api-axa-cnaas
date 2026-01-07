package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.entities.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDTO toDTO(Group entity);
    Group toEntity(GroupDTO dto);
}
