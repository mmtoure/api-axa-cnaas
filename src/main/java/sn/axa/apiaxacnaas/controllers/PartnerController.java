package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.dto.PartnerDTO;
import sn.axa.apiaxacnaas.services.PartnerService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partners")
public class PartnerController {
    private final PartnerService partnerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PartnerDTO> createPartner(@RequestPart("partner") PartnerDTO request, MultipartFile logoPartner) throws IOException {
        PartnerDTO response = partnerService.createPartner(request,logoPartner);
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
