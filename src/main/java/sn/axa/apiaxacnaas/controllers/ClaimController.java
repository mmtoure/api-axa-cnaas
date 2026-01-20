package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.repositories.ClaimRepository;
import sn.axa.apiaxacnaas.services.ClaimService;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/claims")
public class ClaimController {
    private final ClaimService claimService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ClaimDTO> createClaim(@RequestPart("claim") ClaimDTO request,
                                                @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                @RequestParam(value = "documentTypes", required = false) List<ClaimDocumentType> documentTypes){
        ClaimDTO claimDTO = claimService.createClaim(request,files,documentTypes);
        return ResponseEntity.status(HttpStatus.CREATED).body(claimDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> getClaimById(@PathVariable Long id){
        ClaimDTO claimDTO = claimService.getClaimById(id);
        return ResponseEntity.status(HttpStatus.OK).body(claimDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClaimDTO>> getAllClaims(){
        List<ClaimDTO> claimDTOList = claimService.getAllClaims();
        return ResponseEntity.status(HttpStatus.OK).body(claimDTOList);
    }




}
