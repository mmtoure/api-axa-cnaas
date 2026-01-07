package sn.axa.apiaxacnaas.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.*;
import sn.axa.apiaxacnaas.entities.Sinistre;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SinistreDocumentDTO {
    private Long id;
    private String title;
    @Lob
    private byte[] file;
    private String fileContentType;
    private Long sinistreId;
}
