package sn.axa.apiaxacnaas.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyDTO {
    private Long id;
    private String name;
    private Long networkId;
    private String networkName;
    private Long chefAgencyId;
    private String chefAgencyName;
    private Long partnerId;
    private String partnerName;
    private List<RegionDTO> regions;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
