package sn.axa.apiaxacnaas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.StatusContract;
import sn.axa.apiaxacnaas.util.TypeContractEnum;

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
    private TypeContractEnum typeContract;
    private String policeNumber;
    @JsonFormat(pattern = "dd MMMM yyyy", locale = "fr")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd MMMM yyyy", locale = "fr")
    private LocalDate endDate;
    private String description;
    private Double accessoryCost;
    private Double tax;
    private Double montantPrime;
    private StatusContract status; // ACTIF, EXPIRÉ, ANNULÉ
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ContractGarantieDTO> garanties;
    private Long insuredId;
    private String firstName;
    private String lastName;
    private List<ClaimDTO> claims;
    private Double montantPrimeTtc;
}
