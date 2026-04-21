package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.AgenceDTO;
import sn.axa.apiaxacnaas.entities.Agence;

@Mapper(componentModel = "spring")
public interface AgenceMapper {
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "chefAgence.id", target = "chefAgenceId")
    AgenceDTO toDTO(Agence entity);
    Agence toEntity(AgenceDTO dto);
}
