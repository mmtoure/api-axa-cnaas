package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.StatusContract;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDTO {
    private Long id;
    private GarantieEnum typeContrat;
    private String policeNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Double accessoryCost;
    private Double tax;
    private Double montantPrime;
    private StatusContract status; // ACTIF, EXPIRÉ, ANNULÉ
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ContractGarantieDTO> garanties;
}
