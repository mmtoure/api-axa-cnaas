package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.services.InsuredService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/insureds")

public class InsuredController {
    private final InsuredService insuredService;

    @PostMapping
    public ResponseEntity<InsuredDTO> createInsured(@RequestBody InsuredDTO insuredDTODTO){
        InsuredDTO newInsured = insuredService.createInsured(insuredDTODTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newInsured);
    }

    @GetMapping
    public  ResponseEntity<List<InsuredDTO>> getAllInsureds(){
        List<InsuredDTO> allInsureds = insuredService.getAllInsureds();
        return ResponseEntity.status(HttpStatus.OK).body(allInsureds);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<InsuredDTO> getInsuredById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(insuredService.getInsuredById(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<InsuredDTO> updateInsured(@RequestBody InsuredDTO insuredDTODTO, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(insuredService.updateInsured(insuredDTODTO,id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteInsured(@PathVariable Long id){
        insuredService.deleteInsured(id);
        return ResponseEntity.status(HttpStatus.OK).body("Insured supprimée avec succés");
    }


}