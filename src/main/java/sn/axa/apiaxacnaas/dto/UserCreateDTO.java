package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.util.PartenaireEnum;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long partnerId;
    private Long networkId;
    private String RoleName;
    private List<RegionDTO> regions;
    private List<Long> regionIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
