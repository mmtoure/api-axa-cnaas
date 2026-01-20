package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.entities.Claim;

@Mapper(componentModel = "spring")
public interface ClaimMapper {
    ClaimDTO toDTO(Claim entity);
    Claim toEntity(ClaimDTO dto);

}
