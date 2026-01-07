package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.entities.Contract;


@Mapper(componentModel = "spring")
public interface ContractMapper {
    ContractDTO toDTO(Contract entity);
    Contract toEntity(ContractDTO dto);

}
