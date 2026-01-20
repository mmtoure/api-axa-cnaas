package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ContractGarantieDTO;
import sn.axa.apiaxacnaas.entities.ContractGarantie;

@Mapper(componentModel = "spring")
public interface ContractGarantieMapper {
    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "garantie.id", target = "garantieId")
    @Mapping(source = "garantie.name", target = "garantieName")
    ContractGarantieDTO toDTO(ContractGarantie entity);

    ContractGarantie toEntity(ContractGarantieDTO dto);
}
