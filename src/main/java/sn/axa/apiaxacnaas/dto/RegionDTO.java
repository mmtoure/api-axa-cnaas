package sn.axa.apiaxacnaas.dto;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.RegionEnum;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegionDTO {
    private Long id;
    private RegionEnum name;
    private Long networkId;
    private String networkName;

    private Long userId;
    private String userFirstName;
    private String userLastName;
    private List<String> departments;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
