package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.dto.InsuredSimpleDTO;
import sn.axa.apiaxacnaas.entities.Insured;

@Mapper(componentModel = "spring", uses = {
        BeneficiaryMapper.class
})
public interface InsuredMapper {
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "region.id", target = "regionId")
    @Mapping(source = "region.name", target = "regionName")
    InsuredDTO toDTO(Insured entity);
    InsuredSimpleDTO toSimpleDTO(Insured entity);
    Insured toEntity(InsuredDTO dto);
}
