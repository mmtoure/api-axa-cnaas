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
    @Mapping(source = "insured.id", target = "insuredId")
    @Mapping(source = "insured.firstName", target = "firstName")
    @Mapping(source = "insured.lastName", target = "lastName")
    ContractDTO toDTO(Contract entity);

    Contract toEntity(ContractDTO dto);

}
