package sn.axa.apiaxacnaas.entities;

import jakarta.persistence.*;
import sn.axa.apiaxacnaas.util.DepartementEnum;


@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DepartementEnum name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
}