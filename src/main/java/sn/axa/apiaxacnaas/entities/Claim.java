package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
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
@Filter(name = "partnerFilter", condition = "partner_id = :partnerId")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "claim_seq", sequenceName = "claim_seq", allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private String numeroSinistre;
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
    private Long numberNuitsHospitalisation;
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimDocument> claimDocuments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insured_id", nullable = false)
    private Insured insured;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(name = "partner_id", insertable = false, updatable = false)
    private Long partnerId;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // utilisateur qui crée
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    // utilisateur qui valide
    @ManyToOne
    @JoinColumn(name = "validated_by")
    private User validatedBy;

    private LocalDateTime validatedAt;

    // utilisateur qui rejette
    @ManyToOne
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    private LocalDateTime rejectedAt;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;

    private LocalDateTime paidAt;


    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("actionDate ASC")
    private List<ClaimHistory> histories;

    private String rejectReason;
}
