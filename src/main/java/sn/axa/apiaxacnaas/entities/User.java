package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import sn.axa.apiaxacnaas.util.PartenaireEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_users")

@Filter(name = "partnerFilter", condition = "partner_id = :partnerId")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String activationToken;
    private String profileImageUrl;
    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private Partner partner;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "chefAgence", cascade = CascadeType.ALL)
    private List<Agence> agences;

    @OneToOne(mappedBy = "chefZone")
    private Zone zone;

    public void addAgence(Agence agence) {
        agences.add(agence);
        agence.setChefAgence(this);
    }

}
