package sn.axa.apiaxacnaas.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerDTO {
    private Long id;
    private String code; //CNAAS, LG
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private List<PartnerPricingDTO> pricings;
}
