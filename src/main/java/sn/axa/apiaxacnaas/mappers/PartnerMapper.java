package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import sn.axa.apiaxacnaas.dto.PartnerDTO;
import sn.axa.apiaxacnaas.entities.Partner;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartnerMapper {
    PartnerDTO toDTO(Partner entity);
    Partner toEntity(PartnerDTO dto);
    List<PartnerDTO> toDTOList(List<Partner> entities);
}
