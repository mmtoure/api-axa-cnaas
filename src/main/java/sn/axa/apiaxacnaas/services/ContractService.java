package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.mappers.ContractMapper;
import sn.axa.apiaxacnaas.mappers.GarantieMapper;
import sn.axa.apiaxacnaas.repositories.ContractRepository;
import sn.axa.apiaxacnaas.repositories.GarantieRepository;
import sn.axa.apiaxacnaas.repositories.GroupRepository;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.GlobalConstants;
import sn.axa.apiaxacnaas.util.StatusContract;
import sn.axa.apiaxacnaas.util.TypeContractEnum;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final GarantieRepository garantieRepository;
    private final GarantieMapper garantieMapper;
    private final GroupRepository groupRepository;
    private final InsuredRepository insuredRepository;

    public ContractDTO createContract(Insured insured){
        Contract contract = new Contract();
        contract.setPoliceNumber(generatePoliceContract());
        contract.setMontantPrime(GlobalConstants.MONTANT_PRIME);
        contract.setStatus(StatusContract.ACTIF);
        contract.setAccessoryCost(GlobalConstants.ACCESSOIRE);
        contract.setTax(GlobalConstants.TAXE);

        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusYears(1));
        System.out.println("CONTRACT SAVED ID = " + insured.getId());
        contract.setInsured(insured);


        // AJOUTER LES GARANTIES

        Set<ContractGarantie> garanties = new HashSet<>();
        garanties.add(createHospicash(contract));
        garanties.add(createInvalidity(contract));
        garanties.add(createCapitalFuneraire(contract));
        contract.setGaranties(garanties);
        Contract savedContract = contractRepository.save(contract);
        System.out.println("CONTRACT SAVED ID = " + savedContract.getId());

        return contractMapper.toDTO(savedContract);
    }



    public ContractGarantie createHospicash(Contract contract){
        Garantie garantie = garantieRepository.findByName(GarantieEnum.HOSPICASH)
                .orElseThrow(()->new RuntimeException("Garantie Hospicash absente"));
        return ContractGarantie.builder()
                .contract(contract)
                .garantie(garantie)
                .capitalMax(GlobalConstants.CAPITAL_MAX)
                .plafondNuitsParAn(GlobalConstants.PLAFOND_NB_NUITS_PAR_AN)
                .nuitsRestantes(GlobalConstants.PLAFOND_NB_NUITS_PAR_AN)
                .montantParNuit(GlobalConstants.MONTANT_VERSEMENT_PAR_NUIT)
                .build();
    }

    public ContractGarantie createInvalidity(Contract contract){
        Garantie garantie = garantieRepository.findByName(GarantieEnum.INVALIDITE)
                .orElseThrow(()->new RuntimeException("Garantie invalidité absente"));
        return ContractGarantie.builder()
                .contract(contract)
                .garantie(garantie)
                .capitalMax(GlobalConstants.CAPITAL_MAX)
                .capitalDejaVerse(0.0)
                .build();
    }

    public ContractGarantie createCapitalFuneraire(Contract contract){
        Garantie garantie = garantieRepository.findByName(GarantieEnum.CAPITAL_FUNERAIRE)
                .orElseThrow(()->new RuntimeException("Garantie Capital funéraire absente"));
        return ContractGarantie.builder()
                .contract(contract)
                .garantie(garantie)
                .capitalMax(GlobalConstants.CAPITAL_MAX)
                .capitalDejaVerse(0.0)
                .build();
    }

    private String generatePoliceContract() {
        return "CTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public List<ContractDTO> getAllContracts(){
        List<Contract> allContracts = contractRepository.findAll();
        return  allContracts.stream().map(contractMapper::toDTO).toList();
    }

    public ContractDTO getContractById(Long id){
        Contract contract = contractRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Contract not found"));
        return contractMapper.toDTO(contract);
    }




}
