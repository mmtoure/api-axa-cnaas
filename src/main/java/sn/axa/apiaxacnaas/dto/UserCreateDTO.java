package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.util.PartenaireEnum;

import java.time.LocalDateTime;

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
    private String RoleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
