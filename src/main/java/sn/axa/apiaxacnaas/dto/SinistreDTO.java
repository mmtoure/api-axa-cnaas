package sn.axa.apiaxacnaas.dto;

import jakarta.persistence.*;
import lombok.*;
import sn.axa.apiaxacnaas.entities.Contract;
import sn.axa.apiaxacnaas.entities.SinistreDocument;
import sn.axa.apiaxacnaas.util.CompensationStatusEnum;
import sn.axa.apiaxacnaas.util.GarantieEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SinistreDTO {
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
    private CompensationStatusEnum compensationStatus;
    private Set<SinistreDocument> sinistreDocuments;
    private Long contractId;
}
