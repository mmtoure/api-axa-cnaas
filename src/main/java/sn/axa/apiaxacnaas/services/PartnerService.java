package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.PartnerDTO;
import sn.axa.apiaxacnaas.dto.PartnerPricingDTO;
import sn.axa.apiaxacnaas.entities.Partner;
import sn.axa.apiaxacnaas.entities.PartnerPricing;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.PartnerMapper;
import sn.axa.apiaxacnaas.mappers.PricingPartnerMapper;
import sn.axa.apiaxacnaas.repositories.PartnerPricingRepository;
import sn.axa.apiaxacnaas.repositories.PartnerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final PartnerPricingRepository partnerPricingRepository;
    private final PricingPartnerMapper pricingPartnerMapper;

    public PartnerDTO createPartner( PartnerDTO partnerDTO){
        Partner partner = partnerMapper.toEntity(partnerDTO);
        Partner savedPartner = partnerRepository.save(partner);
        return partnerMapper.toDTO(savedPartner);
    }
    public Partner createPartnerIfNotExist(String code, String name){
        return partnerRepository.findByCode(code)
                .orElseGet(()->partnerRepository.save(
                        Partner.builder()
                                .name(name)
                                .code(code)
                                .email("contact@cnaas.sn")
                                .phoneNumber("775606060")
                                .build())
                );
    }
    public List<PartnerDTO> getAllPartners(){
        return partnerMapper.toDTOList(partnerRepository.findAll());
    }

    public PartnerDTO getPartnerById(Long partnerId){
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(()-> new ResourceNotFoundException("Partenaire not found"));
        return partnerMapper.toDTO(partner);
    }

}
