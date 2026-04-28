package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.entities.Role;
import sn.axa.apiaxacnaas.entities.Zone;
import sn.axa.apiaxacnaas.util.PartenaireEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
    private Role role;
    private Long partnerId;
    private ZoneDTO zone;
    private List<AgenceDTO> agences;
    private String partnerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
