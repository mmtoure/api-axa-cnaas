package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.dto.AgenceDTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_zones")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Agence> agences = new HashSet<>();

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Insured> insureds = new HashSet<>();

    @ManyToOne
    private User createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_zone_id", nullable = true)
    private User chefZone;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    public void addAgence(Agence agence) {
        agences.add(agence);
        agence.setZone(this);
    }
}
