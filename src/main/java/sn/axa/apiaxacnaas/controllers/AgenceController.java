package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.AgenceDTO;
import sn.axa.apiaxacnaas.services.AgenceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agences")

public class AgenceController {
    private final AgenceService agenceService;

    @PostMapping
    public ResponseEntity<AgenceDTO> createAgence(@RequestBody AgenceDTO agenceDTO){
        AgenceDTO newAgence = agenceService.createAgence(agenceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgence);
    }

    @GetMapping
    public  ResponseEntity<List<AgenceDTO>> getAllAgences(){
        List<AgenceDTO> allAgences = agenceService.getAllAgences();
        return ResponseEntity.status(HttpStatus.OK).body(allAgences);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<AgenceDTO> getAgenceById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agenceService.getAgenceById(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<AgenceDTO> updateAgence(@RequestBody AgenceDTO agenceDTO, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agenceService.updateAgence(agenceDTO,id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteAgence(@PathVariable Long id){
        agenceService.deleteAgence(id);
        return ResponseEntity.status(HttpStatus.OK).body("Agence supprimée avec succés");
    }


}