package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.entities.ClaimDocument;
import sn.axa.apiaxacnaas.util.ClaimStatus;
import sn.axa.apiaxacnaas.util.GarantieEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimDTO {
    private Long id;
    private String numeroSinistre;
    private LocalDate hospitalizationStartDate;
    private LocalDate hospitalizationEndDate;
    private LocalDateTime accidentDate;
    private String healthStructure;
    private String doctor;
    private String nature;
    private String cause;
    private String motif;
    private GarantieEnum sinisterType;
    private Integer probableDuration;
    private Double compensationAmount;
    private ClaimStatus status;
    private List<ClaimDocument> claimDocuments;
    private Long insuredId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
