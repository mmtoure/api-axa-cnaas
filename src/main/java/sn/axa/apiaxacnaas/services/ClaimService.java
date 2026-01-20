package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ClaimDTO;
import sn.axa.apiaxacnaas.dto.ClaimDocumentDTO;
import sn.axa.apiaxacnaas.entities.Claim;
import sn.axa.apiaxacnaas.entities.Contract;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ClaimDocumentMapper;
import sn.axa.apiaxacnaas.mappers.ClaimMapper;
import sn.axa.apiaxacnaas.repositories.ClaimRepository;
import sn.axa.apiaxacnaas.repositories.ContractRepository;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;
import sn.axa.apiaxacnaas.util.ClaimStatus;
import sn.axa.apiaxacnaas.util.StatusContract;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;
    private final ClaimDocumentService claimDocumentService;
    private final ClaimDocumentMapper claimDocumentMapper;
    private final ContractRepository contractRepository;

    public ClaimDTO createClaim(ClaimDTO claimDTO, List<MultipartFile> files, List<ClaimDocumentType> types){
        Contract contract = contractRepository.findById(claimDTO.getContractId())
                .orElseThrow(()->new ResourceNotFoundException("Contract n'existe pas"));

        if(contract.getStatus()!= StatusContract.ACTIF){
            throw  new RuntimeException("Le contract n'est pas actif");
        }
        // 🔹 Création du sinistre
        Claim claim = Claim.builder()
                .sinisterType(claimDTO.getSinisterType())
                .hospitalizationStartDate(claimDTO.getHospitalizationStartDate())
                .hospitalizationEndDate(claimDTO.getHospitalizationEndDate())
                .status(ClaimStatus.EN_COURS)
                .contract(contract)
                .build();
        Claim savedClaim = claimRepository.save(claim);

        if(files!=null && !files.isEmpty()){
            if(types==null || files.size() != types.size()) {
                throw new RuntimeException("Chaque document doit avoir un type");
            }
            for(int i=0; i<=files.size(); i++){
                claimDocumentService.uploadDocumentClaim(types.get(i),savedClaim.getId(),files.get(i));
            }
        }
        List<ClaimDocumentDTO> claimDocumentDTO = claimDocumentMapper.toDTOList(claim.getClaimDocuments());
        return claimMapper.toDTO(savedClaim);


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
