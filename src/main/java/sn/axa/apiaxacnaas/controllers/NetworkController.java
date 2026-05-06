package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.axa.apiaxacnaas.dto.NetworkDTO;
import sn.axa.apiaxacnaas.dto.RegionDTO;
import sn.axa.apiaxacnaas.services.NetworkService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reseaux")
public class NetworkController {
    private final NetworkService networkService;

    @PostMapping
    public ResponseEntity<NetworkDTO> createZone(@RequestBody NetworkDTO zoneDTO) {
        NetworkDTO newZoneDTO = networkService.createNetwork(zoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newZoneDTO);
    }

    @GetMapping
    public ResponseEntity<List<NetworkDTO>> getAllZones() {
        List<NetworkDTO> listZones = networkService.getZones();
        return ResponseEntity.ok().body(listZones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkDTO> getZone(@PathVariable Long id) {
        NetworkDTO zone = networkService.getZone(id);
        return ResponseEntity.ok().body(zone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NetworkDTO> deleteZone(@PathVariable Long id) {
        networkService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NetworkDTO> updateZone(@PathVariable Long id, @RequestBody NetworkDTO zone) {
        if(id==null){
            throw new IllegalArgumentException("Zone Not Found");
        }
        NetworkDTO updatedZone = networkService.updateZone(zone, id);
        return ResponseEntity.ok().body(updatedZone);
    }

    // get networks not unassigned for user

    @GetMapping("/unassigned")
    public ResponseEntity<List<NetworkDTO>> getAvailableNetworks() {
        List<NetworkDTO> networks = networkService.getAvailableNetworks();
        return new ResponseEntity<>(networks, HttpStatus.OK);

    }

}
