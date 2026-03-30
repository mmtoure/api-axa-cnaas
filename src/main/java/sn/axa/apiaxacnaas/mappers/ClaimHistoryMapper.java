package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ClaimHistoryDTO;
import sn.axa.apiaxacnaas.entities.ClaimHistory;

@Mapper(componentModel = "spring")
public interface ClaimHistoryMapper {
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "claim.id", target = "claimId")
    ClaimHistoryDTO toDTO(ClaimHistory entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "claim", ignore = true)
    ClaimHistory toEntity(ClaimHistoryDTO dto);
}

