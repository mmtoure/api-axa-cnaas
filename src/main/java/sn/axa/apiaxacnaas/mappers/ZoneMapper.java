package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ZoneDTO;
import sn.axa.apiaxacnaas.entities.Zone;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    @Mapping(source = "chefZone.id", target = "chefZoneId")
    ZoneDTO toDTO(Zone entity);
    Zone toEntity(ZoneDTO entity);
}
