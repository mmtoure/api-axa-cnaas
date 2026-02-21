package sn.axa.apiaxacnaas.dto;

import sn.axa.apiaxacnaas.util.ClaimStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SimpleClaimDTO {
    private Long id;
    private String numeroSinistre;
    private LocalDate hospitalizationStartDate;
    private LocalDate hospitalizationEndDate;
    private ClaimStatus status;
    private Long insuredId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
