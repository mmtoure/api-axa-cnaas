package sn.axa.apiaxacnaas.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

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

    @OneToMany(mappedBy = "chefAgency", cascade = CascadeType.ALL)
    private List<Agency> agencies;

    @OneToOne(mappedBy = "manager")
    private Network network;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Region> regions;

    public void addAgence(Agency agence) {
        agencies.add(agence);
        agence.setChefAgency(this);
    }

}
