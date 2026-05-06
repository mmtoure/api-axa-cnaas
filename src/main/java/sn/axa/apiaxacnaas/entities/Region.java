package sn.axa.apiaxacnaas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.axa.apiaxacnaas.util.RegionEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RegionEnum name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    @JsonIgnore
    private Network network;

    @ElementCollection
    @CollectionTable(
            name = "region_departments",
            joinColumns = @JoinColumn(name = "region_id")
    )
    @Column(name = "department")
    private List<String> departments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "region",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insured> insureds = new ArrayList<>();

    @OneToMany(mappedBy = "region",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groups = new ArrayList<>();

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
