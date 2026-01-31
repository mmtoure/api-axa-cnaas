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
import sn.axa.apiaxacnaas.util.ClaimDocumentType;
import sn.axa.apiaxacnaas.util.ClaimStatus;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.StatusContract;

import java.io.IOException;
import java.time.LocalDate;
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

        if(insured.getContract().getStatus()!= StatusContract.ACTIF){
            throw  new RuntimeException("Le contract n'est pas actif");
        }
        // 🔹 Création du sinistre
        Claim claim = Claim.builder()
                .sinisterType(claimDTO.getSinisterType())
                .hospitalizationStartDate(claimDTO.getHospitalizationStartDate())
                .hospitalizationEndDate(claimDTO.getHospitalizationEndDate())
                .compensationAmount((Double) (claimDTO.getCompensationAmount()))
                .status(ClaimStatus.EN_COURS)
                .insured(insured)
                .build();
        Claim savedClaim = claimRepository.save(claim);

        System.out.println("type: "+savedClaim.getId());
        if(files!=null && !files.isEmpty()){
            if(types==null || files.size() != types.size()) {
                throw new ResourceNotFoundException("Chaque document doit avoir un type");
            }
            claimDocumentService.uploadClaimDocuments(savedClaim.getId(),files, types);
            return  claimMapper.toDTO(savedClaim);

        }



        List<ClaimDocumentDTO> claimDocumentDTO = claimDocumentMapper.toDTOList(claim.getClaimDocuments());
        Claim fullClaim = claimRepository.findById(savedClaim.getId())
                .orElseThrow(() -> new RuntimeException("Claim not found after save"));

        if(fullClaim.getSinisterType()== GarantieEnum.HOSPICASH){
            int nuits = parseInt(String.valueOf(ChronoUnit.DAYS.between(claimDTO.getHospitalizationStartDate(),claimDTO.getHospitalizationEndDate())));
            Contract contract = insured.getContract();
            ContractGarantie hospicash = contract.getGaranties().stream()
                    .filter(g->g.getGarantie().getName()==GarantieEnum.HOSPICASH)
                    .findFirst()
                    .orElseThrow(()-> new ResourceNotFoundException("Garantie HOSPICASH absente"));
            hospicash.setNuitsRestantes(hospicash.getPlafondNuitsParAn()-nuits);
            contractGarantieRepository.save(hospicash);

        }





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

}
