package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.entities.Insured;

@Mapper(componentModel = "spring", uses = {
        BeneficiaryMapper.class
})
public interface InsuredMapper {
    @Mapping(source = "group.id", target = "group_id")
    InsuredDTO toDTO(Insured entity);
    Insured toEntity(InsuredDTO dto);
}
