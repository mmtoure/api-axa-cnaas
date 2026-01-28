package sn.axa.apiaxacnaas.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.util.InsuredStatus;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private BeneficiaryDTO beneficiary;
    private Set<ClaimDTO> claims;
    private Long groupId;
    private String groupName;
    private UserDTO user;
    private ContractDTO contract;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
