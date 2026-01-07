package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.AgenceDTO;
import sn.axa.apiaxacnaas.entities.Agence;

@Mapper(componentModel = "spring")
public interface AgenceMapper {
    AgenceDTO toDTO(Agence entity);
    Agence toEntity(AgenceDTO dto);
}
