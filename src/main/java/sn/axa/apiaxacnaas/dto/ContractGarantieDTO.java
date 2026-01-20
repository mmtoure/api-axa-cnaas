package sn.axa.apiaxacnaas.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.entities.Contract;
import sn.axa.apiaxacnaas.entities.Garantie;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractGarantieDTO {
    private Long id;
    private Double capitalMax; // 350 000
    private Double capitalDejaVerse;
    private Integer plafondNuitsParAn; // 30
    private Integer nuitsRestantes;
    private Double montantParNuit; // 5 000
    private Long contractId;
    private Long garantieId;
    private String garantieName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
