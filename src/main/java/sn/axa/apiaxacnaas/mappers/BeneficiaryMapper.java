package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.BeneficiaryDTO;
import sn.axa.apiaxacnaas.entities.Beneficiary;


@Mapper(componentModel = "spring")
public interface BeneficiaryMapper {
   BeneficiaryDTO toDTO(Beneficiary entity);
    Beneficiary toEntity(BeneficiaryDTO dto);
}
