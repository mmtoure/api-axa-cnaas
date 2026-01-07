package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.ContractGarantieDTO;
import sn.axa.apiaxacnaas.entities.ContractGarantie;

@Mapper(componentModel = "spring")
public interface ContractGarantieMapper {
    ContractGarantieDTO toDTO(ContractGarantie entity);
    ContractGarantie toEntity(ContractGarantieDTO dto);
}
