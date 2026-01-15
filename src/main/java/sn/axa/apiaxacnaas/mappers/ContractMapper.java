package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.InsuredSimpleDTO;
import sn.axa.apiaxacnaas.entities.Contract;


@Mapper(
        componentModel = "spring",
        uses = {
                InsuredSimpleMapper.class,
                ContractGarantieMapper.class
        }
)
public interface ContractMapper {
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    ContractDTO toDTO(Contract entity);

    Contract toEntity(ContractDTO dto);

}
