package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.AgencyDTO;
import sn.axa.apiaxacnaas.dto.RegionDTO;
import sn.axa.apiaxacnaas.entities.Agency;
import sn.axa.apiaxacnaas.entities.Region;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    @Mapping(source = "network.id", target = "networkId")
    @Mapping(source = "network.name", target = "networkName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    RegionDTO toDTO(Region entity);

    @Mapping(target = "network", ignore = true)
    @Mapping(target = "user", ignore = true)
    Region toEntity(RegionDTO dto);
}
