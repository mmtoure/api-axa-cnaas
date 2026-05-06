package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.axa.apiaxacnaas.dto.RegionDTO;
import sn.axa.apiaxacnaas.services.RegionService;

import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;


    //get all regions
    @GetMapping
    public ResponseEntity<List<RegionDTO>> getRegions() {
        List<RegionDTO> regions = regionService.getRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    // get region by Id
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        RegionDTO regionDTO = regionService.getRegionById(id);
        return new ResponseEntity<>(regionDTO, HttpStatus.OK);

    }
    @GetMapping("/unassigned")
    public ResponseEntity<List<RegionDTO>> getUnassignedRegions() {
        List<RegionDTO> regions = regionService.getUnassignedRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);

    }
    //get available regions for network

    @GetMapping("/{networkId}/available")
    public ResponseEntity<List<RegionDTO>> getAvailableRegions(@PathVariable Long networkId) {
        System.out.println("networkId: " + networkId);
        List<RegionDTO> regions = regionService.getAvailableRegions(networkId);
        return new ResponseEntity<>(regions, HttpStatus.OK);

    }

}
