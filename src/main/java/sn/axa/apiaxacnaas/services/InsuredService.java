package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.Beneficiary;
import sn.axa.apiaxacnaas.entities.Insured;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.mappers.InsuredMapper;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuredService {
    private static final Logger log = LoggerFactory.getLogger(InsuredService.class);
    private final InsuredMapper insuredMapper;
    private final InsuredRepository insuredRepository;
    private final UserService userService;
    private final ContractService contractService;

    public InsuredDTO createInsured(InsuredDTO insuredDTO){
        User currentUser = userService.getCurrentUser();

        Insured newInsured = insuredMapper.toEntity(insuredDTO);
        newInsured.setUser(currentUser);

        Insured savedInsured = insuredRepository.save(newInsured);
        if(newInsured.getBeneficiary()!=null){
            newInsured.getBeneficiary().setInsured(newInsured);
        }
        contractService.createContract(null, savedInsured);
        return insuredMapper.toDTO(savedInsured);
    }

    public InsuredDTO getInsuredById(Long id){
        Insured existingInsured = insuredRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Insured not found"));
        return  insuredMapper.toDTO(existingInsured);
    }

    public List<InsuredDTO> getAllInsureds(){
        List<Insured> listInsureds = insuredRepository.findAll();
        return listInsureds.stream().map(insuredMapper::toDTO).toList();
    }

    public InsuredDTO updateInsured(InsuredDTO insuredDTO, Long id){
        Insured existingInsured = insuredRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Insured not found"));
        Insured saveInsured = insuredRepository.save(insuredMapper.toEntity(insuredDTO));
        return insuredMapper.toDTO(saveInsured);

    }

    public void deleteInsured(Long id){
        Insured existingInsured = insuredRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Insured not found"));
        insuredRepository.delete(existingInsured);
    }


}
