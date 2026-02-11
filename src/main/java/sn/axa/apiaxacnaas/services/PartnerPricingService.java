package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.PartnerDTO;
import sn.axa.apiaxacnaas.dto.PartnerPricingDTO;
import sn.axa.apiaxacnaas.entities.Partner;
import sn.axa.apiaxacnaas.entities.PartnerPricing;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.PricingPartnerMapper;
import sn.axa.apiaxacnaas.repositories.PartnerPricingRepository;
import sn.axa.apiaxacnaas.repositories.PartnerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerPricingService {
    private final PartnerPricingRepository partnerPricingRepository;
    private final PartnerRepository partnerRepository;
    private final PricingPartnerMapper pricingPartnerMapper;

    public PartnerPricingDTO createPartnerPricing(PartnerPricingDTO request){
        Partner partner = partnerRepository.findById(request.getPartnerId())
                .orElseThrow(()->new ResourceNotFoundException("Partenaire not found"));
        PartnerPricing partnerPricing = pricingPartnerMapper.toEntity(request);
        partnerPricing.setMontantPrimeTTC(getMontantPrimeTTC(request));
        partnerPricing.setPartner(partner);
        PartnerPricing savedPartner = partnerPricingRepository.save(partnerPricing);
        return pricingPartnerMapper.toDTO(savedPartner);

    }
    public List<PartnerPricingDTO> getPricingByPartner(Long partnerId){
        List<PartnerPricing> listPricings = partnerPricingRepository.findAllByPartnerId(partnerId);
        return pricingPartnerMapper.toDTOList(listPricings);
    }

    public Double getMontantPrimeTTC(PartnerPricingDTO pricingDTO){
        return pricingDTO.getMontantPrime()+pricingDTO.getAccessoryCost()+pricingDTO.getTax();
    }



}
