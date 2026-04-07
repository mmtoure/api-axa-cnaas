package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.axa.apiaxacnaas.dto.ClaimDocumentDTO;
import sn.axa.apiaxacnaas.entities.ClaimDocument;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.repositories.ClaimDocumentRepository;
import sn.axa.apiaxacnaas.services.ClaimDocumentService;
import sn.axa.apiaxacnaas.services.FileStorageService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/claim_documents")
@RequiredArgsConstructor
public class ClaimDocumentController {
    private final ClaimDocumentService claimDocumentService;
    private final ClaimDocumentRepository claimDocumentRepository;
    private final FileStorageService storageService;

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDocumentDTO> getClaimDocument(@PathVariable Long id) throws IOException {
        ClaimDocumentDTO doc = claimDocumentService.getClaimDocument(id);
        return ResponseEntity.status(HttpStatus.OK).body(doc);
    }
}