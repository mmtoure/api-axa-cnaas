package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.ClaimDocumentDTO;
import sn.axa.apiaxacnaas.entities.Claim;
import sn.axa.apiaxacnaas.entities.ClaimDocument;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ClaimDocumentMapper;
import sn.axa.apiaxacnaas.repositories.ClaimDocumentRepository;
import sn.axa.apiaxacnaas.repositories.ClaimRepository;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimDocumentService {
    private final ClaimDocumentRepository claimDocumentRepository;
    private final ClaimRepository claimRepository;
   private final FileStorageService storageService;
    private final ClaimDocumentMapper claimDocumentMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;
    @Transactional
    public void uploadClaimDocuments(
            Long claimId,
            List<MultipartFile> files,
            List<ClaimDocumentType> documentTypes
    ) throws IOException {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim introuvable"));

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);
            ClaimDocumentType type = documentTypes.get(i);

            String path = storageService.store(
                    file,
                    "claims/" + claim.getId()
            );

            ClaimDocument doc = ClaimDocument.builder()
                    .claim(claim)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(path)
                    .type(type)
                    .createdAt(LocalDateTime.now())
                    .build();

            claimDocumentRepository.save(doc);
        }
    }

    public void uploadSingleDocument(Long claimId, MultipartFile file, ClaimDocumentType type){
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim introuvable"));
        try {
            String path = storageService.store(
                    file,
                    "claims/" + claim.getId()
            );
            ClaimDocument doc = ClaimDocument.builder()
                    .claim(claim)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(path)
                    .type(type)
                    .createdAt(LocalDateTime.now())
                    .build();

            claimDocumentRepository.save(doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
