package sn.axa.apiaxacnaas.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateClaimDTO {
    private String type;
    private LocalDate hospitalizationStartDate;
    private LocalDate hospitalizationEndDate;

}
