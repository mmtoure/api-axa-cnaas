package sn.axa.apiaxacnaas.dto;

import jakarta.persistence.Lob;
import lombok.*;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimDocumentDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private ClaimDocumentType type;
    private Long claimId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
