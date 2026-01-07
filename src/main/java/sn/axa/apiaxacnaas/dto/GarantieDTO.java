package sn.axa.apiaxacnaas.dto;
import lombok.*;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.TypeInvalidityEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GarantieDTO {
    private Long id;
    private GarantieEnum name;
    private TypeInvalidityEnum type_invalidity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
