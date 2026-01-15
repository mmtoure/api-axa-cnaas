package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.InsuredSimpleDTO;
import sn.axa.apiaxacnaas.entities.Insured;

@Mapper(componentModel = "spring")
public interface InsuredSimpleMapper {
    InsuredSimpleDTO toDTO(Insured insured);
}
