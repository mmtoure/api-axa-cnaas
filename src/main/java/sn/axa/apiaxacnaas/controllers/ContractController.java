package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.services.ContractService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contracts")
public class ContractController {
    private final ContractService contractService;

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAllContracts(){
        List<ContractDTO> allContracts = contractService.getAllContracts();
        return ResponseEntity.status(HttpStatus.OK).body(allContracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContractById(@PathVariable Long id){
        ContractDTO contractDTO = contractService.getContractById(id);
        return ResponseEntity.status(HttpStatus.OK).body(contractDTO);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateContractPdf(@PathVariable Long id) throws IOException {
        byte[] pdf = contractService.generateContractPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contrat-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
