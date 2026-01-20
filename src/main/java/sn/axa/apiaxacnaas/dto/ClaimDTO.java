package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.entities.ClaimDocument;
import sn.axa.apiaxacnaas.util.ClaimStatus;
import sn.axa.apiaxacnaas.util.GarantieEnum;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimDTO {
    private Long id;
    private LocalDateTime hospitalizationStartDate;
    private LocalDateTime hospitalizationEndDate;
    private LocalDateTime accidentDate;
    private String healthStructure;
    private String doctor;
    private String nature;
    private String cause;
    private GarantieEnum sinisterType;
    private Integer probableDuration;
    private Double compensationAmount;
    private ClaimStatus compensationStatus;
    private List<ClaimDocument> claimDocuments;
    private Long contractId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
