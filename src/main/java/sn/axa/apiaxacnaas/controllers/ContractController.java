package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.entities.Insured;
import sn.axa.apiaxacnaas.services.ContractPdfService;
import sn.axa.apiaxacnaas.services.ContractService;
import sn.axa.apiaxacnaas.services.InsuredService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contracts")
public class ContractController {
    private final ContractService contractService;
    private final ContractPdfService contractPdfService;

    @GetMapping("all")
    public ResponseEntity<List<ContractDTO>> getAllContracts(){
        List<ContractDTO> allContracts = contractService.getAllContracts();
        return ResponseEntity.status(HttpStatus.OK).body(allContracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContractById(@PathVariable Long id){
        ContractDTO contractDTO = contractService.getContractById(id);
        return ResponseEntity.status(HttpStatus.OK).body(contractDTO);
    }


    @GetMapping
    public ResponseEntity<Page<ContractDTO>> getContracts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contractService.getContracts(page, size));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateContract(@PathVariable Long id) throws Exception {
        ContractDTO contract = contractService.getContractById(id);
        Long insuredId = contract.getInsuredId();
        byte[] pdf = contractPdfService.generatePdfForInsured(insuredId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=contract_" + contract.getPoliceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
