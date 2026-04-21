package sn.axa.apiaxacnaas.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.util.VilleEnum;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgenceDTO {
    private Long id;
    private String name;
    private VilleEnum ville;
    private Long zoneId;
    private Long partnerId;
    private Long chefAgenceId;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
