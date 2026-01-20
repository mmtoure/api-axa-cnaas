package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ClaimDocumentDTO;
import sn.axa.apiaxacnaas.entities.Claim;
import sn.axa.apiaxacnaas.entities.ClaimDocument;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ClaimDocumentMapper;
import sn.axa.apiaxacnaas.repositories.ClaimDocumentRepository;
import sn.axa.apiaxacnaas.repositories.ClaimRepository;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimDocumentService {
    private final ClaimDocumentRepository claimDocumentRepository;
    private final ClaimRepository claimRepository;
    private final LocalFileStorageService localFileStorageService;
    private final ClaimDocumentMapper claimDocumentMapper;

    public ClaimDocumentDTO uploadDocumentClaim(ClaimDocumentType typeDocument, Long claimId, MultipartFile file){
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Sinistre introuvable"));
        String filePath = localFileStorageService.store(file, "Claims/"+claimId);

        ClaimDocument claimDocument = ClaimDocument.builder()
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .filePath(filePath)
                .type(typeDocument)
                .claim(claim)
                .build();
        return claimDocumentMapper.toDTO(claimDocument);
    }

    public ClaimDocumentDTO getClaimDocument(Long id){
        ClaimDocument claimDocument = claimDocumentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Document sinistre n'existe pas"));
        return claimDocumentMapper.toDTO(claimDocument);
    }

    public List<ClaimDocumentDTO> getDocumentsByClaim(Long claimId){
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Sinistre introuvable"));
        List<ClaimDocument> claims_documents = claimDocumentRepository.findByClaimId(claim.getId());
        return claimDocumentMapper.toDTOList(claims_documents);


    }

    public void deleteDocumentClaim(Long id){
        ClaimDocument claimDocument = claimDocumentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Document sinistre n'existe pas"));
        localFileStorageService.deleteFile(claimDocument.getFilePath());
        claimDocumentRepository.deleteById(id);
    }
}
