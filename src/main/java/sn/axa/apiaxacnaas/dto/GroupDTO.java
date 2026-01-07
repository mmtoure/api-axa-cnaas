package sn.axa.apiaxacnaas.dto;

import lombok.*;
import sn.axa.apiaxacnaas.entities.Insured;
import sn.axa.apiaxacnaas.entities.User;

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
    private String firstNameRepresentant;
    private String lastNameRepresentant;
    private String phoneNumberRepresentant;
    private Set<InsuredDTO> insureds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
