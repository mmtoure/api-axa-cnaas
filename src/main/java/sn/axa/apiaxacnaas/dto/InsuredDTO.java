package sn.axa.apiaxacnaas.dto;


import lombok.*;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.util.InsuredStatus;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuredDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private InsuredStatus status;
    private LocalDate dateOfBirth;
    private BeneficiaryDTO beneficiary;
    private ContractDTO contract;
    private Long groupId;
    private String groupName;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
