package sn.axa.apiaxacnaas.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import sn.axa.apiaxacnaas.util.CompensationStatusEnum;
import sn.axa.apiaxacnaas.util.GarantieEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_sinistres")
public class Sinistre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime hospitalizationStartDate;
    private LocalDateTime hospitalizationEndDate;
    private LocalDateTime accidentDate;
    private String healthStructure;
    private String doctor;
    private String nature;
    private String cause;
    @Enumerated(EnumType.STRING)
    private GarantieEnum sinisterType;
    private Integer probableDuration;
    private Double compensationAmount;
    @Enumerated(EnumType.STRING)
    private CompensationStatusEnum compensationStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sinistre")
    private Set<SinistreDocument> sinistreDocuments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Contract contract;
}
