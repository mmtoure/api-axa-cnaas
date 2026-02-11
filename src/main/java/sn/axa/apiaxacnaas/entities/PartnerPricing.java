package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.PartnerCategory;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_partner_pricings")
public class PartnerPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long plafondNuitsParAn; // 30
    private Double montantParNuit; // 5 000
    private Double accessoryCost;
    private Double tax;
    private Double montantPrime;
    private Double capitalMAX;
    private Double montantPrimeTTC;


    @Enumerated(EnumType.STRING)
    private PartnerCategory category; // LG only

    @ManyToOne(optional = false)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
