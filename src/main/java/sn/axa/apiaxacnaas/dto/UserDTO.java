package sn.axa.apiaxacnaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import lombok.*;
import sn.axa.apiaxacnaas.entities.Role;

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
    private Long networkId;
    private List<RegionDTO> regions;
    private List<Long> regionIds;
    private NetworkDTO network;
    private String partnerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
