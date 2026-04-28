package sn.axa.apiaxacnaas.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
