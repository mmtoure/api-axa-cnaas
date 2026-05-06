package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.AgencyDTO;
import sn.axa.apiaxacnaas.entities.Agency;

@Mapper(componentModel = "spring")
public interface AgenceMapper {
    @Mapping(source = "network.id", target = "networkId")
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "chefAgency.id", target = "chefAgencyId")
    AgencyDTO toDTO(Agency entity);
    Agency toEntity(AgencyDTO dto);
}
