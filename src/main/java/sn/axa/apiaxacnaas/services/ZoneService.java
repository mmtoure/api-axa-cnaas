package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ZoneDTO;
import sn.axa.apiaxacnaas.entities.Partner;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.entities.Zone;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ZoneMapper;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.repositories.ZoneRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public ZoneDTO createZone(ZoneDTO dto) {
        User chefZone = userRepository.findById(dto.getChefZoneId())
                .orElseThrow(()-> new ResourceNotFoundException("Chef Zone Not Found"));

        User currentUser = userService.getCurrentUser();
        Zone zone = new Zone();
        zone.setName(dto.getName());
        zone.setPartner(currentUser.getPartner());
        zone.setCreatedBy(currentUser);
        zone.setChefZone(chefZone);
        chefZone.setZone(zone);

        return zoneMapper.toDTO(zoneRepository.save(zone));
    }


    public void deleteZone(Long id) {
        zoneRepository.deleteById(id);
    }
    public ZoneDTO getZone(Long id) {
        Zone zone = zoneRepository.findById(id).orElse(null);
        return zoneMapper.toDTO(zone);
    }

    public List<ZoneDTO> getZones() {
        List<Zone> zones = zoneRepository.findAll();
        return zones.stream().map(zoneMapper::toDTO).toList();
    }

    // update Zone
    public ZoneDTO updateZone(ZoneDTO dto, Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone Not Found"));
        User chefZone = userRepository.findById(dto.getChefZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Chef Zone Not Found"));
            zone.setName(dto.getName());
            zone.setChefZone(chefZone);
            chefZone.setZone(zone);
            Zone updatedZone = zoneRepository.save(zone);
            return zoneMapper.toDTO(updatedZone);
    }

}
