package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.ClaimStatus;
import sn.axa.apiaxacnaas.util.GarantieEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate hospitalizationStartDate;
    private LocalDate hospitalizationEndDate;
    private LocalDate accidentDate;
    private String healthStructure;
    private String doctor;
    private String nature;
    private String cause;
    private String motif;
    @Enumerated(EnumType.STRING)
    private GarantieEnum sinisterType;
    private Integer probableDuration;
    private Double compensationAmount;
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimDocument> claimDocuments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insured_id", nullable = false)
    private Insured insured;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
