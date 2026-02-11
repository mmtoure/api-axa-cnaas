package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.PartnerDTO;
import sn.axa.apiaxacnaas.services.PartnerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partners")
public class PartnerController {
    private final PartnerService partnerService;

    @PostMapping
    public ResponseEntity<PartnerDTO> createPartner(@RequestBody PartnerDTO request){
        PartnerDTO response = partnerService.createPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getAllPartners(){
        List<PartnerDTO> allPartners = partnerService.getAllPartners();
        return ResponseEntity.status(HttpStatus.OK).body(allPartners);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PartnerDTO> getPartner(@PathVariable Long id){
        PartnerDTO partnerDTO = partnerService.getPartnerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(partnerDTO);
    }

}
