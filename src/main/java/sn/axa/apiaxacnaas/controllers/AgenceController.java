package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.AgencyDTO;
import sn.axa.apiaxacnaas.services.AgenceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agences")

public class AgenceController {
    private final AgenceService agenceService;

    @PostMapping
    public ResponseEntity<AgencyDTO> createAgence(@RequestBody AgencyDTO agenceDTO){
        AgencyDTO newAgence = agenceService.createAgence(agenceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgence);
    }

    @GetMapping
    public  ResponseEntity<List<AgencyDTO>> getAllAgences(){
        List<AgencyDTO> allAgences = agenceService.getAllAgences();
        return ResponseEntity.status(HttpStatus.OK).body(allAgences);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<AgencyDTO> getAgenceById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agenceService.getAgenceById(id));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<AgencyDTO> updateAgence(@RequestBody AgencyDTO agenceDTO, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(agenceService.updateAgence(agenceDTO,id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteAgence(@PathVariable Long id){
        agenceService.deleteAgence(id);
        return ResponseEntity.status(HttpStatus.OK).body("Agence supprimée avec succés");
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<AgencyDTO>> getAllAgencesByZoneId(@PathVariable Long zoneId) {
        List<AgencyDTO> listAgences = agenceService.getAgencesByZoneId(zoneId);
        return ResponseEntity.status(HttpStatus.OK).body(listAgences);
    }


}