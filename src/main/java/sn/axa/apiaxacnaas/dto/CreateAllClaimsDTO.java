package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.util.ClaimDocumentType;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAllClaimsDTO {
    private Long insuredId;

    private List<ClaimDTO> claims;
}
