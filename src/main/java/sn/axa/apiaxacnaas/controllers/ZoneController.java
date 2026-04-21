package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.AgenceDTO;
import sn.axa.apiaxacnaas.dto.ZoneDTO;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.services.ZoneService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/zones")
public class ZoneController {
    private final ZoneService zoneService;

    @PostMapping
    public ResponseEntity<ZoneDTO> createZone(@RequestBody ZoneDTO zoneDTO) {
        ZoneDTO newZoneDTO = zoneService.createZone(zoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newZoneDTO);
    }

    @GetMapping
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        List<ZoneDTO> listZones = zoneService.getZones();
        return ResponseEntity.ok().body(listZones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneDTO> getZone(@PathVariable Long id) {
        ZoneDTO zone = zoneService.getZone(id);
        return ResponseEntity.ok().body(zone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ZoneDTO> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneDTO> updateZone(@PathVariable Long id, @RequestBody ZoneDTO zone) {
        if(id==null){
            throw new IllegalArgumentException("Zone Not Found");
        }
        ZoneDTO updatedZone = zoneService.updateZone(zone, id);
        return ResponseEntity.ok().body(updatedZone);
    }


}
