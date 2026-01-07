package sn.axa.apiaxacnaas.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaireDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String lienParente;
}
