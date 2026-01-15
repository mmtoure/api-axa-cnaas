package sn.axa.apiaxacnaas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private Long agenceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
