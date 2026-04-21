package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.entities.Insured;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.util.GroupStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDTO {
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private LocalDate subscriptionDate;
    private Set<InsuredSimpleDTO> insureds;
    private UserDTO user;
    private BigDecimal montantPrimeTtc;

    private UserDTO validatedBy;
    private LocalDateTime validatedAt;
    private UserDTO createdBy;
    private GroupStatus status;
    private String proofPayment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
