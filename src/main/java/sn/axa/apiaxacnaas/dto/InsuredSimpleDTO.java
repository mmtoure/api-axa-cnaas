package sn.axa.apiaxacnaas.dto;

import lombok.Data;
import sn.axa.apiaxacnaas.util.InsuredStatus;
import sn.axa.apiaxacnaas.util.SubscriptionTypeEnum;

import java.time.LocalDate;

@Data
public class InsuredSimpleDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private LocalDate dateOfBirth;
        private InsuredStatus status;
        private BeneficiaryDTO beneficiary;
        private ContractDTO contract;
        private SubscriptionTypeEnum subscriptionType;
}
