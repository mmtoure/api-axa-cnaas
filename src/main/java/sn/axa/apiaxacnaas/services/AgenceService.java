package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.AgencyDTO;
import sn.axa.apiaxacnaas.entities.Agency;
import sn.axa.apiaxacnaas.entities.Network;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.AgenceMapper;
import sn.axa.apiaxacnaas.repositories.AgencyRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.repositories.NetworkRepository;
import sn.axa.apiaxacnaas.util.RoleEnum;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceService {
    private final AgencyRepository agenceRepository;
    private final NetworkRepository zoneRepository;
    private final AgenceMapper agenceMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public AgencyDTO createAgence(AgencyDTO agenceDTO){
        User currentUser = userService.getCurrentUser();
        Network zone = zoneRepository.findById(agenceDTO.getNetworkId())
                .orElseThrow(()-> new IllegalArgumentException("Zone not found"));

        User chefAgence = userRepository.findById(agenceDTO.getChefAgencyId())
                .orElseThrow(()-> new ResourceNotFoundException("Chef Chef Agence Not Found"));

        Agency agence = new Agency();
        agence.setName(agenceDTO.getName());
        agence.setPartner(currentUser.getPartner());
        agence.setNetwork(zone);
        agence.setChefAgency(chefAgence);
        chefAgence.addAgence(agence);
        chefAgence.setNetwork(zone);
        agence.setCreatedBy(currentUser);
        Agency agenceCreated = agenceRepository.save(agence);
        return agenceMapper.toDTO(agenceCreated);
    }



    public AgencyDTO getAgenceById(Long id){
        Agency existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        return  agenceMapper.toDTO(existingAgence);
    }

    public List<AgencyDTO> getAllAgences(){
        User currentUser = userService.getCurrentUser();
        List<Agency> listAgences = new ArrayList<>();
        if(currentUser.getRole().getName().equals(RoleEnum.MANAGER)){
            listAgences = agenceRepository.findByNetworkId(currentUser.getNetwork().getId());
        }
        else{
            listAgences = agenceRepository.findAll();
        }
        return listAgences.stream().map(agenceMapper::toDTO).toList();
    }

    public AgencyDTO updateAgence(AgencyDTO dto, Long id){
        Agency updateAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        Network zone = zoneRepository.findById(dto.getNetworkId())
                .orElseThrow(()->new ResourceNotFoundException("Zone not found"));

        User chefAgence = userRepository.findById(dto.getChefAgencyId())
                .orElseThrow(()->new ResourceNotFoundException("Chef Agence Not Found"));
        updateAgence.setName(dto.getName());
        updateAgence.setNetwork(zone);
        updateAgence.setChefAgency(chefAgence);
        chefAgence.addAgence(updateAgence);
        chefAgence.setNetwork(zone);
        updateAgence = agenceRepository.save(updateAgence);
        Agency saveAgence = agenceRepository.save(updateAgence);
        return agenceMapper.toDTO(saveAgence);

    }

    public void deleteAgence(Long id){
        Agency existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        User chefAgence = existingAgence.getChefAgency();
        existingAgence.setChefAgency(null);
        existingAgence = agenceRepository.save(existingAgence);
        agenceRepository.delete(existingAgence);
    }

    public List<AgencyDTO> getAgencesByZoneId(Long id){
        List<Agency> listAgences = agenceRepository.findByNetworkId(id);
        System.out.println(listAgences);
        return listAgences.stream().map(agenceMapper::toDTO).toList();
    }

}
