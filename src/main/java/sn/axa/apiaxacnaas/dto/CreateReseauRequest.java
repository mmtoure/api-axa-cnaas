package sn.axa.apiaxacnaas.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReseauRequest {
    private String name;
    private List<Long> regionIds;
}
