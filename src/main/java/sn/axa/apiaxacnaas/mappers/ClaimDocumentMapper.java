package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.ClaimDocumentDTO;
import sn.axa.apiaxacnaas.entities.ClaimDocument;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClaimDocumentMapper {

    @Mapping(source = "claim.id", target = "claimId")
    ClaimDocumentDTO toDTO(ClaimDocument entity);
    @Mapping(source = "claimId", target = "claim.id")
    ClaimDocument toEntity(ClaimDocumentDTO dto);

    List<ClaimDocumentDTO> toDTOList(List<ClaimDocument> entities);

}
