package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.RegionDTO;
import sn.axa.apiaxacnaas.entities.Agency;
import sn.axa.apiaxacnaas.entities.Region;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.RegionMapper;
import sn.axa.apiaxacnaas.repositories.AgencyRepository;
import sn.axa.apiaxacnaas.repositories.RegionRepository;
import sn.axa.apiaxacnaas.util.RegionEnum;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
 public  void createRegionIfNotExists(RegionEnum name) {
     if(regionRepository.findByName(name).isPresent()) {
         System.out.println("Region already exists");
         return;
     }
     Region regionEntity = new Region();
     regionEntity.setName(name);
     regionRepository.save(regionEntity);


 }
 //get all regions
 public List<RegionDTO> getRegions() {
     List<Region> regions = regionRepository.findAll();
     return regions.stream().map(regionMapper::toDTO).toList();

 }

 //get region by Id
    public RegionDTO getRegionById(long id) {
     Region region = regionRepository.findById(id)
             .orElseThrow(()-> new ResourceNotFoundException("Region not found"));
     return regionMapper.toDTO(region);
    }

 //get all regions unassigned for network
 public List<RegionDTO> getUnassignedRegions() {
     return regionRepository.findByNetworkIsNull()
             .stream()
             .map(regionMapper::toDTO)
             .toList();

 }

 // get available regions for user
public  List<RegionDTO> getAvailableRegions(Long networkId) {
 return regionRepository.findByNetworkIdAndUserIsNull(networkId)
         .stream()
         .map(regionMapper::toDTO)
         .toList();
}


}
