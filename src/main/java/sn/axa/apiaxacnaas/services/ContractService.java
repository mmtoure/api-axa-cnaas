package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ContractMapper;
import sn.axa.apiaxacnaas.mappers.GarantieMapper;
import sn.axa.apiaxacnaas.mappers.InsuredMapper;
import sn.axa.apiaxacnaas.repositories.*;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.GlobalConstants;
import sn.axa.apiaxacnaas.util.StatusContract;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final InsuredMapper insuredMapper;
    private final GarantieRepository garantieRepository;
    private final GarantieMapper garantieMapper;
    private final GroupRepository groupRepository;
    private final InsuredRepository insuredRepository;
    private final PartnerPricingRepository partnerPricingRepository;
    private final ContractPdfService contractPdfService;

    public ContractDTO createContract(Insured insured) {

        if(insured==null){
            throw  new ResourceNotFoundException("Assuré introuvable");
        }
        Partner partner = insured.getPartner();
        PartnerPricing pricing;
        if(partner.getCode().equals("LG")){
            pricing=partnerPricingRepository
                    .findByPartnerIdAndCategory(partner.getId(), insured.getCategory())
                    .orElseThrow(()->new ResourceNotFoundException("Pricing LG introuvable"));
        }
        else{
            pricing = partnerPricingRepository.findFirstByPartnerId(partner.getId())
                    .orElseThrow(()->new ResourceNotFoundException("Pricing introuvable"));
        }
        Contract contract = new Contract();
        contract.setInsured(insured);
        contract.setPartner(partner);
        contract.setPoliceNumber(generatePoliceContract());
        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusYears(1));
        contract.setStatus(StatusContract.ACTIF);


        //Pricings
        contract.setMontantPrime(pricing.getMontantPrime());
        contract.setMontantPrimeTTC(pricing.getMontantPrimeTTC());
        contract.setCapitalMax(pricing.getCapitalMAX());
        contract.setAccessoryCost(pricing.getAccessoryCost());
        contract.setTax(pricing.getTax());
        contract.setPlafondNuitsParAn(pricing.getPlafondNuitsParAn());
        contract.setMontantParNuit(pricing.getMontantParNuit());
        contract.setNuitsRestantes(pricing.getPlafondNuitsParAn());
        contract.setCapitalDejaVerse(0.0);
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
        List<Contract> allContracts = contractRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return  allContracts.stream().map(contractMapper::toDTO).toList();
    }

    public ContractDTO getContractById(Long id){
        Contract contract = contractRepository
                .findByIdWithGaranties(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat introuvable"));
        return contractMapper.toDTO(contract);
    }
    public byte[] generateContractPdf(Long id) throws IOException {
        Contract contract = contractRepository
                .findByIdWithGaranties(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat introuvable"));
        ContractDTO contractDTO = contractMapper.toDTO(contract);
        InsuredDTO insuredDTO = insuredMapper.toDTO(contract.getInsured());
        return  contractPdfService.generateAndSavePdf(contractDTO,insuredDTO,"contract");
    }

    public Double getPrimeTcc(Double tax, Double montantNet, Double accessoire){
        return  tax+montantNet+accessoire;
    }





}
