package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.util.ClaimStatus;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClaimHistoryDTO {
    private Long id;
    private Long claimId;
    private ClaimStatus status;
    private Long userId;
    private String firstName;
    private String lastName;
    private String comment;
    private LocalDateTime actionDate;
}
