package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.InsuredStatus;
import sn.axa.apiaxacnaas.util.PartnerCategory;

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
@Table(name = "tbl_insureds")
public class Insured {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private InsuredStatus status= InsuredStatus.ACTIF;

    private String firstNameBeneficiaire;
    private String lastNameBeneficiaire;
    private String phoneNumberBeneficiaire;
    private String lienParente;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "beneficiary_id",
            unique = true
    )
    private Beneficiary beneficiary;
    @Enumerated(EnumType.STRING)
    private PartnerCategory category; // STANDARD / GOLD / ARGENT

    @OneToOne(mappedBy = "insured")
    private Contract contract;

    @OneToMany(mappedBy = "insured")
    private Set<Claim> claims = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
