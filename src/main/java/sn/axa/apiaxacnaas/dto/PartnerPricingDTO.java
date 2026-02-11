package sn.axa.apiaxacnaas.dto;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.PartnerCategory;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerPricingDTO {
    private Long id;
    private PartnerCategory category;
    private Long plafondNuitsParAn; // 30
    private Double montantParNuit; // 5 000
    private Double accessoryCost;
    private Double tax;
    private Double montantPrime;
    private Double montantPrimeTTC;
    private Double capitalMAX;
    private Long partnerId;
    private String partnerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
