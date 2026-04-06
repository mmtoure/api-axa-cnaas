package sn.axa.apiaxacnaas.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;
import sn.axa.apiaxacnaas.entities.BaseEntity;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.util.InsuredStatus;
import sn.axa.apiaxacnaas.util.PartnerCategory;
import sn.axa.apiaxacnaas.util.SubscriptionTypeEnum;


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
    private PartnerCategory category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate subscriptionDate;

    @Column(unique = true, nullable = true)
    private String identityCardNumber;
    private SubscriptionTypeEnum subscriptionType;
    private LocalDate dateOfBirth;
    private BeneficiaryDTO beneficiary;
    private Set<ClaimDTO> claims;
    private Long groupId;
    private String groupName;
    private UserDTO user;
    private Long partnerId;
    private String partnerName;
    private ContractDTO contract;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO validatedBy;
    private LocalDateTime validatedAt;
    private UserDTO createdBy;
}
