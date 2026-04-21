package sn.axa.apiaxacnaas.dto;


import lombok.*;
import sn.axa.apiaxacnaas.util.VilleEnum;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZoneDTO {
    private Long id;
    private String name;
    private PartnerDTO partner;
    private Set<AgenceDTO> agences;
    private Long  chefZoneId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
