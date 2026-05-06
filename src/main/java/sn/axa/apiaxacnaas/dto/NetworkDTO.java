package sn.axa.apiaxacnaas.dto;


import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NetworkDTO {
    private Long id;
    private String name;
    private Long parentId;
    private String parentName;
    private List<Long> regionIds;
    private Long  managerId;
    private String managerFirstName;
    private String managerLastName;
    private List<RegionDTO> regions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
