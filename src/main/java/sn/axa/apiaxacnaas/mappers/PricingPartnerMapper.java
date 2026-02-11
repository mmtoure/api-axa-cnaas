package sn.axa.apiaxacnaas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sn.axa.apiaxacnaas.dto.PartnerPricingDTO;
import sn.axa.apiaxacnaas.entities.PartnerPricing;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PricingPartnerMapper {
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "partner.name", target = "partnerName")
    PartnerPricingDTO toDTO(PartnerPricing entity);
    PartnerPricing toEntity(PartnerPricingDTO dto);
    List<PartnerPricingDTO> toDTOList(List<PartnerPricing> entities);
}
