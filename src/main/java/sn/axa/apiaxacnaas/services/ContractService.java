package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ContractDTO;
import sn.axa.apiaxacnaas.dto.InsuredDTO;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ContractMapper;
import sn.axa.apiaxacnaas.mappers.InsuredMapper;
import sn.axa.apiaxacnaas.repositories.*;
import sn.axa.apiaxacnaas.util.StatusContract;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final InsuredMapper insuredMapper;


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

    public Page<ContractDTO> getContracts(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Contract> contractPage = contractRepository.findAllByOrderByCreatedAtDesc(pageable);
        return contractPage.map(contractMapper::toDTO);

    }
}
