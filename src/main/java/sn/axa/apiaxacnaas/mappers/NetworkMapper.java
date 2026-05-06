package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.NetworkDTO;
import sn.axa.apiaxacnaas.entities.Network;

@Mapper(componentModel = "spring")
public interface NetworkMapper {
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "manager.firstName", target = "managerFirstName")
    @Mapping(source = "manager.lastName", target = "managerLastName")
    NetworkDTO toDTO(Network entity);
    Network toEntity(NetworkDTO entity);
}
