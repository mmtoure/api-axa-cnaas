package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.PartnerPricingDTO;
import sn.axa.apiaxacnaas.services.PartnerPricingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pricings")
public class PartnerPricingController {
    public final PartnerPricingService partnerPricingService;
    @PostMapping
    public ResponseEntity<PartnerPricingDTO> createPricing(@RequestBody PartnerPricingDTO request){
        PartnerPricingDTO partnerPricingDTO = partnerPricingService.createPartnerPricing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(partnerPricingDTO);
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<List<PartnerPricingDTO>> getAllPrincingsByPartner(@PathVariable Long partnerId){
        List<PartnerPricingDTO> pricingDTOList = partnerPricingService.getPricingByPartner(partnerId);
        return ResponseEntity.status(HttpStatus.OK).body(pricingDTOList);
    }


}
