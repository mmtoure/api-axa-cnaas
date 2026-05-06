package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.NetworkDTO;
import sn.axa.apiaxacnaas.dto.RegionDTO;
import sn.axa.apiaxacnaas.entities.Region;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.entities.Network;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.NetworkMapper;
import sn.axa.apiaxacnaas.repositories.RegionRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.repositories.NetworkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NetworkService {
    private final NetworkRepository networkRepository;
    private final NetworkMapper networkMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    public NetworkDTO createNetwork(NetworkDTO dto) {
        User currentUser = userService.getCurrentUser();
        Network network = new Network();
        network.setName(dto.getName());
        if(dto.getManagerId()!=null){
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(()->new ResourceNotFoundException("Manager not found"));
            network.setManager(manager);
        }
        network.setPartner(currentUser.getPartner());
        network.setCreatedBy(currentUser);
        Network savedNetwork = networkRepository.save(network);
        List<Region> regions = regionRepository.findAllById(dto.getRegionIds()).stream().toList();
        regions.forEach(region -> {
            if (region.getNetwork() != null) {
                throw new ResourceNotFoundException(
                        "La région " + region.getName() + " est déjà affectée à un réseau"
                );
            }

            region.setNetwork(savedNetwork);

        });
        regionRepository.saveAll(regions);
        savedNetwork.setRegions(regions);
        return networkMapper.toDTO(savedNetwork);
    }


    public void deleteZone(Long id) {
        networkRepository.deleteById(id);
    }
    public NetworkDTO getZone(Long id) {
        Network zone = networkRepository.findById(id).orElse(null);
        return networkMapper.toDTO(zone);
    }

    public List<NetworkDTO> getZones() {
        List<Network> zones = networkRepository.findAll();
        return zones.stream().map(networkMapper::toDTO).toList();
    }

    // update Zone
    public NetworkDTO updateZone(NetworkDTO dto, Long id) {
        Network network = networkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Network Not Found"));
        network.setName(dto.getName());
        Network updatedZone = networkRepository.save(network);
        return networkMapper.toDTO(updatedZone);
    }

    // get available regions for user
    public  List<NetworkDTO> getAvailableNetworks() {
        return networkRepository.findByManagerIsNull()
                .stream()
                .map(networkMapper::toDTO)
                .toList();
    }

}
