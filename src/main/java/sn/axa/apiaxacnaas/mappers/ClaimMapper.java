package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.entities.Claim;

@Mapper(componentModel = "spring")
public interface ClaimMapper {
    @Mapping(source = "insured.id", target = "insuredId")
    @Mapping(source = "insured.firstName", target = "firstName")
    @Mapping(source = "insured.lastName", target = "lastName")
    ClaimDTO toDTO(Claim entity);
    Claim toEntity(ClaimDTO dto);

}
