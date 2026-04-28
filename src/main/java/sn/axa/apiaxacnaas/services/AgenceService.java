package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.AgenceDTO;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.entities.Zone;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.AgenceMapper;
import sn.axa.apiaxacnaas.repositories.AgenceRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.repositories.ZoneRepository;
import sn.axa.apiaxacnaas.util.RoleEnum;
import sn.axa.apiaxacnaas.util.VilleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgenceService {
    private final AgenceRepository agenceRepository;
    private final ZoneRepository zoneRepository;
    private final AgenceMapper agenceMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public AgenceDTO createAgence( AgenceDTO agenceDTO){
        User currentUser = userService.getCurrentUser();
        Zone zone = zoneRepository.findById(agenceDTO.getZoneId())
                .orElseThrow(()-> new IllegalArgumentException("Zone not found"));

        User chefAgence = userRepository.findById(agenceDTO.getChefAgenceId())
                .orElseThrow(()-> new ResourceNotFoundException("Chef Chef Agence Not Found"));

        Agence agence = new Agence();
        agence.setName(agenceDTO.getName());
        agence.setPartner(currentUser.getPartner());
        agence.setZone(zone);
        agence.setChefAgence(chefAgence);
        chefAgence.addAgence(agence);
        chefAgence.setZone(zone);
        agence.setCreatedBy(currentUser);
        Agence agenceCreated = agenceRepository.save(agence);
        return agenceMapper.toDTO(agenceCreated);
    }



    public AgenceDTO getAgenceById(Long id){
        Agence existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        return  agenceMapper.toDTO(existingAgence);
    }

    public List<AgenceDTO> getAllAgences(){
        User currentUser = userService.getCurrentUser();
        List<Agence> listAgences = new ArrayList<>();
        if(currentUser.getRole().getName().equals(RoleEnum.MANAGER)){
            listAgences = agenceRepository.findByZoneId(currentUser.getZone().getId());
        }
        else{
            listAgences = agenceRepository.findAll();
        }
        return listAgences.stream().map(agenceMapper::toDTO).toList();
    }

    public AgenceDTO updateAgence(AgenceDTO dto, Long id){
        Agence updateAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        Zone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(()->new ResourceNotFoundException("Zone not found"));

        User chefAgence = userRepository.findById(dto.getChefAgenceId())
                .orElseThrow(()->new ResourceNotFoundException("Chef Agence Not Found"));
        updateAgence.setName(dto.getName());
        updateAgence.setZone(zone);
        updateAgence.setChefAgence(chefAgence);
        chefAgence.addAgence(updateAgence);
        chefAgence.setZone(zone);
        updateAgence = agenceRepository.save(updateAgence);
        Agence saveAgence = agenceRepository.save(updateAgence);
        return agenceMapper.toDTO(saveAgence);

    }

    public void deleteAgence(Long id){
        Agence existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        User chefAgence = existingAgence.getChefAgence();
        existingAgence.setChefAgence(null);
        existingAgence = agenceRepository.save(existingAgence);
        agenceRepository.delete(existingAgence);
    }

    public List<AgenceDTO> getAgencesByZoneId(Long id){
        List<Agence> listAgences = agenceRepository.findByZoneId(id);
        System.out.println(listAgences);
        return listAgences.stream().map(agenceMapper::toDTO).toList();
    }

}
