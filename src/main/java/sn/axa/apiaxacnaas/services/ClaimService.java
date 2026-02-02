package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.dto.ClaimDocumentDTO;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ClaimDocumentMapper;
import sn.axa.apiaxacnaas.mappers.ClaimMapper;
import sn.axa.apiaxacnaas.repositories.*;
import sn.axa.apiaxacnaas.util.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;
    private final ClaimDocumentService claimDocumentService;
    private final ClaimDocumentMapper claimDocumentMapper;
    private final ContractRepository contractRepository;
    private  final InsuredRepository insuredRepository;
    private final ContractGarantieRepository contractGarantieRepository;
    private final ClaimDocumentRepository claimDocumentRepository;

    public ClaimDTO createClaim(ClaimDTO claimDTO, List<MultipartFile> files, List<ClaimDocumentType> types) throws IOException {
        Insured insured = insuredRepository.findById(claimDTO.getInsuredId())
                .orElseThrow(()->new ResourceNotFoundException("Assure n'existe pas"));
        Contract contract = insured.getContract();
        if(contract.getStatus()!=StatusContract.ACTIF){
            throw  new ResourceNotFoundException("Pas de sinistre car contrat n'est pas actif");
        }

        if(claimDTO.getSinisterType()== GarantieEnum.HOSPICASH){
            if(contract.getCapitalDejaVerse()>=contract.getCapitalMax()){
                throw  new ResourceNotFoundException("Capital MAX est atteint");
            }
            if(contract.getNuitsRestantes()<=0 || getNuitsHospitalisation(claimDTO)>=30){
                throw  new ResourceNotFoundException("Plafond nuit hospitalisation atteint");
            }
            Double montantVerse = GlobalConstants.MONTANT_VERSEMENT_PAR_NUIT*getNuitsHospitalisation(claimDTO);
            contract.setCapitalDejaVerse(contract.getCapitalDejaVerse()+montantVerse);
            contract.setNuitsRestantes(contract.getNuitsRestantes()-getNuitsHospitalisation(claimDTO));
            contractRepository.save(contract);
        }
        if(claimDTO.getSinisterType()==GarantieEnum.INVALIDITE){
            if(contract.getCapitalDejaVerse()>=contract.getCapitalMax()){
                throw  new ResourceNotFoundException("Capital MAX est atteint");
            }
            if(claimDTO.getCompensationAmount()>= GlobalConstants.CAPITAL_MAX){
                throw  new ResourceNotFoundException("Montant MAX dépassé");
            }
            contract.setCapitalDejaVerse(contract.getCapitalDejaVerse()+claimDTO.getCompensationAmount());
            contractRepository.save(contract);
        }

        if(claimDTO.getSinisterType()==GarantieEnum.CAPITAL_FUNERAIRE){
            if(contract.getCapitalDejaVerse()>=contract.getCapitalMax()){
                throw  new ResourceNotFoundException("Capital MAX est atteint");
            }
            claimDTO.setCompensationAmount(contract.getCapitalMax()-contract.getCapitalDejaVerse());
            contract.setCapitalDejaVerse(contract.getCapitalDejaVerse()+claimDTO.getCompensationAmount());
            contractRepository.save(contract);
        }
        if(types==null || files.size() != types.size()) {
            throw new ResourceNotFoundException("Chaque document doit avoir un type");
        }
        // 🔹 Création du sinistre
        Claim claim = Claim.builder()
                .sinisterType(claimDTO.getSinisterType())
                .hospitalizationStartDate(claimDTO.getHospitalizationStartDate())
                .hospitalizationEndDate(claimDTO.getHospitalizationEndDate())
                .compensationAmount(claimDTO.getCompensationAmount())
                .numeroSinistre(generateNumeroSinistre())
                .status(ClaimStatus.EN_COURS)
                .insured(insured)
                .build();
        Claim savedClaim = claimRepository.save(claim);
        claimDocumentService.uploadClaimDocuments(savedClaim.getId(),files, types);


        List<ClaimDocumentDTO> claimDocumentDTO = claimDocumentMapper.toDTOList(claim.getClaimDocuments());
        Claim fullClaim = claimRepository.findById(savedClaim.getId())
                .orElseThrow(() -> new RuntimeException("Claim not found after save"));



        return claimMapper.toDTO(fullClaim);


    }

    public ClaimDTO getClaimById(Long claimId){
        Claim existingClaim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Sinistre avec cette  (id=" + claimId + ") est introuvable"));
        return claimMapper.toDTO(existingClaim);
    }

    public List<ClaimDTO> getAllClaims(){
        List<Claim> claims = claimRepository.findAll();
        return claims.stream().map(claimMapper::toDTO).toList();
    }

    public String generateNumeroSinistre(){
        Long seq = claimRepository.getNextSequence();
        return "SN-" + Year.now().getValue()+ "-" +String.format("%06d",seq);
    }

    public Long getNuitsHospitalisation(ClaimDTO  claimDTO){
        return (ChronoUnit.DAYS.between(claimDTO.getHospitalizationStartDate(),claimDTO.getHospitalizationEndDate()));
    }

    public Double getMontantVerse(ClaimDTO claimDTO, Contract contract){
        Double montantVerse=0.0;
        if(claimDTO.getSinisterType()==GarantieEnum.HOSPICASH){
            Long nightHospicash = getNuitsHospitalisation((claimDTO));
            montantVerse += GlobalConstants.MONTANT_VERSEMENT_PAR_NUIT*nightHospicash;
        }
        if(claimDTO.getSinisterType()==GarantieEnum.INVALIDITE){
            montantVerse+= claimDTO.getCompensationAmount();
        }
        if(claimDTO.getSinisterType()==GarantieEnum.CAPITAL_FUNERAIRE){
            montantVerse = GlobalConstants.CAPITAL_MAX-contract.getCapitalDejaVerse();
        }
        return  montantVerse;
    }

}
