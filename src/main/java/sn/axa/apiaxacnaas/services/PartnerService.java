package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.PartnerDTO;
import sn.axa.apiaxacnaas.dto.PartnerPricingDTO;
import sn.axa.apiaxacnaas.entities.Partner;
import sn.axa.apiaxacnaas.entities.PartnerPricing;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.PartnerMapper;
import sn.axa.apiaxacnaas.mappers.PricingPartnerMapper;
import sn.axa.apiaxacnaas.repositories.PartnerPricingRepository;
import sn.axa.apiaxacnaas.repositories.PartnerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final PartnerPricingRepository partnerPricingRepository;
    private final PricingPartnerMapper pricingPartnerMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public PartnerDTO createPartner(PartnerDTO partnerDTO, MultipartFile logoPartner) throws IOException {
        Partner partner = partnerMapper.toEntity(partnerDTO);
        if(logoPartner != null){
            String fileName = partnerDTO.getCode()+"_"+logoPartner.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir, "partners");
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(logoPartner.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);
            partner.setLogoPartner("/uploads/partners/"+fileName);

        }
        partner.setActive(true);
        Partner savedPartner = partnerRepository.save(partner);
        return partnerMapper.toDTO(savedPartner);



    }

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

    public Long getCurrentUserPartnerId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return null;
        }
        return  userDetails.getPartnerId();
    }

}
