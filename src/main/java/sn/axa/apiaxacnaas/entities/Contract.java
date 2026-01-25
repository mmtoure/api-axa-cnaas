package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.StatusContract;
import sn.axa.apiaxacnaas.util.TypeContractEnum;

import java.math.BigDecimal;
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
    private BigDecimal accessoryCost;
    private BigDecimal tax;
    private BigDecimal montantPrimeTtc;

    @Column(nullable = false)
    private BigDecimal montantPrime;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insured_id", nullable = false, unique = true)
    private Insured insured;

    @Enumerated(EnumType.STRING)
    private StatusContract status; // ACTIF, EXPIRÉ, ANNULÉ

    // Garanties rattachées au contrat
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContractGarantie> garanties = new HashSet<>();

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void calculateMontantPrimeTtc() {
        this.montantPrimeTtc = montantPrime
                .add(accessoryCost)
                .add(tax);
    }


}
