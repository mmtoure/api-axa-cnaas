package sn.axa.apiaxacnaas.dto;

import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

public record DocumentPayload(
        MultipartFile file,
        ClaimDocumentType type
) {}