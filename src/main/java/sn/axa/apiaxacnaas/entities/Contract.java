package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.StatusContract;
import sn.axa.apiaxacnaas.util.TypeContractEnum;
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
@Table(name = "tbl_contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeContractEnum typeContract;

    private String policeNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Double accessoryCost;
    private Double tax;
    private Double capitalMax;
    private Double capitalDejaVerse;
    private Long plafondNuitsParAn; // 30
    private Long nuitsRestantes;
    private Double montantParNuit; // 5 000

    private Double montantPrimeTTC;
    @Column(nullable = false)
    private Double montantPrime;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insured_id", nullable = false, unique = true)
    private Insured insured;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private StatusContract status; // ACTIF, EXPIRÉ, ANNULÉ

    // Garanties rattachées au contrat
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ContractGarantie> garanties = new HashSet<>();

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
