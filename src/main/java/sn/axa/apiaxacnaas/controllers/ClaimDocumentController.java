package sn.axa.apiaxacnaas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.axa.apiaxacnaas.entities.ClaimDocument;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.repositories.ClaimDocumentRepository;
import sn.axa.apiaxacnaas.services.FileStorageService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class ClaimDocumentController {
    private final ClaimDocumentRepository claimDocumentRepository;
    private final FileStorageService storageService;

    @GetMapping("/claims/{id}")
    public ResponseEntity<Resource> getClaimDocument(@PathVariable Long id) throws IOException {

        ClaimDocument doc = claimDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable"));

        Resource resource = storageService.load(doc.getFilePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getFileType()))
                .body(resource);
    }
}