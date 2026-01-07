package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.SinistreDTO;
import sn.axa.apiaxacnaas.entities.Sinistre;

@Mapper(componentModel = "spring")
public interface SinistreMapper {
    SinistreDTO toDTO(Sinistre entity);
    Sinistre toEntity(SinistreDTO dto);

}
