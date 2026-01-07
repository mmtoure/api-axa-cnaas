package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.SinistreDocumentDTO;
import sn.axa.apiaxacnaas.entities.SinistreDocument;

@Mapper(componentModel = "spring")
public interface SinistreDocumentMapper {
    SinistreDocumentDTO toDTO(SinistreDocument entity);
    SinistreDocument toEntity(SinistreDocumentDTO dto);

}
