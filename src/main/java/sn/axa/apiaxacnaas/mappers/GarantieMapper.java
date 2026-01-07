package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sn.axa.apiaxacnaas.dto.GarantieDTO;
import sn.axa.apiaxacnaas.entities.Garantie;

@Mapper(componentModel = "spring")
public interface GarantieMapper {
    GarantieDTO toDTO(Garantie entity);
    Garantie toEntity(GarantieDTO dto);
}
