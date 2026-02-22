package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.AgenceDTO;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.mappers.AgenceMapper;
import sn.axa.apiaxacnaas.repositories.AgenceRepository;
import sn.axa.apiaxacnaas.util.VilleEnum;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgenceService {
    private final AgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    public AgenceDTO createAgence( AgenceDTO agenceDTO){
        Agence newAgence = agenceRepository.save(agenceMapper.toEntity(agenceDTO));
        return agenceMapper.toDTO(newAgence);
    }

    public Agence createAgenceIfNotExist(String name, VilleEnum ville){
        return agenceRepository.findByName(name).orElseGet(()->
                agenceRepository.save(Agence.builder()
                                .name(name)
                                .ville(VilleEnum.DAKAR)
                        .build())
        );
    }

    public AgenceDTO getAgenceById(Long id){
        Agence existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        return  agenceMapper.toDTO(existingAgence);
    }

    public List<AgenceDTO> getAllAgences(){
        List<Agence> listAgences = agenceRepository.findAll();
        return listAgences.stream().map(agenceMapper::toDTO).toList();
    }

    public AgenceDTO updateAgence(AgenceDTO agenceDTO, Long id){
        Agence existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        Agence saveAgence = agenceRepository.save(agenceMapper.toEntity(agenceDTO));
        return agenceMapper.toDTO(saveAgence);

    }

    public void deleteAgence(Long id){
        Agence existingAgence = agenceRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Agence not found"));
        agenceRepository.delete(existingAgence);
    }

}
