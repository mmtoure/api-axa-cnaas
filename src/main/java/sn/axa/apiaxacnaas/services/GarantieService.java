package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.GarantieDTO;
import sn.axa.apiaxacnaas.entities.Garantie;
import sn.axa.apiaxacnaas.mappers.GarantieMapper;
import sn.axa.apiaxacnaas.repositories.GarantieRepository;

@Service
@RequiredArgsConstructor
public class GarantieService {
    private final GarantieRepository garantieRepository;
    private final GarantieMapper garantieMapper;

    public GarantieDTO createGarantie(GarantieDTO garantieDTO){
        Garantie garantie = garantieMapper.toEntity(garantieDTO);
        Garantie savedGarantie= garantieRepository.save(garantie);
        return  garantieMapper.toDTO(savedGarantie);
    }
}
