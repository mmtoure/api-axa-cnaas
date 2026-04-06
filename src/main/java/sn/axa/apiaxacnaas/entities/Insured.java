package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import sn.axa.apiaxacnaas.util.InsuredStatus;
import sn.axa.apiaxacnaas.util.PartnerCategory;
import sn.axa.apiaxacnaas.util.SubscriptionTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_insureds")
@Entity
@FilterDef(name = "partnerFilter", parameters = @ParamDef(name = "partnerId", type = Long.class))
@Filter(name = "partnerFilter", condition = "partner_id = :partnerId")
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
    private LocalDate subscriptionDate;
    private String identityCardNumber;
    @Enumerated(EnumType.STRING)
    private InsuredStatus insuredStatus;

    @Enumerated(EnumType.STRING)
    private SubscriptionTypeEnum subscriptionType;

    @Enumerated(EnumType.STRING)
    private InsuredStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validated_by", referencedColumnName = "id")
    private User validatedBy;
    private LocalDateTime validatedAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "beneficiary_id",
            unique = true
    )
    private Beneficiary beneficiary;
    @Enumerated(EnumType.STRING)
    private PartnerCategory category; // STANDARD / GOLD / ARGENT

    @OneToOne(mappedBy = "insured",cascade = CascadeType.ALL, orphanRemoval = true)
    private Contract contract;

    @OneToMany(mappedBy = "insured", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Claim> claims = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(name = "partner_id", insertable = false, updatable = false)
    private Long partnerId;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
